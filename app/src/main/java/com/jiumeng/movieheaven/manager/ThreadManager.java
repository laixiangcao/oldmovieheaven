package com.jiumeng.movieheaven.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 旧梦 on 2016/6/25.
 */
public class ThreadManager {

    private static ThreadPool mThreadPool;

    public static ThreadPool getThreadManager() {
        if (mThreadPool == null) {
            synchronized (ThreadManager.class) {
                if (mThreadPool==null){
                    int threadCount=10;
                    mThreadPool = new ThreadPool(threadCount,threadCount,1L);
                }
            }
        }
        return mThreadPool;
    }

    public static class ThreadPool {

        private int corePoolSize;// 核心线程数
        private int maximumPoolSize;// 最大线程数
        private long keepAliveTime;// 休息时间

        private ThreadPoolExecutor executor;//线程池对象

        private ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void execute(Runnable r) {
            if (executor==null){
                executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), Executors.defaultThreadFactory());
            }
            // 线程池执行一个Runnable对象, 具体运行时机线程池说了算
            executor.execute(r);
        }
        public void cancel(Runnable r){
            if (executor!=null){
                //获取线程池队列  然后移除队列中的对象
                executor.getQueue().remove(r);
            }

        }
    }
}
