package com.jason.permissionlib.rationaleDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/29 1:57 PM
 * desc   : 提示权限声明对话框 配置
 * version: 1.0
 */
public class RationaleDialogConfig {
    private static final String KEY_POSITIVE_BUTTON = "positiveButton";
    private static final String KEY_NEGATIVE_BUTTON = "negativeButton";
    private static final String KEY_RATIONALE_MESSAGE = "rationaleMsg";
    private static final String KEY_THEME = "theme";
    private static final String KEY_REQUEST_CODE = "requestCode";
    private static final String KEY_PERMISSIONS = "permissions";

    String positiveButton;
    String negativeButton;
    int theme;
    int requestCode;
    String rationaleMsg;
    String[] permissions;

    RationaleDialogConfig(@NonNull String positiveButton,
                          @NonNull String negativeButton,
                          @NonNull String rationaleMsg,
                          @StyleRes int theme,
                          int requestCode,
                          @NonNull String[] permissions) {

        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
        this.rationaleMsg = rationaleMsg;
        this.theme = theme;
        this.requestCode = requestCode;
        this.permissions = permissions;
    }

    /**
     * 构造器
     * @param bundle 对话框需要的数据
     */
    RationaleDialogConfig(Bundle bundle) {
        positiveButton = bundle.getString(KEY_POSITIVE_BUTTON);
        negativeButton = bundle.getString(KEY_NEGATIVE_BUTTON);
        rationaleMsg = bundle.getString(KEY_RATIONALE_MESSAGE);
        theme = bundle.getInt(KEY_THEME);
        requestCode = bundle.getInt(KEY_REQUEST_CODE);
        permissions = bundle.getStringArray(KEY_PERMISSIONS);
    }

    Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_POSITIVE_BUTTON, positiveButton);
        bundle.putString(KEY_NEGATIVE_BUTTON, negativeButton);
        bundle.putString(KEY_RATIONALE_MESSAGE, rationaleMsg);
        bundle.putInt(KEY_THEME, theme);
        bundle.putInt(KEY_REQUEST_CODE, requestCode);
        bundle.putStringArray(KEY_PERMISSIONS, permissions);

        return bundle;
    }

    /**
     * 创建 对话框 权限请求
     * @param context 上下文
     * @param listener 按钮监听
     * @return 返回AlertDialog对话框
     */
    AlertDialog createSupportDialog(Context context, Dialog.OnClickListener listener) {
        AlertDialog.Builder builder;
        if (theme > 0) {
            builder = new AlertDialog.Builder(context, theme);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        return builder
                .setCancelable(false)
                .setPositiveButton(positiveButton, listener)
                .setNegativeButton(negativeButton, listener)
                .setMessage(rationaleMsg)
                .create();
    }

    /**
     * 创建 对话框 权限请求
     * @param context 上下文
     * @param listener 按钮监听
     * @return 返回AlertDialog对话框
     */
    android.app.AlertDialog createFrameworkDialog(Context context, Dialog.OnClickListener listener) {
        android.app.AlertDialog.Builder builder;
        if (theme > 0) {
            builder = new android.app.AlertDialog.Builder(context, theme);
        } else {
            builder = new android.app.AlertDialog.Builder(context);
        }
        return builder
                .setCancelable(false)
                .setPositiveButton(positiveButton, listener)
                .setNegativeButton(negativeButton, listener)
                .setMessage(rationaleMsg)
                .create();
    }


}
