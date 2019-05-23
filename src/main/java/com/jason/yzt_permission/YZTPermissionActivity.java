package com.jason.yzt_permission;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.jason.permissionlib.R;
import com.jason.permissionlib.interfaceImpl.IYZTPermissionCallback;
import com.jason.yzt_permission.core.IPermission;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具activity
 */
public class YZTPermissionActivity extends Activity{

    private static final String PARAM_PERMISSION = "param_permission";
    private static final String PARAM_REQUEST_CODE = "param_request_code";

    private String[] mPermissions;
    private int mRequestCode;
    private String params;
    private static IPermission permissionListener;

    List<String>  mPermissionList = new ArrayList<>();


    /**
     * 请求权限
     */
    public static void requestPermission(Context context, String[] permissions, int requestCode, IPermission iPermission) {
            permissionListener = iPermission;
            Intent intent = new Intent(context, YZTPermissionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putStringArray(PARAM_PERMISSION, permissions);
            bundle.putInt(PARAM_REQUEST_CODE, requestCode);
            intent.putExtras(bundle);
            context.startActivity(intent);
            if (context instanceof Activity) {
                ((Activity) context).overridePendingTransition(0, 0);
            }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 权限申请
        setContentView(R.layout.activity_yzt_permission_layout);
        this.mPermissions = getIntent().getStringArrayExtra(PARAM_PERMISSION);
        this.mRequestCode = getIntent().getIntExtra(PARAM_REQUEST_CODE, -1);
        if (mPermissions == null || mRequestCode < 0 || permissionListener == null) {
            this.finish();
            return;
        }

        //检查是否已授权
        if (PermissionUtils.hasPermission(this, mPermissions)) {
            permissionListener.ganted(mRequestCode);
            finish();
            return;
        }
        mPermissionList.clear();//清空已经允许的没有通过的权限
        //逐个判断是否还有未通过的权限
        for (int i = 0;i<mPermissions.length;i++){
            if (ContextCompat.checkSelfPermission(this,mPermissions[i])!=
                    PackageManager.PERMISSION_GRANTED){
                mPermissionList.add(mPermissions[i]);//添加还未授予的权限到mPermissionList中
            }
        }
        //申请权限
        if (mPermissionList.size()>0){//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this,mPermissions,this.mRequestCode);
        }
    }

    /**
     * grantResults对应于申请的结果，这里的数组对应于申请时的第二个权限字符串数组。
     * 如果你同时申请两个权限，那么grantResults的length就为2，分别记录你两个权限的申请结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //创建两个集合存储所有的请求
        List<String> granted = new ArrayList<>();
        List<String> denied = new ArrayList<>();
        for (int i=0;i< permissions.length;i++){
            String prem = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                granted.add(prem);
            }else {
                denied.add(prem);
            }
        }
        //请求权限成功
        if (PermissionUtils.verifyPermission(this, grantResults)) {
            permissionListener.ganted(requestCode);
            finish();
            return;
        }

        //用户点击了不再显示
        if (!PermissionUtils.shouldShowRequestPermissionRationale(this, permissions)) {
            if (permissions.length != grantResults.length) {
                return;
            }
            permissionListener.denied(requestCode);
            finish();
            return;
        }
        //用户取消
        permissionListener.cancled(requestCode);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}
