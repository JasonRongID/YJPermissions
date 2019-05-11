package com.jason.permissionlib;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;

import com.jason.permissionlib.helperImpl.PermissionHelper;

import java.util.Arrays;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/29 10:16 AM
 * desc   : 一个不可变的模型对象，它包含与权限请求相关的所有参数，
 *  例如权限，请求代码和基本原理
 * version: 1.0
 */
public class PermissionRequest {
    private final PermissionHelper mHelper; //权限授权代理类
    private final String[] mPerms; //需要的 授权权限
    private final int mRequestCode; //授权 请求码
    private final String mRationale; //权限描述
    private final String mPositiveButtonText; //同意 按钮的文案
    private final String mNegativeButtonText;//拒绝 按钮的文案
    private final int mTheme; //弹框样式

    private PermissionRequest(PermissionHelper helper,
                              String[] perms,
                              int requestCode,
                              String rationale,
                              String positiveButtonText,
                              String negativeButtonText,
                              int theme) {
        mHelper = helper;
        mPerms = perms.clone();
        mRequestCode = requestCode;
        mRationale = rationale;
        mPositiveButtonText = positiveButtonText;
        mNegativeButtonText = negativeButtonText;
        mTheme = theme;
    }
    @NonNull
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public PermissionHelper getHelper() {
        return mHelper;
    }

    @NonNull
    public String[] getPerms() {
        return mPerms.clone();
    }

    public int getRequestCode() {
        return mRequestCode;
    }

    @NonNull
    public String getRationale() {
        return mRationale;
    }

    @NonNull
    public String getPositiveButtonText() {
        return mPositiveButtonText;
    }

    @NonNull
    public String getNegativeButtonText() {
        return mNegativeButtonText;
    }

    @StyleRes
    public int getTheme() {
        return mTheme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermissionRequest request = (PermissionRequest) o;

        return Arrays.equals(mPerms, request.mPerms) && mRequestCode == request.mRequestCode;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(mPerms);
        result = 31 * result + mRequestCode;
        return result;
    }

    @Override
    public String toString() {
        return "PermissionRequest{" +
                "mHelper=" + mHelper +
                ", mPerms=" + Arrays.toString(mPerms) +
                ", mRequestCode=" + mRequestCode +
                ", mRationale='" + mRationale + '\'' +
                ", mPositiveButtonText='" + mPositiveButtonText + '\'' +
                ", mNegativeButtonText='" + mNegativeButtonText + '\'' +
                ", mTheme=" + mTheme +
                '}';
    }

    /**
     *PermissionRequst 构造器
     */
    public static final class Builder{
        private final PermissionHelper mHelper; //权限授权代理类
        private final String[] mPerms; //需要的 授权权限
        private final int mRequestCode; //授权 请求码

        private  String mRationale; //权限描述
        private  String mPositiveButtonText; //同意 按钮的文案
        private  String mNegativeButtonText;//拒绝 按钮的文案
        private  int mTheme = -1; //弹框样式

        /**
         * 针对 activity 构造
         * @param activity activity
         * @param requestCode 请求码
         * @param perms 请求的权限
         */
        public Builder(@NonNull Activity activity,@NonNull int requestCode,@NonNull String... perms){
            mHelper = PermissionHelper.newInstance(activity);
            mPerms = perms;
            mRequestCode = requestCode;
        }

        /**
         * 针对 Fragment 构造
         * @param fragment activity
         * @param requestCode 请求码
         * @param perms 请求的权限
         */
        public Builder(@NonNull Fragment fragment,@NonNull int requestCode,@NonNull String... perms){
            mHelper = PermissionHelper.newInstance(fragment);
            mPerms = perms;
            mRequestCode = requestCode;
        }
        public Builder setRationale(@NonNull String rationale){
            mRationale = rationale;
            return this;
        }
        public Builder setRationale(@StringRes int rationaleId){
            mRationale = mHelper.getContext().getString(rationaleId);
            return this;
        }
        public Builder setPositiveButtonText(@NonNull String positiveButtonText){
            mPositiveButtonText = positiveButtonText;
            return this;
        }
        public Builder setPositiveButtonText(@StringRes int positiveButtonTextID){
            mPositiveButtonText = mHelper.getContext().getString(positiveButtonTextID);
            return this;
        }
        public Builder setNegativeButtonText(@NonNull String negativeButtonText){
            mNegativeButtonText = negativeButtonText;
            return this;
        }
        public Builder setNegativeButtonText(@StringRes int negativeButtonTextID){
            mNegativeButtonText = mHelper.getContext().getString(negativeButtonTextID);
            return this;
        }
        public Builder setTheme(@StyleRes int theme){
            mTheme = theme;
            return this;
        }

        @NonNull
        public PermissionRequest builder(){
            if (mRationale == null){
                mRationale = mHelper.getContext().getString(R.string.rationale_ask);
            }
            if (mPositiveButtonText == null){
                mPositiveButtonText = mHelper.getContext().getString(android.R.string.ok);
            }
            if(mNegativeButtonText == null){
                mNegativeButtonText = mHelper.getContext().getString(android.R.string.cancel);
            }
            return new PermissionRequest(mHelper,mPerms,mRequestCode,mRationale,mPositiveButtonText,mNegativeButtonText,mTheme);
        }

    }
}
