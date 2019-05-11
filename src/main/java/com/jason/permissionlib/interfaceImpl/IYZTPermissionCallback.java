package com.jason.permissionlib.interfaceImpl;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/299:50 AM
 * desc   : callback 接收授权回调
 * version: 1.0
 */
public interface IYZTPermissionCallback extends ActivityCompat.OnRequestPermissionsResultCallback {
    void onPermissionsGranted(int requestCode, @NonNull List<String> parmas);
    void onPermissionsDenied(int requestCode, @NonNull List<String> parmas);
}
