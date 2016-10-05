package com.jiumeng.movieheaven.bean;


import com.jiumeng.movieheaven.fragment.tabhost.HomeFragment;
import com.jiumeng.movieheaven.fragment.tabhost.MyInfoFragment;
import com.jiumeng.movieheaven.R;
import com.jiumeng.movieheaven.fragment.tabhost.CinemaFragment;
import com.jiumeng.movieheaven.fragment.tabhost.ClassifyFragment;

public enum MainTab {

    NEWS(0, R.string.main_tab__name_home, R.drawable.tab_icon_new, HomeFragment.class),

    TWEET(1, R.string.main_tab__name_share, R.drawable.tab_icon_tweet, ClassifyFragment.class),

    EXPLORE(2, R.string.main_tab_name_discuss, R.drawable.tab_icon_explore, CinemaFragment.class),

    ME(3, R.string.main_tab_name_my, R.drawable.tab_icon_me, MyInfoFragment.class);

    private int idx;
    private int resName;
    private int resIcon;
    private Class<?> clz;

    private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
        this.idx = idx;
        this.resName = resName;
        this.resIcon = resIcon;
        this.clz = clz;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getResName() {
        return resName;
    }

    public void setResName(int resName) {
        this.resName = resName;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
}
