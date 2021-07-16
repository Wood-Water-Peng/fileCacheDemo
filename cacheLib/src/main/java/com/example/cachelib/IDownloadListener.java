package com.example.cachelib;

import com.example.cachelib.bean.TaskProcessInfo;

public interface IDownloadListener {
    void onTaskStart(TaskProcessInfo processInfo);

    void onTaskDowning(TaskProcessInfo processInfo);

    void onTaskFinished(TaskProcessInfo result);

    void onAllFinished();
}
