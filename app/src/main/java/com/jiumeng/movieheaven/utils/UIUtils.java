package com.jiumeng.movieheaven.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Process;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jiumeng.movieheaven.global.MovieheavenApplication;
import com.jiumeng.movieheaven.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by 7 on 2016/6/15.
 */
public class UIUtils {

    //获取上下文
    public static Context getContext(){
        return MovieheavenApplication.getContext();
    }

    //获取一个handler
    public static Handler getHandler(){
        return MovieheavenApplication.getHandler();
    }

    //获取主线程ID
    public static int getMainThreadId(){
        return MovieheavenApplication.getMainThreadId();
    }


    // /////////////////加载资源文件 ///////////////////////////

    //获取String
    public static String getString(int id){
        return getContext().getResources().getString(id);
    }
    //获取StringArray
    public static String[] getStringArray(int id){
        return getContext().getResources().getStringArray(id);
    }
    //获取Drawable
    public static Drawable getDrawable(int id){
        return getContext().getResources().getDrawable(id);
    }
    //获取Color
    public static int getColor(int id){
        return getContext().getResources().getColor(id);
    }
    //获取Color状态选择器
    public static ColorStateList getColorStateList(int id){
        return getContext().getResources().getColorStateList(id);
    }
    //获取尺寸
    public static int getDimen(int id){
        return getContext().getResources().getDimensionPixelSize(id);
    }

    // /////////////////dip和px转换//////////////////////////
    public static int dip2px(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    public static float px2dip(int px){
        float density = getContext().getResources().getDisplayMetrics().density;
        return px/density;
    }

    // /////////////////加载布局文件//////////////////////////
    public static View inflate(int id){
        return View.inflate(getContext(),id,null);
    }

    //判断是否运行在主线程
    public static boolean isRunOnUIThread(){
        // 获取当前线程id, 如果当前线程id和主线程id相同, 那么当前就是主线程
        if (Process.myTid()==getMainThreadId()){
            return true;
        }else {
            return false;
        }
    }
    //让任务运行在主线程
    public static void runOnUIThread(Runnable r){
        if (isRunOnUIThread()){
            // 已经是主线程, 直接运行
            r.run();
        }else {
            // 如果是子线程, 借助handler让其运行在主线程
            getHandler().post(r);
        }
    }

    /**
     * 隐藏输入法
     */
    public static void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen){
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    /**
     * @return 0:没有网络 1：移动网络 2：wifi网络
     */
    public static int getNetworkType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) UIUtils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return 0;
        }
        int type = networkInfo.getType();
        if (type == ConnectivityManager.TYPE_MOBILE) {
            return 1;
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            return 2;
        }
        return 0;
    }

    public static DisplayImageOptions getDisplayImageOptions(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
//                .showImageForEmptyUri(R.drawable.image_default)
//                .showImageOnFail(R.drawable.image_default)
                .showStubImage(R.drawable.default_movie_image2)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        return options;
    }
    public static DisplayImageOptions getDisplayImageOptions2(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
//                .showImageForEmptyUri(R.drawable.image_default)
//                .showImageOnFail(R.drawable.image_default)
                .showStubImage(R.drawable.default_movie_image)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();//设置圆角显示
        return options;
    }


}
