package com.jiumeng.movieheaven.adapter;

import com.jiumeng.movieheaven.bean.MovieDao;
import com.jiumeng.movieheaven.holder.AmericaTvPagerHolder;
import com.jiumeng.movieheaven.holder.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class AmericaTvApapter extends ListBaseAdapter<MovieDao> {

    public AmericaTvApapter(ArrayList<MovieDao> data) {
        super(data);
    }
    @Override
    protected BaseViewHolder<MovieDao> getViewHolder() {
        return new AmericaTvPagerHolder();
    }
}
