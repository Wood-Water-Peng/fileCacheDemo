package com.example.cachelib;


import android.content.Context;

import com.example.cachelib.bean.FileInfoBean;
import com.example.cachelib.bean.FileInfoBeanFinishUpdate;
import com.example.cachelib.bean.FileInfoBeanUpdate;
import com.example.cachelib.bean.TaskProcessInfo;
import com.example.cachelib.db.DBHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 资源更新的入口类
 * 1.负责检测资源的更新信息
 * 2.负责开启下载任务
 * 3.负责开启解压任务
 * 4.当所有的下载和解压完成之后，更新状态
 */
public class CoreApi {
    private static CoreApi sInstance;
    private final LinkedBlockingQueue<Object> pendingTasks = new LinkedBlockingQueue<>();
    private boolean isStop = false;
    private boolean hasInit;
    private DBHelper dbHelper;
    private Context mContext;

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public void init(Context context) {
        //开辟资源
        if (!hasInit) {
            hasInit = true;
            dbHelper = new DBHelper();
            dbHelper.init(context);
            this.mContext = context;
        }
        startCheck();
    }

    public static synchronized CoreApi getsInstance() {
        if (sInstance == null) {
            sInstance = new CoreApi();
        }
        return sInstance;
    }

    boolean isChecking = false;

    public void startCheck() {
        if (isChecking) {
            LogUtil.log("is checking now...");
        } else {
            String url = "www.test.com";
            pendingTasks.add(new CheckUpdateTask(url));
            //开启主线程，接受任务
            isChecking = true;
            new Thread(new MainTask()).start();
        }
    }

    private void startDownloadTask(List<DownloadTask> tasks, IDownloadListener listener) {
        new Thread(new FileDownloadManagerTask(tasks, listener)).start();
    }


    class MainTask implements Runnable {

        @Override
        public void run() {
            LogUtil.log("主任务开启");
            while (!isStop) {
                Callable downloadTask = null;
                try {
                    Object task = pendingTasks.take();
                    if (task instanceof Callable) {
                        downloadTask = (Callable) task;
                        Object result = downloadTask.call();
                        if (result instanceof CheckUpdateResult) {
                            LogUtil.log("执行更新任务...");
                            handleUpdateResult((CheckUpdateResult) result);
                        }
                    } else if (task instanceof DownloadFinishTask) {
                        //下载任务完成
                        LogUtil.log("下载任务完成");
                    } else if (task instanceof Runnable) {
                        //解压任务
                        LogUtil.log("执行解压任务...");
                        ((Runnable) task).run();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //总的下载数，解压数
            LogUtil.log("主任务退出");
        }
    }

    private void handleUpdateResult(CheckUpdateResult result) {
        File filesDir = mContext.getFilesDir();
        String downloadDir = filesDir.getAbsolutePath() + "/resources";
        //生成下载任务
        List<UpdateInfo> list = result.getList();
        List<DownloadTask> downloadTasks = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            UpdateInfo info = list.get(i);
            downloadTasks.add(new DownloadTask(info.getUrl(), info.getResName(), downloadDir, "zip"));
        }
        FileDownloadManagerTask task = new FileDownloadManagerTask(downloadTasks, new DownloadListenerImpl());
        Executors.newSingleThreadExecutor().submit(task);

    }


    class DownloadListenerImpl implements IDownloadListener {

        @Override
        public void onTaskStart(final TaskProcessInfo processInfo) {
            //查询db
            FileInfoBean bean = dbHelper.getFileInfoDao().queryEntityById(processInfo.getTaskId());
            if (bean != null) {
                //更新db
                bean.setTransferredSize(processInfo.getTransferredSize());
                bean.setTotalSize(processInfo.getTotalSize());
                bean.setStartTime(processInfo.getStartTime());
                dbHelper.getFileInfoDao().updateFileInfo(bean);
                LogUtil.log("onTaskStart update " + processInfo.getFileName());
            } else {
                //插入
                FileInfoBean fileInfoBean = new FileInfoBean();
                fileInfoBean.setTaskId(processInfo.getTaskId());
                fileInfoBean.setUrl(processInfo.getUrl());
                fileInfoBean.setStartTime(processInfo.getStartTime());
                fileInfoBean.setFileName(processInfo.getFileName());
                fileInfoBean.setDownloadPath(new File(processInfo.getDirPath(), processInfo.getFileName() + "." + processInfo.getFileSuffix()).getAbsolutePath());
                LogUtil.log("onTaskStart insert " + processInfo.getFileName());
                dbHelper.getFileInfoDao().insertFileInfo(fileInfoBean);
            }

        }

        @Override
        public void onTaskDowning(TaskProcessInfo processInfo) {
            //更新db
            FileInfoBean bean = dbHelper.getFileInfoDao().queryEntityById(processInfo.getTaskId());
            //更新db
            FileInfoBeanUpdate update = new FileInfoBeanUpdate();
            update.setTransferredSize(processInfo.getTransferredSize());
            update.setFileId(bean.getFileId());
            dbHelper.getFileInfoDao().updateFileDowningInfo(update);
//            LogUtil.log("onTaskDowning " + processInfo.getFileName() + " transferredSize: " + processInfo.getTransferredSize());
        }

        @Override
        public void onTaskFinished(TaskProcessInfo processInfo) {
            //更新db
            FileInfoBean bean = dbHelper.getFileInfoDao().queryEntityById(processInfo.getTaskId());
            //更新db
            FileInfoBeanFinishUpdate update = new FileInfoBeanFinishUpdate();
            update.setEndTime(processInfo.getEndTime());
            update.setTotalSize(processInfo.getTotalSize());
            update.setFileId(bean.getFileId());
            update.setStatus(1);
            dbHelper.getFileInfoDao().updateFileFinishInfo(update);
            LogUtil.log("onTaskFinished " + processInfo.getFileName() + " totalSize: " + processInfo.getTotalSize());
            File filesDir = mContext.getFilesDir();
            String desDir = filesDir.getAbsolutePath() + "/des";
            //提交一个解压任务
            String desFileName = "_" + processInfo.getFileName();
            UnpackTask unpackTask = new UnpackTask(processInfo.getTaskId(), processInfo.getDirPath(), desDir, processInfo.getFileName() + "." + processInfo.getFileSuffix(), desFileName);
            pendingTasks.add(unpackTask);
        }

        @Override
        public void onAllFinished() {
            //提交一个下载完成的任务
            pendingTasks.add(new DownloadFinishTask());
            isStop = true;
        }
    }
}
