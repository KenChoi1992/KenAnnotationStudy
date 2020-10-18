package com.cyg.annotation;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cyg.permissionlib.annotation.ClickInterval;
import com.cyg.permissionlib.annotation.PermissionCancel;
import com.cyg.permissionlib.annotation.PermissionDenied;
import com.cyg.permissionlib.annotation.PermissionGranted;
import com.cyg.permissionlib.annotation.PermissionRequest;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @ClickInterval()
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
    }

    @PermissionRequest(value = {Manifest.permission.CALL_PHONE}, requestCode = 11)
    public void requestPermission() {
        Log.e("test", "request permission!");
    }

    @PermissionGranted(requestCode = 11)
    public void permissionGranted() {
        Log.e("test", "permission granted!");
    }

    @PermissionDenied(requestCode = 11)
    public void permissionDenied() {
        Log.e("test", "permission denied!");
    }

    @PermissionCancel(requestCode = 11)
    public void permissionCancel() {
        Log.e("test", "permission cancel!");
    }


}