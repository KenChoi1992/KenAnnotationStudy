package com.cyg.autoregister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * 利用注解实现在组件化的项目中自动注册组件的功能
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}