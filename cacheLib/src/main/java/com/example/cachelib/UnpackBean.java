package com.example.cachelib;

public class UnpackBean {
    String url;
    String path;
    String fileName;

    public UnpackBean(String url, String path, String fileName) {
        this.url = url;
        this.path = path;
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public UnpackBean() {
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
