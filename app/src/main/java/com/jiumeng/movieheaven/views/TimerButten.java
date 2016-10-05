package com.jiumeng.movieheaven.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.jiumeng.movieheaven.R;

/**
 * Created by Administrator on 2016/8/12 0012.
 */
public class TimerButten extends Button implements Runnable {

    private int mCountdown;//倒计时时间
    private boolean isRun;//倒计时是否正在运行
    private TimerCountdownOverListener listener;

    public TimerButten(Context context) {
        super(context);
    }

    public TimerButten(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs!=null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimerButten);
            if (typedArray!=null){
                mCountdown = typedArray.getInt(R.styleable.TimerButten_countdown, 3000);
                mCountdown++;
            }
            typedArray.recycle();
        }
    }

    public TimerButten(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void countdown(){
        if (mCountdown==1){
            this.setText("倒计时："+(--mCountdown));
            stop();//停止倒计时
        }else {
            mCountdown--;
        }
    }

    @Override
    public void run() {
        if (isRun){
            countdown();
            this.setText("倒计时："+mCountdown);
            postDelayed(this,1000);
        }else {
            removeCallbacks(this);
        }
    }

    /**
     * 开始计时
     */
    public void start() {
        isRun = true;
        run();
    }

    /**
     * 结束计时
     */
    public void stop() {
        isRun = false;
        if (listener!=null){
            listener.startActivity();
        }
    }

    public interface TimerCountdownOverListener {
        void startActivity();
    }
    public void setTimerCountdownOverListener(TimerCountdownOverListener listener){
        this.listener=listener;
    }
}
