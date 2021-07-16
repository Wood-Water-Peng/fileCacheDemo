package com.example.cachelib;

import com.example.cachelib.bean.FileInfoBean;
import com.example.cachelib.bean.TaskProcessInfo;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.Callable;

public class DownloadTask implements Callable<DownloadResult> {
    String url;
    String fileName;
    String dir;
    String fileSuffix;

    IDownloadListener listener;
    TaskProcessInfo processInfo = new TaskProcessInfo();

    public DownloadTask(String url, String fileName, String dir, String fileSuffix) {
        this.url = url;
        this.fileName = fileName;
        this.dir = dir;
        this.fileSuffix = fileSuffix;
        processInfo.setUrl(url);
        processInfo.setFileName(fileName);
        processInfo.setDirPath(dir);
        processInfo.setFileSuffix(fileSuffix);
        processInfo.setTaskId(url.hashCode() + "");
    }

    public void setListener(IDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    public DownloadResult call() throws Exception {
        LogUtil.log("开始下载 " + url);
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request.Builder builder = new Request.Builder().url(url);
        //查询当前要下载的资源在本地db中是否存在
        FileInfoBean existTask = CoreApi.getsInstance().getDbHelper().getFileInfoDao().queryEntityById(processInfo.getTaskId());
        if (existTask != null) {
            builder.header("Range", String.format("bytes=%d-", existTask.getTransferredSize()));
        }
        Response response = client.newCall(builder.build()).execute();
        int code = response.code();
        if (code == 200 || code == 206) {
            File dirFile = new File(this.dir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File createFile = new File(dirFile, fileName + "." + fileSuffix);
            if (!createFile.exists()) {
                createFile.createNewFile();
            }
            ResponseBody body = response.body();
            if (listener != null) {
                //对于zip类型，我们必须先把流中的内容全部读出来才能知道totalSize
                processInfo.setStartTime(System.currentTimeMillis());
                listener.onTaskStart(processInfo);
            }
            BufferedSource source = body.source();
            boolean data = processData(source, createFile);
            response.close();
            LogUtil.log("完成下载 " + url);
            return new DownloadResult(this.dir, fileName, fileSuffix);
        } else {
            response.close();
            LogUtil.log("下载出现异常  " + url);
            return new DownloadResult(response.code());
        }
    }

    private boolean processData(BufferedSource bufferedSource, File downloadFile) throws IOException {
        BufferedInputStream mBis = new BufferedInputStream(bufferedSource.inputStream());
        RandomAccessFile mRaFile = new RandomAccessFile(downloadFile, "rw");
        mRaFile.seek(processInfo.getTransferredSize());
        BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(downloadFile));
        byte[] buf = new byte[8192];
        int size = 0;
        long total = 0;
        while ((size = mBis.read(buf)) != -1) {
            total += size;
            fos.write(buf, 0, size);
            if (listener != null) {
                processInfo.setTransferredSize(total);
                listener.onTaskDowning(processInfo);
            }
        }
        if (listener != null) {
            processInfo.setEndTime(System.currentTimeMillis());
            processInfo.setTransferredSize(total);
            processInfo.setTotalSize(total);
            listener.onTaskFinished(processInfo);
        }
        mBis.close();
        fos.close();
        return true;
    }
}
