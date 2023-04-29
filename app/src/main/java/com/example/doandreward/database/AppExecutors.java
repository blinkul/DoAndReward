package com.example.doandreward.database;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {

    private final ExecutorService singleThreadRunnable;
    private final ExecutorService singleThreadCallable;
    private final Executor mainThread;

    private static final AppExecutors INSTANCE = new AppExecutors(
            Executors.newSingleThreadExecutor(),
            Executors.newSingleThreadExecutor(),
            new MainThreadExecutor()
    );

    public static AppExecutors getInstance() {
        return INSTANCE;
    }

    private AppExecutors(ExecutorService singleThreadRunnable , ExecutorService singleThreadCallable, Executor mainThread) {
        this.singleThreadRunnable = singleThreadRunnable;
        this.singleThreadCallable = singleThreadCallable;
        this.mainThread = mainThread;
    }

    public ExecutorService getSingleThreadRunnable() {
        return singleThreadRunnable;
    }

    public ExecutorService getSingleThreadCallable() {
        return singleThreadCallable;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    /**
     * Used to execute an UI change outside of Activity class
     */
    public void executeRunnableForUi(Runnable uiRunnable) {
        new Handler(Looper.getMainLooper()).post(uiRunnable);
    }
}
