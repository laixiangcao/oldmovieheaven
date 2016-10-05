package com.jiumeng.movieheaven.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiumeng.movieheaven.R;
import com.jiumeng.movieheaven.bean.MovieDao;
import com.jiumeng.movieheaven.utils.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class NewestViewPagerHolder extends BaseViewHolder<MovieDao> {

    private ImageView iv_image;
    private TextView tv_name, tv_type, tv_country, tv_time, tv_ibdm;
    private ImageLoader mImageLoader;

    @Override
    protected View initView() {
        mImageLoader = ImageLoader.getInstance();
        View view = UIUtils.inflate(R.layout.list_item_fragment_viewpager2);
        iv_image = (ImageView) view.findViewById(R.id.iv_image);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_type = (TextView) view.findViewById(R.id.tv_type);
        tv_country = (TextView) view.findViewById(R.id.tv_country);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_ibdm = (TextView) view.findViewById(R.id.tv_ibdm);
        return view;
    }

    @Override
    protected void refreshView(MovieDao data) {
        mImageLoader.displayImage(data.jpgList.get(0), iv_image, UIUtils.getDisplayImageOptions2());
        tv_name.setText(data.minName);
        tv_type.setText("类型：" + data.category);
        tv_country.setText("国家：" + data.country);
        tv_time.setText("更新时间：" + data.updatetime);
        if (data.grade != "") {
            tv_ibdm.setText("评分：" + data.grade);
        }
    }

}
