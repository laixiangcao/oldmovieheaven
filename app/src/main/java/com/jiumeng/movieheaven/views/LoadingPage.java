package com.jiumeng.movieheaven.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jiumeng.movieheaven.R;
import com.jiumeng.movieheaven.utils.UIUtils;


/**
 * 根据当前状态来显示不同页面的自定义控件
 * 未加载 - 加载中 - 加载失败 - 数据为空 - 加载成功--没有网络
 * Created by 7 on 2016/6/15.
 */
public abstract class LoadingPage extends FrameLayout {

    private static final int STATE_LOAD_UNDO = 1;// 未加载
    private static final int STATE_LOAD_LOADING = 2;// 正在加载
    private static final int STATE_LOAD_ERROR = 3;// 加载失败
    private static final int STATE_LOAD_NONETWORK = 4;// 没有网络
    private static final int STATE_LOAD_EMPTY = 5;// 数据为空
    private static final int STATE_LOAD_SUCCESS = 6;// 加载成功
    private int mCurrentState = STATE_LOAD_UNDO;// 当前状态

    private View mLoadingPage;
    private View mEmptyPage;
    private View mErrorPage;
    private View mSuccessPage;
    private View mNoNetwork;

    public LoadingPage(Context context) {
        this(context, null);
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

        //初始化布局 添加 数据为空 加载失败 加载中的布局
        // 初始化加载中的布局
        if (mLoadingPage == null) {
            mLoadingPage = UIUtils.inflate(R.layout.page_loading);
            addView(mLoadingPage);// 将加载中的布局添加给当前的帧布局
        }

        // 初始化加载失败布局
        if (mErrorPage == null) {
            mErrorPage = UIUtils.inflate(R.layout.page_error);
            addView(mErrorPage);
        }
        // 初始化没有网络布局
        if (mNoNetwork == null) {
            mNoNetwork = UIUtils.inflate(R.layout.page_nonetwork);
            addView(mNoNetwork);
        }

        // 初始化数据为空布局
        if (mEmptyPage == null) {
            mEmptyPage = UIUtils.inflate(R.layout.page_empty);
            addView(mEmptyPage);
        }


        TextView btnAgainLoad= (TextView) findViewById(R.id.button1);
        btnAgainLoad.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        //根据当前状态显示适当的布局
        showRightPage();
    }

    private void showRightPage() {

        //根据当前状态 显示不同的布局
        mLoadingPage.setVisibility((mCurrentState == STATE_LOAD_UNDO || mCurrentState == STATE_LOAD_LOADING) ? VISIBLE : GONE);
        mEmptyPage.setVisibility((mCurrentState == STATE_LOAD_EMPTY) ? VISIBLE : GONE);
        mErrorPage.setVisibility((mCurrentState == STATE_LOAD_ERROR) ? VISIBLE : GONE);
        mNoNetwork.setVisibility((mCurrentState == STATE_LOAD_NONETWORK) ? VISIBLE : GONE);


        //添加加载成功的布局(这个 添加成功的布局  一点要写在这里  因为当加载完数据后 会重新调用此方法)
        if (mSuccessPage == null && mCurrentState == STATE_LOAD_SUCCESS) {
            mSuccessPage = onCreateSuccessView();
            if (mSuccessPage != null) {
                addView(mSuccessPage);
            }
        }

        if (mSuccessPage != null) {
            mSuccessPage.setVisibility((mCurrentState == STATE_LOAD_SUCCESS) ? VISIBLE : GONE);
        }

    }

    //开始加载数据
    public void loadData() {
        if (mCurrentState != STATE_LOAD_LOADING) {//当前状态 不是 正在加载中 就开始去加载数据
            mCurrentState = STATE_LOAD_LOADING;  //设置当前状态  表示数据正在加载中
            requestNetData();//开启一个同步加载数据的任务
        }
    }


    // 加载成功后显示的布局, 必须由调用者来实现
    public abstract View onCreateSuccessView();

    // 运行在子线程加载网络数据, 返回值表示请求网络结束后的状态 必须由调用者来实现
    public abstract void requestNetData();

    public void loadDataComplete(ResultState resultState){
        // 根据最新的状态来刷新页面
        mCurrentState=resultState.getState();
        showRightPage();
    }

    //枚举
    public enum ResultState {
        STATE_LOADING(STATE_LOAD_LOADING),
        STATE_EMPTY(STATE_LOAD_EMPTY),
        STATE_ERROR(STATE_LOAD_ERROR),
        STATE_NONETWORK(STATE_LOAD_NONETWORK),
        STATE_SUCCESS(STATE_LOAD_SUCCESS);

        private int state;

        private ResultState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }
}
