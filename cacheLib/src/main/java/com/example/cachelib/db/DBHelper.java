package com.example.cachelib.db;

import android.content.Context;

import androidx.room.Room;

public class DBHelper {
    private CacheDatabase database;
    private FileInfoDao fileInfoDao;

    public void init(Context context) {
        createDB(context);
    }

    private void createDB(Context context) {
        database = Room.databaseBuilder(context.getApplicationContext(), CacheDatabase.class, DBParams.DB_NAME) //new a database
                .build();
        fileInfoDao = database.getFileInfoDao();
    }

    public FileInfoDao getFileInfoDao() {
        return fileInfoDao;
    }
}
