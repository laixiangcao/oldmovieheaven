package com.jiumeng.movieheaven.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.jiumeng.movieheaven.views.TimerButten;
import com.jiumeng.movieheaven.R;

/**
 * Created by Administrator on 2016/8/12 0012.
 */
public class SplashActivity extends Activity implements TimerButten.TimerCountdownOverListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //方法一：代码隐藏标题栏和状态栏
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        //getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);//隐藏状态栏
        //方法二：通过主题隐藏标题栏和状态栏 （效果最佳）android:theme="@android:style/Theme.NoTitleBar.Fullscreen"
        setContentView(R.layout.activity_splash);
        TimerButten timer_countdown = (TimerButten) findViewById(R.id.timer_countdown);
        timer_countdown.setTimerCountdownOverListener(this);
        timer_countdown.start();
    }

    @Override
    public void startActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
