package com.jason.permissionlib.interfaceImpl;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/299:57 AM
 * desc   : 回调接口，用于接收基本原理对话框的按钮单击事件
 * version: 1.0
 */
public interface IYZTRationaleCallbacks {

    void onRationaleAccepted(int requestCode);

    void onRationaleDenied(int requestCode);

}
