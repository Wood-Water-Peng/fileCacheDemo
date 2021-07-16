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
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * http请求中Content-Length和Range的说明
 * <p>
 * 由于zip文件在服务端被压缩，http的响应头中没有Content-Length字段
 * <p>
 * 请求头的Range字段
 * 假如你请求的资源的总大小是1024
 * 0-     200   返回所有资源
 * 1-1023之间   206   返回客户端请求的范围
 * 1024-  200   返回所有资源
 */
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
            if (existTask.getStatus() == 1) {
                LogUtil.log("任务已经下载 " + url);
                return new DownloadResult(this.dir, fileName, fileSuffix);
            }
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
            RandomAccessFile mRaFile = new RandomAccessFile(createFile, "rw");
            if (code == 206) {
                mRaFile.seek(existTask.getTransferredSize());
            }
            processData(source, mRaFile);
            response.close();
            LogUtil.log("完成下载 " + url);
            return new DownloadResult(this.dir, fileName, fileSuffix);
        } else {
            response.close();
            LogUtil.log("下载出现异常  " + url);
            return new DownloadResult(response.code());
        }
    }

    private boolean processData(BufferedSource bufferedSource, RandomAccessFile randomAccessFile) {
        BufferedInputStream mBis = new BufferedInputStream(bufferedSource.inputStream());
        byte[] buf = new byte[8192];
        int size = 0;
        long total = 0;
        try {
            while ((size = mBis.read(buf)) != -1) {
                total += size;
                randomAccessFile.write(buf, 0, size);
                if (listener != null) {
                    processInfo.setTransferredSize(total);
                    listener.onTaskDowning(processInfo);
                }
                Random random = new Random();
                int i = random.nextInt(100);
                if (i == 9) {
                    throw new IllegalStateException();
                }
            }
            if (listener != null) {
                processInfo.setEndTime(System.currentTimeMillis());
                processInfo.setTransferredSize(total);
                processInfo.setTotalSize(total);
                listener.onTaskFinished(processInfo);
            }

        } catch (Exception e) {
            LogUtil.log("下载出现异常 " + url + " msg: " + e.getMessage());
            return false;
        } finally {
            try {
                mBis.close();
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
