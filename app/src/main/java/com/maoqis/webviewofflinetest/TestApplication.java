package com.maoqis.webviewofflinetest;

import android.app.Application;
import android.content.Context;

public class TestApplication extends Application {
    public static Context context;

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this.getApplicationContext();
    }
}
