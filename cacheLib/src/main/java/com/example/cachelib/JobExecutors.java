package com.example.cachelib;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class JobExecutors {

    private final Executor mDiskIO;

    private final Executor mNetworkIO;

    private final Executor mMainThread;

    public static class JobHolder {
        public static final JobExecutors INSTANCE = new JobExecutors();
    }

    private JobExecutors(Executor networkIO, Executor diskIO, Executor mainThread) {
        this.mNetworkIO = networkIO;
        this.mDiskIO = diskIO;
        this.mMainThread = mainThread;
    }

    public JobExecutors() {
        this(
                new ThreadPoolExecutor(
                        1,
                        2,
                        30,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>()
                ),
                new ThreadPoolExecutor(
                        1,
                        2,
                        30,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>()
                ),
                new MainThreadExecutor());
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor networkIO() {
        return mNetworkIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}
