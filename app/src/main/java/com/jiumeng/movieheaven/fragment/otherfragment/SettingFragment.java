package com.jiumeng.movieheaven.fragment.otherfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jiumeng.movieheaven.activity.EmptyActivity;
import com.jiumeng.movieheaven.utils.PrefUtils;
import com.jiumeng.movieheaven.views.ToggleButton;
import com.jiumeng.movieheaven.R;

/**
 * Created by Administrator on 2016/8/4 0004.
 */
public class SettingFragment extends Fragment implements View.OnClickListener{

    private View mRootView;
    private RelativeLayout mNotify, mClearCache, mAbout, mQuit;
    private ToggleButton mDblclickQuit, mDetectionUpdate, mLoadPicture;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_settings, null);
        initUI();
        return mRootView;
    }

    private void initUI() {
        mNotify = (RelativeLayout) mRootView.findViewById(R.id.rl_notification_settings);
        mClearCache = (RelativeLayout) mRootView.findViewById(R.id.rl_clean_cache);
        mAbout = (RelativeLayout) mRootView.findViewById(R.id.rl_about);
        mQuit = (RelativeLayout) mRootView.findViewById(R.id.rl_exit);
        mNotify.setOnClickListener(this);
        mClearCache.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mQuit.setOnClickListener(this);

        mDblclickQuit = (ToggleButton) mRootView.findViewById(R.id.tb_dblclick_quit);
        mDetectionUpdate = (ToggleButton) mRootView.findViewById(R.id.tb_detection_update);
        mLoadPicture = (ToggleButton) mRootView.findViewById(R.id.tb_loading_img);

        if (PrefUtils.getBoolean("isDblclickQuit")){
            mDblclickQuit.setToggleOn();
        }else {
            mDblclickQuit.setToggleOff();
        }
        if (PrefUtils.getBoolean("isDetectionUpdate")){
            mDetectionUpdate.setToggleOn();
        }else {
            mDetectionUpdate.setToggleOff();
        }
        if (PrefUtils.getBoolean("isLoadPicture")){
            mLoadPicture.setToggleOn();
        }else {
            mLoadPicture.setToggleOff();
        }

        mLoadPicture.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                PrefUtils.putBoolean("isLoadPicture",on);
            }
        });
        mDetectionUpdate.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                PrefUtils.putBoolean("isDetectionUpdate",on);
            }
        });
        mDblclickQuit.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                PrefUtils.putBoolean("isDblclickQuit",on);
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_notification_settings:
                EmptyActivity activity = (EmptyActivity) getActivity();
                activity.addFragment(new NoticeSettingFragment(),"noticeSettingFragment");
                break;
            case R.id.rl_clean_cache:
                Toast.makeText(getContext(), "清理缓存", 0).show();
                break;
            case R.id.rl_about:
                Toast.makeText(getContext(), "关于", 0).show();
                break;
            case R.id.rl_exit:
                Toast.makeText(getContext(), "退出", 0).show();
                break;
        }
    }
}
