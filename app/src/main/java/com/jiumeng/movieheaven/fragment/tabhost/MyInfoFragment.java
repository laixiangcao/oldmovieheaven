package com.jiumeng.movieheaven.fragment.tabhost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jiumeng.movieheaven.activity.EmptyActivity;
import com.jiumeng.movieheaven.R;

/**
 * Created by Administrator on 2016/7/14 0014.
 */
public class MyInfoFragment extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_my_information, null);

        LinearLayout my_message = (LinearLayout) mRootView.findViewById(R.id.my_message);
        LinearLayout my_favorites = (LinearLayout) mRootView.findViewById(R.id.my_favorites);
        LinearLayout recommend = (LinearLayout) mRootView.findViewById(R.id.recommend);
        LinearLayout feedback = (LinearLayout) mRootView.findViewById(R.id.feedback);
        LinearLayout isloadimage = (LinearLayout) mRootView.findViewById(R.id.isloadimage);
        LinearLayout night_mode = (LinearLayout) mRootView.findViewById(R.id.night_mode);
        LinearLayout ll_setting = (LinearLayout) mRootView.findViewById(R.id.ll_setting);
        ll_setting.setOnClickListener(this);
        my_message.setOnClickListener(this);
        my_favorites.setOnClickListener(this);
        recommend.setOnClickListener(this);
        feedback.setOnClickListener(this);
        isloadimage.setOnClickListener(this);
        night_mode.setOnClickListener(this);
        return mRootView;

    }

    private Intent mIntent;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_setting:
                mIntent = new Intent(getActivity(), EmptyActivity.class);
                mIntent.putExtra("fragmentType",EmptyActivity.FRAGMENTTYPE_SETTING);
                getActivity().startActivity(mIntent);
                break;
            case R.id.my_message:
                mIntent = new Intent(getActivity(), EmptyActivity.class);
                mIntent.putExtra("fragmentType",EmptyActivity.FRAGMENTTYPE_MY_MESSAGE);
                getActivity().startActivity(mIntent);
                break;
            case R.id.my_favorites:
                mIntent = new Intent(getActivity(), EmptyActivity.class);
                mIntent.putExtra("fragmentType",EmptyActivity.FRAGMENTTYPE_MY_FAVORITES);
                getActivity().startActivity(mIntent);
                break;
            case R.id.recommend:
                mIntent = new Intent(getActivity(), EmptyActivity.class);
                mIntent.putExtra("fragmentType",EmptyActivity.FRAGMENTTYPE_RECOMMEND);
                getActivity().startActivity(mIntent);
                break;
            case R.id.feedback:
                mIntent = new Intent(getActivity(), EmptyActivity.class);
                mIntent.putExtra("fragmentType",EmptyActivity.FRAGMENTTYPE_FEEDBACK);
                getActivity().startActivity(mIntent);
                break;
            case R.id.isloadimage:

                break;
            case R.id.night_mode:

                break;
        }
    }
}
