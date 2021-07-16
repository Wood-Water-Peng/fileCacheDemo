package com.example.cachelib.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cachelib.db.DBParams;

@Entity(tableName = DBParams.FILE_TABLE_NAME)
public class FileInfoBean {
    @PrimaryKey(autoGenerate = true)
    private int fileId;

    @ColumnInfo(name = "taskId")
    private String taskId;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "downloadPath")
    private String downloadPath;

    @ColumnInfo(name = "fileName")
    private String fileName;

    //文件状态   1-有效  2-破损  3-无效
    @ColumnInfo(name = "status")
    private int status;

    @ColumnInfo(name = "transferredSize")
    private long transferredSize;

    @ColumnInfo(name = "totalSize")
    private long totalSize;

    @ColumnInfo(name = "startTime")
    private long startTime;

    @ColumnInfo(name = "endTime")
    private long endTime;

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    //
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
//
    public void setUrl(String url) {
        this.url = url;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTransferredSize(long transferredSize) {
        this.transferredSize = transferredSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
//
    public String getTaskId() {
        return taskId;
    }
//
    public String getUrl() {
        return url;
    }
//
    public String getDownloadPath() {
        return downloadPath;
    }

    public String getFileName() {
        return fileName;
    }

    public int getStatus() {
        return status;
    }

    public long getTransferredSize() {
        return transferredSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }
}
