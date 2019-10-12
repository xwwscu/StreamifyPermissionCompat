package com.byton.permissionlib;

/**
 * Created by wenwu.xie on 2019/10/11.
 */
public class PermissionResult {
    public int requestCode;
    public String[] permissions;
    public int[] grantResults;

    public PermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        this.requestCode = requestCode;
        this.permissions = permissions;
        this.grantResults = grantResults;
    }
}
