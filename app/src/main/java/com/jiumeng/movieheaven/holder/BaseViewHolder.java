package com.jiumeng.movieheaven.holder;

import android.view.View;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public abstract class BaseViewHolder<T> {

    private final View mRootView;
    private T data;

    //3、打标记
    protected BaseViewHolder(){
        mRootView = initView();
        mRootView.setTag(this);
    }
    /**
     * 1、加载布局文件
     * 2、找到布局文件
     * 具体加载怎样的布局由子类去实现
     * @return 返回加载的布局文件
     */
    protected abstract View initView();

    //4、根据数据刷新布局 具体实现内容由子类实现
    protected abstract void refreshView(T data);

    public void setData(T data){
        this.data=data;
        refreshView(this.data);
    }

    public View getRootView() {
        return mRootView;
    }

    public T getData() {
        return data;
    }
}
