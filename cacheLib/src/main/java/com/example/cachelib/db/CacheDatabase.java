package com.example.cachelib.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.cachelib.bean.FileInfoBean;

/**
 * @Author jacky.peng
 * @Date 2021/4/14 9:24 AM
 * @Version 1.0
 */
@Database(entities = {FileInfoBean.class}, version = 1, exportSchema = false)
public abstract class CacheDatabase extends RoomDatabase {
    public abstract FileInfoDao getFileInfoDao();
}
