package com.jiumeng.movieheaven.holder;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.jiumeng.movieheaven.utils.UIUtils;
import com.jiumeng.movieheaven.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/24 0024.
 */
public class ListViewHeaderHolder extends BaseViewHolder<ArrayList<String>> {
    private ArrayList<String> data;
    private ViewPager mViewPager;
    private LinearLayout mLinearLayout;
    private int mPreviousPos;// 上个圆点位置

    @Override
    protected View initView() {
        //创建一个相对布局 用来放ViewPager 和 Indicator
        RelativeLayout rlLayout = new RelativeLayout(UIUtils.getContext());
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, UIUtils.dip2px(160));
        rlLayout.setLayoutParams(rlParams);//设置相对布局的宽高

        //创建一个viewPager
        mViewPager = new ViewPager(UIUtils.getContext());
        RelativeLayout.LayoutParams vpParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rlLayout.addView(mViewPager, vpParams);

        //创建一个线性布局用来放小圆点指示器
        mLinearLayout = new LinearLayout(UIUtils.getContext());
        RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        int padding = UIUtils.dip2px(8);
        mLinearLayout.setPadding(padding, padding, padding, padding);
        mLinearLayout.setGravity(Gravity.CENTER);
        //给线性布局添加规则
        llParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);//设置对齐父元素右边
        llParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//设置对齐父元素下边
        rlLayout.addView(mLinearLayout, llParams);

        return rlLayout;
    }

    @Override
    protected void refreshView(ArrayList<String> data) {
        this.data = data;
        mViewPager.setAdapter(new MyPagerAdapter());
        mViewPager.setCurrentItem(data.size() * 100000);//设置默认指示的位置
        for (int i = 0; i < data.size(); i++) {
            ImageView point = new ImageView(UIUtils.getContext());
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                point.setImageResource(R.drawable.point_green);
            } else {
                point.setImageResource(R.drawable.point_white);
                imageParams.leftMargin = UIUtils.dip2px(5);
            }
            mLinearLayout.addView(point, imageParams);
        }
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                position = position % ListViewHeaderHolder.this.data.size();
                ImageView point = (ImageView) mLinearLayout.getChildAt(position);
                point.setImageResource(R.drawable.point_green);
                ImageView lastPoint = (ImageView) mLinearLayout.getChildAt(mPreviousPos);
                lastPoint.setImageResource(R.drawable.point_white);
                mPreviousPos = position;
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        new HomeHeaderTask().start();//启动轮播条自动播放
    }

    class HomeHeaderTask implements Runnable {

        public void start() {
            Handler handler = UIUtils.getHandler();
            handler.removeCallbacksAndMessages(null);// 在开始启动自动轮播的时候  先清除之前的所发送的消息（防止页面切换的时候 被重复调用）
            handler.postDelayed(this, 3000);//发送延时3秒的任务
        }

        public void run() {
            int currentItem = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(++currentItem);
            UIUtils.getHandler().postDelayed(this, 3000); // 继续发延时3秒消息, 实现内循环
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % data.size();
            ImageView imageView = new ImageView(UIUtils.getContext());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.default_movie_image2);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoader.getInstance().displayImage(data.get(position), imageView, UIUtils.getDisplayImageOptions());
            container.addView(imageView);
            return imageView;
        }
    }
}
