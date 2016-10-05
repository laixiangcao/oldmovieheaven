package com.jiumeng.movieheaven.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.jiumeng.movieheaven.holder.BaseViewHolder;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public abstract class ListBaseAdapter<T> extends BaseAdapter {

    private ArrayList<T> data;

    public ListBaseAdapter(ArrayList<T> data) {
        this.data = data;
    }

    public ArrayList<T> getData() {
        return data;
    }

    /**
     * 用于下拉刷新数据时 往集合中添加新数据的方法
     */
    public void addRefreshData(ArrayList<T> newData) {
        data.addAll(0, newData);
        notifyDataSetChanged();
    }
    /**
     * 用于上拉加载更多数据时 往集合中添加新数据的方法
     */
    public void addMoreData(ArrayList<T> newData) {
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();//添加一个加载更多的Item
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder;
        if (convertView == null) {

            //1.加载布局 2.初始化控件 findViewById() 3.打标记
            holder = getViewHolder();

        } else {
            holder = (BaseViewHolder) convertView.getTag();
        }
        //根据数据刷新界面
        holder.setData(getItem(position));

        return holder.getRootView();
    }

    protected abstract BaseViewHolder<T> getViewHolder();

}



