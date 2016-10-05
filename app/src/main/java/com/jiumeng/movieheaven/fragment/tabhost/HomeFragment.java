package com.jiumeng.movieheaven.fragment.tabhost;

import com.jiumeng.movieheaven.base.BaseViewPagerFragment;
import com.jiumeng.movieheaven.fragment.vpFragment.ClassicsVpFragment;
import com.jiumeng.movieheaven.fragment.vpFragment.HottestVpFragment;
import com.jiumeng.movieheaven.fragment.vpFragment.NewestVpFragment;
import com.jiumeng.movieheaven.fragment.vpFragment.SynthesisVpFragment;
import com.jiumeng.movieheaven.utils.UIUtils;
import com.jiumeng.movieheaven.views.itemdrag.ViewPageInfo;
import com.jiumeng.movieheaven.R;

import java.util.ArrayList;

/**
 * 综合电影页面 包含 综合 - 最新 - 最热 - 经典
 * Created by jiumeng on 2016/7/14 0014.
 */
public class HomeFragment extends BaseViewPagerFragment {

    @Override
    protected ArrayList<ViewPageInfo> onSetupTabAdapter() {
        String[] tabTitles = UIUtils.getStringArray(R.array.indicator_names);
        ArrayList<ViewPageInfo> viewPageInfos = new ArrayList<>();
        viewPageInfos.add(new ViewPageInfo(tabTitles[0],"synthesis", SynthesisVpFragment.class,null));
        viewPageInfos.add(new ViewPageInfo(tabTitles[1],"newest", NewestVpFragment.class,null));
        viewPageInfos.add(new ViewPageInfo(tabTitles[2],"hotest", HottestVpFragment.class,null));
        viewPageInfos.add(new ViewPageInfo(tabTitles[3],"classics", ClassicsVpFragment.class,null));
        return viewPageInfos;
    }

    protected void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(1);
    }
}
