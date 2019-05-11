package com.jason.permissionlib;

import android.Manifest;
import android.os.Build;
import android.support.v4.util.SimpleArrayMap;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/29 9:43 PM
 * desc   : 常见权限组合
 * version: 1.0
 */
public class PermissionAssembles {
    private static final String TAG = PermissionAssembles.class.getSimpleName();
    private static final SimpleArrayMap<String, Integer> MIN_SDK_PERMISSIONS;

    static {
        MIN_SDK_PERMISSIONS = new SimpleArrayMap<String, Integer>(8);
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW", 23);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS", 23);
    }
    public static final int CODE_RECORD_AUDIO = 0;
    public static final int CODE_GET_ACCOUNTS = 1;
    public static final int CODE_READ_PHONE_STATE = 2;
    public static final int CODE_CALL_PHONE = 3;
    public static final int CODE_CAMERA = 4;
    public static final int CODE_ACCESS_FINE_LOCATION = 5;
    public static final int CODE_ACCESS_COARSE_LOCATION = 6;
    public static final int CODE_READ_EXTERNAL_STORAGE = 7;
    public static final int CODE_WRITE_EXTERNAL_STORAGE = 8;
    public static final int CODE_MULTI_PERMISSION = 100;

    public static final int CODE_CAMERAS = 10_01;
    public static final int CODE_RECORD_AUDIOS = 10_02;
    public static final int CODE_PHONES = 10_03;
    public static final int CODE_SMS_S = 10_04;
    public static final int CODE_STORAGES = 10_05;
    public static final int CODE_LOCATIONS = 10_06;

    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;

    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;

    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;
    public static final String PERMISSION_WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG;


    public static final String PERMISSION_SEND_SMS = Manifest.permission.SEND_SMS;
    public static final String PERMISSION_RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    public static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;

    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public static final String[] CAMERA = {PERMISSION_CAMERA};
    public static final String RECORD_AUDIO = PERMISSION_RECORD_AUDIO;
    public static final String[] PHONE = {PERMISSION_READ_PHONE_STATE,PERMISSION_CALL_PHONE,PERMISSION_READ_CALL_LOG,PERMISSION_WRITE_CALL_LOG};
    public static final String[] SMS = {PERMISSION_SEND_SMS,PERMISSION_RECEIVE_SMS,PERMISSION_READ_SMS};
    public static final String[] STORAGE = {PERMISSION_WRITE_EXTERNAL_STORAGE,PERMISSION_READ_EXTERNAL_STORAGE};
    public static final String[] LOCATION = {PERMISSION_ACCESS_FINE_LOCATION,PERMISSION_ACCESS_COARSE_LOCATION};


    /**
     * 判断特殊权限是否已授权
     * @param permission 权限
     * @return 权限是否已授权
     */
    public static boolean permissionExists(String permission){
        Integer minVersion = MIN_SDK_PERMISSIONS.get(permission);
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion;
    }
}
