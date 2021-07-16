package com.example.cachelib;

import java.util.List;

public class CheckUpdateResult {
    int status;
    List<UpdateInfo> list;

    public CheckUpdateResult(int status, List<UpdateInfo> list) {
        this.status = status;
        this.list = list;
    }

    public int getStatus() {
        return status;
    }

    public List<UpdateInfo> getList() {
        return list;
    }
}
