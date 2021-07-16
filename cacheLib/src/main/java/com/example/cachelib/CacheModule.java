package com.example.cachelib;

import android.content.Context;

import com.example.cachelib.db.DBHelper;

//模块的入口类
public class CacheModule {
    private Context applicationContext;


    public void onCreate(Context context) {
        this.applicationContext = context.getApplicationContext();
        CoreApi.getsInstance().init(applicationContext);
    }
}
