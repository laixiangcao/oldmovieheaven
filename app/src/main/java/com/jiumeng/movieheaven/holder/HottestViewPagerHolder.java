package com.jiumeng.movieheaven.holder;

import android.view.View;
import android.widget.TextView;

import com.jiumeng.movieheaven.bean.MovieDao;
import com.jiumeng.movieheaven.utils.UIUtils;
import com.jiumeng.movieheaven.R;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class HottestViewPagerHolder extends BaseViewHolder<MovieDao> {

    private TextView tv_title,tv_subtitle,tv_type,tv_rating,tv_time;

    @Override
    protected View initView() {
        View view = UIUtils.inflate(R.layout.list_item_fragment_viewpager3);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_subtitle = (TextView) view.findViewById(R.id.tv_subtitle);
        tv_type = (TextView) view.findViewById(R.id.tv_type);
        tv_rating = (TextView) view.findViewById(R.id.tv_rating);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        return view;
    }

    @Override
    protected void refreshView(MovieDao data) {
        tv_title.setText(data.name);
        tv_subtitle.setText("片名："+data.minName);
        tv_type.setText("类型："+data.category);
        tv_rating.setText("评分："+data.grade);
        tv_time.setText(data.updatetime);
    }
}
