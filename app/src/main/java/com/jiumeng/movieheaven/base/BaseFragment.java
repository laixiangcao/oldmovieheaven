package com.jiumeng.movieheaven.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jiumeng.movieheaven.utils.UIUtils;
import com.jiumeng.movieheaven.views.LoadingPage;
import java.util.ArrayList;

/**
 * Created by 7 on 2016/6/15.
 */
public abstract class BaseFragment extends Fragment {

    private LoadingPage mLoadingPage;
    protected LayoutInflater mInflater;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mLoadingPage==null){
            this.mInflater=inflater;
            //获取一个 加载页面--里面维护了 当加载网络数据失败 、为空、加载中、成功 的相应界面
            // 覆写onCreateSuccessView（）方法 由子类决定 加载成功需要显示的布局文件
            // 覆写onLoad（）方法  返回的是 加载网络数据后的状态  根据此状态去显示 对应的布局文件
            mLoadingPage = new LoadingPage(UIUtils.getContext()) {
                public View onCreateSuccessView() {
                    // 注意:此处一定要调用BaseFragment的onCreateSuccessView, 否则栈溢出
                    return BaseFragment.this.onCreateSuccessView();
                }
                public void requestNetData() {
                    loadDataComplete(ResultState.STATE_LOADING);//设置正在加载中
                    BaseFragment.this.requestNetData();
                }
            };
        }
        return mLoadingPage;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (mLoadingPage!=null){
            mLoadingPage.loadData();
        }
    }
    protected void loadDataComplete(LoadingPage.ResultState resultState){
        mLoadingPage.loadDataComplete(resultState);
    }
    /**
     * 请求网络数据
     * @return
     */
    public abstract void requestNetData();

    /**
     * 2、加载网络数据成功时 显示的布局
     * @return
     */
    public abstract View onCreateSuccessView();



    /**
     * 校验集合数据
     * @param obj 需要校验的集合对象
     * @return 返回页面显示状态
     */
    // 对网络返回数据的合法性进行校验
    protected LoadingPage.ResultState check(Object obj) {
        if (obj != null) {
            if (obj instanceof ArrayList) {// 判断是否是集合
                ArrayList list = (ArrayList) obj;
                if (list.isEmpty()) {
                    return LoadingPage.ResultState.STATE_EMPTY;
                } else {
                    return LoadingPage.ResultState.STATE_SUCCESS;
                }
            }
        }
        return LoadingPage.ResultState.STATE_ERROR;
    }
}
