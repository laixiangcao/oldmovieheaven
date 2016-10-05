package com.jiumeng.movieheaven.base;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jiumeng.movieheaven.adapter.ListBaseAdapter;
import com.jiumeng.movieheaven.bean.MovieDao;
import com.jiumeng.movieheaven.utils.UIUtils;
import com.jiumeng.movieheaven.views.LoadingPage;
import com.jiumeng.movieheaven.views.SuperRefreshLayout;
import com.jiumeng.movieheaven.R;
import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public abstract class BaseListFragment<T> extends BaseFragment implements SuperRefreshLayout.SuperRefreshLayoutListener,
        AdapterView.OnItemClickListener, View.OnClickListener{

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_LOADING = 1;
    public static final int TYPE_NO_MORE = 2;
    public static final int TYPE_ERROR = 3;
    public static final int TYPE_NET_ERROR = 4;
    private SuperRefreshLayout mSwipeRefreshLayout;
    public ListView mListView;
    private TextView mRefreshText;
    private ProgressBar mFooterProgressBar;
    private TextView mFooterText;
    private FloatingActionButton mFab;


    @Override
    public View onCreateSuccessView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.fragment_pull_refresh_listview, null);
        mSwipeRefreshLayout = (SuperRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
        mRefreshText = (TextView) view.findViewById(R.id.tv_updata_state);
        mListView = (ListView) view.findViewById(R.id.listview);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);

        mSwipeRefreshLayout.setSuperRefreshLayoutListener(this);
        ListBaseAdapter<T> mAdapter =  getListAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, false, new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Toast.makeText(UIUtils.getContext(),scrollState+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        }));

        initHeaderView();
        initFooterView();
        initSwipeRefresh();
        initFab();

        return view;
    }

    protected void initFab() {
        mFab.setOnClickListener(this);
        mFab.attachToListView(mListView);

    }

    @Override
    public void onClick(View v) {
        mListView.smoothScrollToPosition(0);
    }


    public void onRefresh() {
        requestRefreshData();
        mSwipeRefreshLayout.onLoadComplete();
    }

    @Override
    public void onLoadMore() {
        setFooterType(TYPE_LOADING);
        UIUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        requestLoadMoreData();
                        mSwipeRefreshLayout.onLoadComplete();
                    }
                });
            }
        },100);

    }

    /**
     * 请求加载更多数据
     */
    protected void requestLoadMoreData() {
    }


    /**
     * 运行在主线程 请求刷新数据 由子类实现
     */
    protected void requestRefreshData() {
    }


    @Override
    protected void loadDataComplete(LoadingPage.ResultState resultState) {
        super.loadDataComplete(resultState);
    }

    /**
     * 获取适配器
     * @return
     */
    protected abstract ListBaseAdapter<T> getListAdapter();

    /**
     * 请求网络数据
     */
    protected abstract void initData();

    /**
     * listview 条目的点击事件  具体由子类实现
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    /**
     * 通过传入初始数据与刷新得到的数据 剔除共有的部分  返回刷新之后的数据
     * @param rawData  第一次加载的数据（或缓存的数据）集合
     * @param refreshData 刷新得到的数据集合
     * @return 效验后 刷新的数据
     */
    protected ArrayList<MovieDao> checkRefreshData(ArrayList<MovieDao> rawData, ArrayList<MovieDao> refreshData){
        ArrayList<MovieDao> checkRefreshData = new ArrayList<>();
        ArrayList<String> idList = new ArrayList<>();

        for (MovieDao movieDao : rawData) {
            idList.add(movieDao.id);
        }
        for (MovieDao movieDao : refreshData) {
            if (!idList.contains(movieDao.id)){
                checkRefreshData.add(movieDao);
            }
        }
        return checkRefreshData;
    }

    @Override
    public void requestNetData() {
        initData();
    }

    //=================================start 脚布局相关==========================================
    private void initFooterView() {
        if (isNeedFooter()) {
            mSwipeRefreshLayout.setCanLoadMore();//允许加载更多
            View mFooterView = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.list_item_loadmore_footview, null);
            mFooterProgressBar = (ProgressBar) mFooterView.findViewById(R.id.pb_progress);
            mFooterText = (TextView) mFooterView.findViewById(R.id.text);
            mListView.addFooterView(mFooterView);
        }
    }

    protected boolean isNeedFooter() {
        return true;
    }

    protected void setFooterType(int type) {
        switch (type) {
            case TYPE_NORMAL:
            case TYPE_LOADING:
                mFooterText.setText("正在加载中...");
                mFooterProgressBar.setVisibility(View.VISIBLE);
                break;
            case TYPE_NET_ERROR:
                mFooterText.setText("网络错误");
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_ERROR:
                mFooterText.setText("加载失败");
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_NO_MORE:
                mFooterText.setText("没有更多数据");
                mFooterProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setNoMoreData();
                break;
        }
    }

    //=================================end 脚布局相关==========================================


//==============================start 头布局相关=====================================================

    /**
     * 初始化头布局，添加可以滑动的图片界面
     */
    private void initHeaderView() {
        if (isNeedHeader()) {
            mListView.addHeaderView(setHeaderView());
        }
    }

    /**
     * 添加头布局View 由子类覆写该方法
     */
    protected View setHeaderView() {
        return null;
    }

    /**
     * 是否需要一个头布局
     *
     * @return 默认返回false
     */
    protected boolean isNeedHeader() {
        return false;
    }
    //===============================end 头布局相关================================================


    //===============================start 下拉刷新相关============================================

    /**
     * 初始化下拉刷新
     */
    private void initSwipeRefresh() {
        if (isNeedRefreshView()) {
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                    R.color.swiperefresh_color3);//设置进度动画的颜色。
        }else {
            mSwipeRefreshLayout.setEnabled(false);
        }
    }



    /**
     * 显示刷新结果
     * @param i 更新的数量
     */
    protected void showUpdateResult(int i) {
        mRefreshText.setVisibility(View.VISIBLE);
        if (i == 0) {
            mRefreshText.setText("暂无更新");
        } else {
            mRefreshText.setText("成功为您更新了" + i + "部电影");
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mRefreshText, "translation", 0f, 1f);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                mRefreshText.setVisibility(View.GONE);
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * 是否需要刷新控件
     *
     * @return 默认返回true
     */
    protected boolean isNeedRefreshView() {
        return true;
    }

    /**
     * 是否需要自动下拉刷新
     */
    protected boolean isNeedAutoRefresh() {
        return true;
    }

    /**
     * 设置自动下拉刷新时间
     *
     * @return 默认自动刷新时间为5小时
     */
    protected long setAutoRefreshTime() {
        return 5 * 60 * 60;
    }

    /**
     * 是否到了下拉刷新时间
     */
    protected boolean onTimeRefresh() {
        return false;
    }

    //===============================end 下拉刷新相关============================================
}
