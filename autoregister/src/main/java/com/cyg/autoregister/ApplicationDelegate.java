package com.cyg.autoregister;

public interface ApplicationDelegate {

    void onCreate();

    void onTerminate();

    void onTrimMemory();

    void onLowMemory();
}
