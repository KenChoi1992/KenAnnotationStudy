package com.cyg.autoregister;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MainApplication extends Application {

    static List<ApplicationDelegate> applicationDelegateList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
