package com.example.cachelib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 负责管理所有的下载任务，执行完之后，发送一个完成信息
 */
public class FileDownloadManagerTask implements Runnable {
    List<? extends DownloadTask> pendingTask = new ArrayList<>();
    IDownloadListener listener;

    public FileDownloadManagerTask(List<? extends DownloadTask> pendingTask, IDownloadListener listener) {
        this.pendingTask = pendingTask;
        this.listener = listener;
    }

    @Override
    public void run() {
        LogUtil.log("FileDownloadManagerTask is running...");
        Iterator<? extends DownloadTask> iterator = pendingTask.iterator();
        while (iterator.hasNext()) {
            try {
                DownloadTask next = iterator.next();
                next.setListener(listener);
                DownloadResult result = next.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
