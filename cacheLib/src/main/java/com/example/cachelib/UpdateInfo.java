package com.example.cachelib;

public class UpdateInfo {
    String url;
    String resName;
    int versionCode;

    public UpdateInfo(String url, String resName, int versionCode) {
        this.url = url;
        this.resName = resName;
        this.versionCode = versionCode;
    }

    public String getUrl() {
        return url;
    }

    public String getResName() {
        return resName;
    }

    public int getVersionCode() {
        return versionCode;
    }
}
