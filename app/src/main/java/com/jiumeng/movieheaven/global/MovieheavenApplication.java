package com.jiumeng.movieheaven.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 自定义application, 进行全局初始化
 * Created by 7 on 2016/6/15.
 */
public class MovieheavenApplication extends Application {

    private static Handler handler;
    private static int mainThreadId;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mainThreadId = Process.myTid();
        handler = new Handler();

        //初始化ImageLoader
        ImageLoader instance = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .diskCacheSize(60*1024*1024)
                .diskCacheFileCount(100)
                .threadPoolSize(5)
                .build();
        instance.init(config);
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }
}
