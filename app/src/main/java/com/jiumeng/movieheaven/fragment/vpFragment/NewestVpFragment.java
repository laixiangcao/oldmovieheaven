package com.jiumeng.movieheaven.fragment.vpFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.jiumeng.movieheaven.adapter.AmericaTvApapter;
import com.jiumeng.movieheaven.adapter.ListBaseAdapter;
import com.jiumeng.movieheaven.bean.MovieDao;
import com.jiumeng.movieheaven.network.NetWorkApi;
import com.jiumeng.movieheaven.parsedata.ParseData;
import com.jiumeng.movieheaven.activity.EmptyActivity;
import com.jiumeng.movieheaven.base.BaseListFragment;
import com.jiumeng.movieheaven.utils.PrefUtils;
import com.jiumeng.movieheaven.utils.UIUtils;
import com.jiumeng.movieheaven.views.LoadingPage;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class NewestVpFragment extends BaseListFragment<MovieDao> {

    private ArrayList<MovieDao> moviedata;
    private int pages = 500;
    private int currentPage = 1;
    private AmericaTvApapter mAmericaTvApapter;

    @Override
    protected ListBaseAdapter<MovieDao> getListAdapter() {
        mAmericaTvApapter = new AmericaTvApapter(moviedata);
        return mAmericaTvApapter;
    }

    @Override
    protected void initData() {

        moviedata = (ArrayList<MovieDao>) PrefUtils.readObject("AmericaTvMovieCache");
        if (moviedata != null) {
            loadDataComplete(check(moviedata));
        } else {
            initNetData();
        }
    }

    protected void initNetData() {
        NetWorkApi.getMovieList(NetWorkApi.MVOIETYPE_NEWEST, 1, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String content = null;
                try {
                    content = new String(responseBody, "GBK");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                pages = ParseData.getCategoryPages(content);
                moviedata = ParseData.parseNewAndHotMovie(content);
                if (moviedata.size() != 0 && moviedata != null) {
                    PrefUtils.saveObject("AmericaTvMovieCache", moviedata);
                }
                loadDataComplete(check(moviedata));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
                loadDataComplete(LoadingPage.ResultState.STATE_ERROR);
            }
        });
    }

    @Override
    protected void requestLoadMoreData() {
        if (currentPage > pages) {
            setFooterType(TYPE_NO_MORE);//没有更多数据
            return;
        }
        NetWorkApi.getMovieList(NetWorkApi.MVOIETYPE_NEWEST, ++currentPage, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String content = null;
                try {
                    content = new String(responseBody, "GBK");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ArrayList<MovieDao> moreData = ParseData.parseNewAndHotMovie(content);
                if (moreData != null) {
                    if (moreData.size() == 0) {
                        setFooterType(TYPE_NO_MORE);//没有更多数据
                    } else if (moreData.size() > 0) {
                        mAmericaTvApapter.addMoreData(moreData);
                        setFooterType(TYPE_NORMAL);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                if (UIUtils.getNetworkType() == 0) {
                    setFooterType(TYPE_NET_ERROR);//没有网络
                } else {
                    setFooterType(TYPE_ERROR);//加载失败
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {

        MovieDao movieDao = mAmericaTvApapter.getItem(position);
        Intent intent = new Intent(getActivity(), EmptyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("movie", movieDao);
        bundle.putInt("fragmentType", 0);
        bundle.putString("movieType", "newest");
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    protected void requestRefreshData() {
        NetWorkApi.getMovieList(NetWorkApi.MVOIETYPE_NEWEST, 1, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String content = null;
                try {
                    content = new String(responseBody, "GBK");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                pages = ParseData.getCategoryPages(content);
                ArrayList<MovieDao> refreshDataList = ParseData.parseNewAndHotMovie(content);

                if (refreshDataList.size() != 0 && refreshDataList != null) {
                    PrefUtils.saveObject("AmericaTvMovieCache", refreshDataList);
                    refreshDataList = checkRefreshData(moviedata, refreshDataList);
                    mAmericaTvApapter.addRefreshData(refreshDataList);
                }
                showUpdateResult(refreshDataList.size());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

}
