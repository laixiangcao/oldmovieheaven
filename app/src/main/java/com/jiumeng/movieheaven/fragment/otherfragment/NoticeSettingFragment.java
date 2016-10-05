package com.jiumeng.movieheaven.fragment.otherfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiumeng.movieheaven.views.ToggleButton;

import com.jiumeng.movieheaven.R;

/**
 * Created by Administrator on 2016/8/4 0004.
 */
public class NoticeSettingFragment extends Fragment {

    private View mRootView;
    private ToggleButton tb_accept, tb_voice, tb_vibration,tb_app_exit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_settings_notifcation, null);
        initUI();
        return mRootView;
    }

    private void initUI() {
        tb_accept = (ToggleButton) mRootView.findViewById(R.id.tb_accept);
        tb_voice = (ToggleButton) mRootView.findViewById(R.id.tb_voice);
        tb_vibration = (ToggleButton) mRootView.findViewById(R.id.tb_vibration);
        tb_app_exit = (ToggleButton) mRootView.findViewById(R.id.tb_app_exit);
        tb_accept.toggleOff();
    }

}
