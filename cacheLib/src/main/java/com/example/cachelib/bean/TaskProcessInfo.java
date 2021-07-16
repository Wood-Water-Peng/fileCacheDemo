package com.example.cachelib.bean;

import androidx.annotation.NonNull;

//任务下载进度
public class TaskProcessInfo implements Cloneable {
    String taskId;
    String fileName;
    String dirPath;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;
    String fileSuffix;
    long transferredSize;
    long totalSize;
    long startTime;
    long endTime;

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public long getTransferredSize() {
        return transferredSize;
    }

    public void setTransferredSize(long transferredSize) {
        this.transferredSize = transferredSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    protected TaskProcessInfo clone() throws CloneNotSupportedException {
        return (TaskProcessInfo) super.clone();
    }

    public TaskProcessInfo deepClone() throws CloneNotSupportedException {
        return clone();
    }
}
