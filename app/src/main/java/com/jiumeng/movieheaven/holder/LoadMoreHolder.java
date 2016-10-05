package com.jiumeng.movieheaven.holder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.jiumeng.movieheaven.R;
import com.jiumeng.movieheaven.utils.UIUtils;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class LoadMoreHolder extends BaseViewHolder<Integer>{

    // 加载更多的几种状态
    public static final int STATE_LOADMORE_MORE = 1;// 1. 可以加载更多
    public static final int STATE_LOADMORE_ERROR = 2;// 2. 加载更多失败
    public static final int STATE_LOADMORE_NONE = 3;// 3. 没有更多数据
    private ProgressBar mProgressBar;
    private TextView mLoadState;
    private View mView;


    public LoadMoreHolder(){
        setData(STATE_LOADMORE_MORE);
    }

    @Override
    protected View initView() {
        mView = UIUtils.inflate(R.layout.list_item_loadmore_footview);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.pb_progress);
        mLoadState = (TextView) mView.findViewById(R.id.text);
        return mView;
    }

    @Override
    protected void refreshView(Integer data) {
        switch (data){
            case STATE_LOADMORE_MORE:
                mProgressBar.setVisibility(View.VISIBLE);
                mLoadState.setVisibility(View.VISIBLE);
                mLoadState.setText("正在加载中...");
                break;
            case STATE_LOADMORE_ERROR:
                mProgressBar.setVisibility(View.GONE);
                mLoadState.setVisibility(View.VISIBLE);
                mLoadState.setText("加载失败");
                break;
            case STATE_LOADMORE_NONE:
                mProgressBar.setVisibility(View.GONE);
                mLoadState.setVisibility(View.VISIBLE);
                mLoadState.setText("已加载全部");
                break;
        }
    }
}
