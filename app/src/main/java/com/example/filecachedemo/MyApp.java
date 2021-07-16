package com.example.filecachedemo;

import android.app.Application;

import com.example.cachelib.CacheModule;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CacheModule cacheModule = new CacheModule();
        cacheModule.onCreate(this);
    }
}
