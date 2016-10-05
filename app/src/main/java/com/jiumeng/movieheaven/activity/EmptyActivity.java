package com.jiumeng.movieheaven.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jiumeng.movieheaven.bean.MovieDao;
import com.jiumeng.movieheaven.fragment.otherfragment.MovieDetailsFragment;
import com.jiumeng.movieheaven.fragment.otherfragment.SearchFragment;
import com.jiumeng.movieheaven.fragment.otherfragment.SettingFragment;
import com.jiumeng.movieheaven.R;


/**
 * Created by Administrator on 2016/7/23 0023.
 */
public class EmptyActivity extends AppCompatActivity {

    private MovieDao mMovie;
    private int mFragmentType;
    private FragmentTransaction mFt;
    private String mMovieType;
    public static final int FRAGMENTTYPE_MOVIEDETAILS = 0;
    public static final int FRAGMENTTYPE_SETTING = 1;
    public static final int FRAGMENTTYPE_MY_MESSAGE = 2;
    public static final int FRAGMENTTYPE_MY_FAVORITES = 3;
    public static final int FRAGMENTTYPE_FEEDBACK = 4;
    public static final int FRAGMENTTYPE_RECOMMEND = 5;
    public static final int FRAGMENTTYPE_SEARCH = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_fragment_viewpager);
        mMovie = (MovieDao) getIntent().getSerializableExtra("movie");
        mFragmentType = getIntent().getIntExtra("fragmentType", -1);
        mMovieType = getIntent().getStringExtra("movieType");
        initActionBar();
        setDefaultFragment();
    }

    private void setDefaultFragment() {
        mFt = getSupportFragmentManager().beginTransaction();
        mFt.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        Bundle bundle;
        switch (mFragmentType) {
            case FRAGMENTTYPE_MOVIEDETAILS:
                MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
                bundle = new Bundle();
                bundle.putSerializable("movie", mMovie);
                bundle.putString("movieType", mMovieType);
                movieDetailsFragment.setArguments(bundle);
                mFt.replace(R.id.fl_content, movieDetailsFragment, "movieDetailsFragment");
                break;
            case FRAGMENTTYPE_SETTING:
                SettingFragment mSettingFragment = new SettingFragment();
                mFt.replace(R.id.fl_content, mSettingFragment, "settingFragment");
                break;
            case FRAGMENTTYPE_SEARCH:
                SearchFragment searchFragment = new SearchFragment();
                mFt.replace(R.id.fl_content, searchFragment, "searchFragment");
                break;
        }

        mFt.commit();

    }

    public void addFragment(Fragment fragment, String tag) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.add(R.id.fl_content, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void initActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setElevation(0);
            if (mFragmentType == 0) {
                actionBar.setTitle(mMovie.minName);
            } else if (mFragmentType == 1) {
                actionBar.setTitle("设置中心");
            } else if (mFragmentType == 2) {
                actionBar.setTitle("消息通知");
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.search:
                Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
