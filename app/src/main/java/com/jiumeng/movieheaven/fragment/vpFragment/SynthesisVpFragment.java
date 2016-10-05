package com.jiumeng.movieheaven.fragment.vpFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.jiumeng.movieheaven.adapter.ListBaseAdapter;
import com.jiumeng.movieheaven.adapter.NewestApapter;
import com.jiumeng.movieheaven.bean.MovieDao;
import com.jiumeng.movieheaven.moviedata.MovieHelper;
import com.jiumeng.movieheaven.activity.EmptyActivity;
import com.jiumeng.movieheaven.base.BaseListFragment;
import com.jiumeng.movieheaven.utils.UIUtils;
import com.jiumeng.movieheaven.holder.ListViewHeaderHolder;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class SynthesisVpFragment extends BaseListFragment {

    private NewestApapter mNewestApapter;
    private ArrayList<MovieDao> mDataList;
    private ArrayList<MovieDao> moreData;

    @Override
    protected void initData() {
        mDataList = MovieHelper.getInstance().initData();
        loadDataComplete(check(mDataList));
    }

    @Override
    protected ListBaseAdapter getListAdapter() {
        mNewestApapter = new NewestApapter(mDataList);
        return mNewestApapter;
    }

    @Override
    protected View setHeaderView() {
        ListViewHeaderHolder headerHolder = new ListViewHeaderHolder();
        ArrayList<String> strings = new ArrayList<>();
        strings.add("https://img3.doubanio.com/view/photo/photo/public/p2364579311.jpg");
        strings.add("https://img3.doubanio.com/view/photo/photo/public/p2366969782.jpg");
        strings.add("https://img3.doubanio.com/view/photo/photo/public/p2367560191.jpg");
        strings.add("https://img3.doubanio.com/view/photo/photo/public/p926130702.jpg");
        headerHolder.setData(strings);
        return headerHolder.getRootView();
    }

    protected boolean isNeedHeader() {
        return true;
    }

    @Override
    protected void requestRefreshData() {
        ArrayList<MovieDao> refreshData = MovieHelper.getInstance().refreshData();
        if (refreshData!=null){
            mNewestApapter.addRefreshData(refreshData);
            showUpdateResult(refreshData.size());
        }else {
            showUpdateResult(0);
        }
    }

    @Override
    protected void requestLoadMoreData() {

        new Thread(){
            @Override
            public void run() {
                moreData = MovieHelper.getInstance().loadMore();
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (moreData==null){
                            if (UIUtils.getNetworkType()==0){
                                setFooterType(TYPE_NET_ERROR);//没有网络
                            }else {
                                setFooterType(TYPE_ERROR);//加载失败
                            }
                        }else{
                            if (moreData.size()==0){
                                setFooterType(TYPE_NO_MORE);//没有更多数据
                            }else if (moreData.size()>0){
                                mNewestApapter.addMoreData(moreData);
                                setFooterType(TYPE_NORMAL);
                            }
                        }
                    }
                });
            }
        }.start();
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {

        MovieDao movieDao = mNewestApapter.getItem(position-1);
        Intent intent = new Intent(getActivity(), EmptyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("movie",movieDao);
        bundle.putInt("fragmentType",0);
        bundle.putString("movieType","synthesis");
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }
}