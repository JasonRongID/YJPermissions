package com.jason.yzt_permission.core;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/29 9:49 AM
 * desc   : 动态权限 接口参数
 * version: 1.0
 */
public interface IPermission {

    /**
     * 已经授权
     */
    void ganted(int requestCode);

    /**
     * 取消授权
     */
    void cancled(int requestCode);

    /**
     *被拒绝 点击了不再提示
     */
    void denied(int requestCode);

}
