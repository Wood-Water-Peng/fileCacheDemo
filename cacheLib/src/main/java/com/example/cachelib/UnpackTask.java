package com.example.cachelib;

import com.example.cachelib.bean.FileInfoBean;

import java.io.File;
import java.io.FileInputStream;

public class UnpackTask implements Runnable {
    String srcDir;
    String desDir;
    String srcFileName;
    String desFileName;
    String taskId;

    public UnpackTask(String taskId,String srcDir, String desDir, String srcFileName, String desFileName) {
        this.taskId=taskId;
        this.srcDir = srcDir;
        this.desDir = desDir;
        this.srcFileName = srcFileName;
        this.desFileName = desFileName;
    }

    @Override
    public void run() {
        File srcFile = new File(srcDir, srcFileName);
        File desFile = new File(desDir, desFileName);
        LogUtil.log("开始解压任务 srcFile: " + srcFile.getPath() + "  desFile: " + desFile.getPath());

        //检查要解压的数据在db中的状态
        FileInfoBean existTask = CoreApi.getsInstance().getDbHelper().getFileInfoDao().queryEntityById(taskId);
        if (existTask == null) {
            LogUtil.log("解压任务失败 srcFile: " + srcFile.getPath() + "  desFile: " + desFile.getPath() + " db中无对应文件的信息");
        } else if (existTask.getStatus() != 1) {
            LogUtil.log("解压任务失败 srcFile: " + srcFile.getPath() + "  desFile: " + desFile.getPath() + " 文件不完整");

        } else {
            long startTime = System.currentTimeMillis();
            try {
                FileInputStream fis = new FileInputStream(srcFile);
                if (!desFile.exists()) {
                    desFile.mkdirs();
                }
                FileUtil.unpackZipPkg(desFile, fis);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogUtil.log("解压任务完成 srcFile: " + srcFile.getPath() + "  desFile: " + desFile.getPath() + " 耗时：" + (System.currentTimeMillis() - startTime));

        }
    }
}
