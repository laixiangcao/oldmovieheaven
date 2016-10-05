package com.jiumeng.movieheaven.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class SuperRefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    private int mTouchSlop;//触发移动事件的最短距离
    private boolean mCanLoadMore = false;//是否能加载更多
    private boolean mIsOnLoading = false;//是否正在加载中
    private boolean mIsMoving = false;//是都正在移动中
    private int mDownY;//按下屏幕时 Y点距离
    private int mLastY;//抬起时 Y点距离
    private ListView mListView;
    private SuperRefreshLayoutListener mListener;

    public SuperRefreshLayout(Context context) {
        super(context);
    }

    public SuperRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();//获取触发移动事件的最短距离
        setOnRefreshListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mListView == null) {
            getListView();
        }
    }

    /**
     * 获取listview
     */
    private void getListView() {
        int child = getChildCount();
        if (child > 0) {
            View childView = getChildAt(0);
            if (childView instanceof ListView) {
                mListView = (ListView) childView;
                // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                mListView.setOnScrollListener(this);
            }
        }
    }

    @Override
    public void onRefresh() {
        if (mListener!=null&&!mIsOnLoading){
            mListener.onRefresh();
        }
    }

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (mListener != null) {
            setIsOnLoading(true);
            mListener.onLoadMore();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 滚动时到了最底部也可以加载更多
        if (canLoad()){
            loadData();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mLastY = (int) ev.getRawY();
                mIsMoving = true;
                break;
            case MotionEvent.ACTION_UP:
                mIsMoving = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否可以加载更多， 条件是listview不再加载中、listview到了最底部、且为上拉操作、且允许加载更多
     *
     * @return 是否可以加载更多
     */
    private boolean canLoad() {
        return isInBottom() && !mIsOnLoading && isPullUp() && mCanLoadMore;
    }

    /**
     * 是否时上拉操作
     *
     * @return 是否时上拉操作
     */
    private boolean isPullUp() {
        return (mDownY - mLastY) >= mTouchSlop;
    }

    /**
     * 设置正在加载
     *
     * @return loading
     */
    private void setIsOnLoading(boolean loading) {
        mIsOnLoading = loading;
        if (!mIsOnLoading) {
            mDownY = 0;
            mLastY = 0;
        }
    }

    /**
     * 判断是否到了最底部
     */
    private boolean isInBottom() {
        return (mListView != null && mListView.getAdapter() != null
                && mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1));
    }

    /**
     * 是否在移动中
     *
     * @return 是否在移动中
     */
    public boolean isMoving() {
        return mIsMoving;
    }

    /**
     * 加载结束记得调用
     */
    public void onLoadComplete(){
        setIsOnLoading(false);
        setRefreshing(false);
    }

    /**
     * 设置listview是能加载更多
     */
    public void setCanLoadMore() {
        this.mCanLoadMore = true;
    }

    /**
     * 设置加载更多时  没有更多数据了
     */
    public void setNoMoreData(){
        this.mCanLoadMore=false;
    }

    public void setSuperRefreshLayoutListener(SuperRefreshLayoutListener loadListener) {
        mListener = loadListener;
    }

    public interface SuperRefreshLayoutListener {
        void onRefresh();

        void onLoadMore();
    }
}
