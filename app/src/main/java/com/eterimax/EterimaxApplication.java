package com.eterimax;

import android.app.Application;

public class EterimaxApplication extends Application {

    private static EterimaxApplication appInstance = new EterimaxApplication();

    public static EterimaxApplication getInstance() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
    }
}
