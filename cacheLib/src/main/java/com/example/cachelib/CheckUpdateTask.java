package com.example.cachelib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CheckUpdateTask implements Callable<CheckUpdateResult> {

    String url;
    List<String> urlList = new ArrayList<>();

    public CheckUpdateTask(String url) {
        this.url = url;
        //o2o
        urlList.add("https://pic50.t8tcdn.com/to8to/ias/offline/4964b799147de2fb7e4ea9621351c42e.zip");
        //uni
        urlList.add("https://pic50.t8tcdn.com/to8to/ias/offline/40447615f770339c3abd31b26fce2fd7.zip");
        //static_to8to
        urlList.add("https://pic50.t8tcdn.com/to8to/ias/offline/df8df64e825c830c6c2b2f75dce85f34.zip");
    }

    @Override
    public CheckUpdateResult call() throws Exception {
        LogUtil.log("开始检测更新 " + url);
        Thread.sleep(500);
        List<UpdateInfo> list = new ArrayList<>();
        list.add(new UpdateInfo("https://pic50.t8tcdn.com/to8to/ias/offline/4964b799147de2fb7e4ea9621351c42e.zip", "o2o", 1));
        list.add(new UpdateInfo("https://pic50.t8tcdn.com/to8to/ias/offline/40447615f770339c3abd31b26fce2fd7.zip", "uni", 1));
        list.add(new UpdateInfo("https://pic50.t8tcdn.com/to8to/ias/offline/df8df64e825c830c6c2b2f75dce85f34.zip", "static_t8t", 1));
        CheckUpdateResult result = new CheckUpdateResult(1, list);
        LogUtil.log("完成检测更新 " + url);
        return result;
    }

}
