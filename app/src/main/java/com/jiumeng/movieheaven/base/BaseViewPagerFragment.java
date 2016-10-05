package com.jiumeng.movieheaven.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.jiumeng.movieheaven.adapter.ViewPageFragmentAdapter;
import com.jiumeng.movieheaven.utils.UIUtils;
import com.jiumeng.movieheaven.views.itemdrag.ViewPageInfo;
import com.jiumeng.movieheaven.R;

import java.util.ArrayList;

/**
 * 带有导航条的基类
 * Created by Administrator on 2016/7/15 0015.
 */
public abstract class BaseViewPagerFragment extends Fragment implements View.OnClickListener{
    protected ViewPager mViewPager;
    protected PagerSlidingTabStrip mTabStrip;
    protected View mRoot;
    protected ViewPageFragmentAdapter mTabsAdapter;
    protected RecyclerView mRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRoot == null) {
            mRoot = inflater.inflate(R.layout.base_viewpage_fragment, null);
            mTabStrip = (PagerSlidingTabStrip) mRoot.findViewById(R.id.pager_tabstrip);
            mViewPager = (ViewPager) mRoot.findViewById(R.id.pager);
            ImageView iv_edit = (ImageView) mRoot.findViewById(R.id.iv_edit);
            mRecyclerView = (RecyclerView) mRoot.findViewById(R.id.recy);

            mRecyclerView.setVisibility(View.GONE);
            mTabStrip.setDividerColor(android.R.color.transparent);
            mTabStrip.setTextColor(getResources().getColor(R.color.viewpage_selector_slide_title));
            mTabStrip.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,14,getResources().getDisplayMetrics()));

            iv_edit.setOnClickListener(this);

            mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(), UIUtils.getContext(), onSetupTabAdapter());
            mViewPager.setAdapter(mTabsAdapter);
            mTabStrip.setViewPager(mViewPager);
            setScreenPageLimit();
        }

        return mRoot;
    }

    /**
     * 添加tab标签的
     */
    protected abstract ArrayList<ViewPageInfo> onSetupTabAdapter();


    /**
     * 子类覆写该方法，实现设置缓存tabsub个数
     */
    protected void setScreenPageLimit() {
    }

    @Override
    public void onClick(View v) {

    }
}
