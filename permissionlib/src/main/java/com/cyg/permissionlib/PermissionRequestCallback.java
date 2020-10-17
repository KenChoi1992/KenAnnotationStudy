package com.cyg.permissionlib;


public interface PermissionRequestCallback {

    void permissionGranted();

    /**
     * 用户点了不再提示，直接拒绝授权
     * @param requestCode
     */
    void permissionDenied(int requestCode);

    /**
     * 用户拒绝授权
     * @param requestCode
     */
    void permissionCancel(int requestCode);
}
