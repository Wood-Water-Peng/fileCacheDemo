package com.example.cachelib;

public class DownloadResult {
    int status;
    String dirPath;
    String fileName;
    String fileSuffix;

    public DownloadResult(String dirPath, String fileName, String fileSuffix) {
        this.dirPath = dirPath;
        this.fileName = fileName;
        this.fileSuffix = fileSuffix;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public DownloadResult(int status) {
        this.status = status;
    }

    public String getDirPath() {
        return dirPath;
    }

    public String getFileName() {
        return fileName;
    }

    public int getStatus() {
        return status;
    }

}
