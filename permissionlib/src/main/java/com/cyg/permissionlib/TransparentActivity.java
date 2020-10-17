package com.cyg.permissionlib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class TransparentActivity extends Activity {

    public static final String KEY_PERMISSIONS = "permissions";
    public static final String KEY_REQUEST_CODE = "requestCode";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("test", "TransparentActivity onCreate");
        Intent intent = getIntent();
        if (intent != null) {
            String[] permissions = intent.getStringArrayExtra(KEY_PERMISSIONS);
            if (permissions != null) {
                int requestCode = intent.getIntExtra(KEY_REQUEST_CODE, -1);
                if (PermissionUtil.hasPermissions(this, permissions)) {
                    if (PermissionUtil.sCallback != null) {
                        PermissionUtil.sCallback.permissionGranted();
                        return;
                    }
                }
                ActivityCompat.requestPermissions(this, permissions, requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtil.permissionGranted(grantResults)) {
            if (PermissionUtil.sCallback != null) {
                PermissionUtil.sCallback.permissionGranted();
                finish();
                return;
            }
        }
        if (PermissionUtil.shouldShowRequestPermissionRationale(this, permissions)) {
            Log.e("test", "用户点了不再提示");
            if (PermissionUtil.sCallback != null) {
                PermissionUtil.sCallback.permissionDenied(requestCode);
                finish();
                return;
            }
        }
        if (PermissionUtil.sCallback != null) {
            PermissionUtil.sCallback.permissionCancel(requestCode);
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
