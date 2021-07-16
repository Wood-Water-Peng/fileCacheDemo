package com.example.cachelib;

import java.io.File;
import java.io.FileInputStream;

public class UnpackTask implements Runnable {
    String srcDir;
    String desDir;
    String srcFileName;
    String desFileName;

    public UnpackTask(String srcDir, String desDir, String srcFileName, String desFileName) {
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
