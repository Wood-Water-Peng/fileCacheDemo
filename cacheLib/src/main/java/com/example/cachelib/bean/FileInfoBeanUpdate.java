package com.example.cachelib.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cachelib.db.DBParams;

public class FileInfoBeanUpdate {
    private int fileId;
    private long transferredSize;

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public long getTransferredSize() {
        return transferredSize;
    }

    public void setTransferredSize(long transferredSize) {
        this.transferredSize = transferredSize;
    }
}
