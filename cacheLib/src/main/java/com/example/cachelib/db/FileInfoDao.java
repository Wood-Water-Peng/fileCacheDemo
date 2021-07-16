package com.example.cachelib.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cachelib.bean.FileInfoBean;
import com.example.cachelib.bean.FileInfoBeanFinishUpdate;
import com.example.cachelib.bean.FileInfoBeanUpdate;

@Dao
public interface FileInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFileInfo(FileInfoBean entity);


    @Update(entity = FileInfoBean.class)
    int updateFileDowningInfo(FileInfoBeanUpdate... entities);
    @Update(entity = FileInfoBean.class)
    int updateFileFinishInfo(FileInfoBeanFinishUpdate... entities);

    @Update(entity = FileInfoBean.class)
    int updateFileInfo(FileInfoBean... entities);

    @Delete
    void deleteFileInfo(FileInfoBean... entities);

    @Query("SELECT * FROM download_table where taskId =:taskId")
    FileInfoBean queryEntityById(String taskId);
}
