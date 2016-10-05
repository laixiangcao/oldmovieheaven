package com.jiumeng.movieheaven.fragment.otherfragment;

import android.view.LayoutInflater;
import android.view.View;

import com.jiumeng.movieheaven.base.BaseFragment;
import com.jiumeng.movieheaven.views.LoadingPage;
import com.jiumeng.movieheaven.R;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class SearchFragment extends BaseFragment {
    @Override
    public void requestNetData() {
        loadDataComplete(LoadingPage.ResultState.STATE_SUCCESS);
    }

    @Override
    public View onCreateSuccessView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_search,null);
    }
}
