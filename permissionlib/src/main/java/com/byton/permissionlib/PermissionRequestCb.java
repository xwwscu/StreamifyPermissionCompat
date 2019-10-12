package com.byton.permissionlib;

/**
 * permission request callback is also provided
 * Created by wenwu.xie on 2019/10/12.
 */
public interface PermissionRequestCb {
    void onRequestPermissionsResult(PermissionResult permissionResult);
}
