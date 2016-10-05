package com.jiumeng.movieheaven.fragment.vpFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.jiumeng.movieheaven.activity.EmptyActivity;
import com.jiumeng.movieheaven.adapter.ClassicsApapter;
import com.jiumeng.movieheaven.adapter.ListBaseAdapter;
import com.jiumeng.movieheaven.base.BaseListFragment;
import com.jiumeng.movieheaven.bean.MovieDao;
import com.jiumeng.movieheaven.utils.PrefUtils;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class ClassicsVpFragment extends BaseListFragment<MovieDao> {

    private ArrayList<MovieDao> moviedata;
    private int page = 1;
    private ClassicsApapter mClassicsApapter;

    @Override
    protected void initData() {
        if (PrefUtils.readObject("classice_0.object")==null){
//            new Thread(){
//                @Override
//                public void run() {
//                    try {
//                        ClassicsParseService.getMovieList();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
            PrefUtils.moveFile();
        }else{
            moviedata = (ArrayList<MovieDao>) PrefUtils.readObject("classice_0.object");
        }
        loadDataComplete(check(moviedata));
    }

    @Override
    protected ListBaseAdapter<MovieDao> getListAdapter() {
        mClassicsApapter = new ClassicsApapter(moviedata);
        return mClassicsApapter;
    }


    @Override
    protected void requestLoadMoreData() {
        if (page > 10) {
            setFooterType(TYPE_NO_MORE);//没有更多数据
            return;
        }
        ArrayList<MovieDao> moreData = (ArrayList<MovieDao>) PrefUtils.readObject("classice_" + (page++) + ".object");
        if (moreData == null) {
            setFooterType(TYPE_ERROR);
        } else {
            if (moreData.size() == 0) {
                setFooterType(TYPE_NO_MORE);
            } else {
                mClassicsApapter.addMoreData(moreData);
                setFooterType(TYPE_NORMAL);
            }
        }
    }

    @Override
    protected boolean isNeedRefreshView() {
        return false;
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {

        MovieDao movieDao = mClassicsApapter.getItem(position);
        Intent intent = new Intent(getActivity(), EmptyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("movie",movieDao);
        bundle.putInt("fragmentType",0);
        bundle.putString("movieType","classics");
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }
}
