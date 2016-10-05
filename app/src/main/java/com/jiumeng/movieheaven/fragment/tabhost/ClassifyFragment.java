package com.jiumeng.movieheaven.fragment.tabhost;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.jiumeng.movieheaven.base.BaseViewPagerFragment;
import com.jiumeng.movieheaven.fragment.vpFragment.ClassifyVpFragment;
import com.jiumeng.movieheaven.network.NetWorkApi;
import com.jiumeng.movieheaven.utils.PrefUtils;
import com.jiumeng.movieheaven.utils.UIUtils;
import com.jiumeng.movieheaven.views.itemdrag.ChannelAdapter;
import com.jiumeng.movieheaven.views.itemdrag.ItemDragHelperCallback;
import com.jiumeng.movieheaven.views.itemdrag.OnCompleteEditListener;
import com.jiumeng.movieheaven.views.itemdrag.ViewPageInfo;

import java.util.ArrayList;


/**
 * 分类电影页面
 * Created by Administrator on 2016/7/14 0014.
 */
public class ClassifyFragment extends BaseViewPagerFragment {


    @Override
    protected ArrayList<ViewPageInfo> onSetupTabAdapter() {

        initDropTab();
        ArrayList<ViewPageInfo> defaultTab = (ArrayList<ViewPageInfo>) PrefUtils.readObject("defaultTab");
        if (defaultTab!=null){
            return defaultTab;
        }
        ArrayList<ViewPageInfo> viewPageInfos = new ArrayList<>();
        viewPageInfos.add(new ViewPageInfo("动作","action", ClassifyVpFragment.class,getBundle(NetWorkApi.MVOIETYPE_ACTION)));
        viewPageInfos.add(new ViewPageInfo("恐怖","terror", ClassifyVpFragment.class,getBundle(NetWorkApi.MVOIETYPE_TERROR)));
        viewPageInfos.add(new ViewPageInfo("科幻","science_fiction", ClassifyVpFragment.class,getBundle(NetWorkApi.MVOIETYPE_SCIENCE_FICTION)));
        viewPageInfos.add(new ViewPageInfo("喜剧","comedy", ClassifyVpFragment.class,getBundle(NetWorkApi.MVOIETYPE_COMEDY)));
        viewPageInfos.add(new ViewPageInfo("剧情","scenario", ClassifyVpFragment.class,getBundle(NetWorkApi.MVOIETYPE_SCENARIO)));
        viewPageInfos.add(new ViewPageInfo("爱情","affection", ClassifyVpFragment.class,getBundle(NetWorkApi.MVOIETYPE_AFFECTION)));

        return viewPageInfos;
    }

    private Bundle getBundle(int type){
        Bundle bundle = new Bundle();
        bundle.putSerializable("mvoietype",type);
//        bundle.putInt("mvoietype",type);
        return bundle;
    }

    @Override
    public void onClick(View v) {
        mRecyclerView.setVisibility(View.VISIBLE);
//        ValueAnimator animator;
//        if (mLayoutParams.height==0){
//            animator = ValueAnimator.ofInt(0,1000);
//        }else {
//            animator = ValueAnimator.ofInt(1000,0);
//        }
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int value = (int) animation.getAnimatedValue();
//                mLayoutParams.height=value;
//                mRecyclerView.setLayoutParams(mLayoutParams);
//            }
//        });
//        animator.setInterpolator(new AccelerateInterpolator());
//        animator.setDuration(300);
//        animator.start();
    }

    private void initDropTab() {

//        mMView = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.activity_demo, null);
//        recyclerView = (RecyclerView) mMView.findViewById(R.id.recy);
        ArrayList<ViewPageInfo> items = new ArrayList<>();
        ArrayList<ViewPageInfo> defaultTab = (ArrayList<ViewPageInfo>) PrefUtils.readObject("defaultTab");
        if (defaultTab!=null){
            items=defaultTab;
        }else {
            ViewPageInfo viewPageInfo = new ViewPageInfo("动作", "action", ClassifyVpFragment.class, getBundle(NetWorkApi.MVOIETYPE_ACTION));
            ViewPageInfo viewPageInfo1 = new ViewPageInfo("恐怖", "terror", ClassifyVpFragment.class, getBundle(NetWorkApi.MVOIETYPE_TERROR));
            ViewPageInfo viewPageInfo2 = new ViewPageInfo("科幻", "science_fiction", ClassifyVpFragment.class, getBundle(NetWorkApi.MVOIETYPE_SCIENCE_FICTION));
            ViewPageInfo viewPageInfo3 = new ViewPageInfo("喜剧", "comedy", ClassifyVpFragment.class, getBundle(NetWorkApi.MVOIETYPE_COMEDY));
            ViewPageInfo viewPageInfo4 = new ViewPageInfo("剧情", "scenario", ClassifyVpFragment.class, getBundle(NetWorkApi.MVOIETYPE_SCENARIO));
            ViewPageInfo viewPageInfo5 = new ViewPageInfo("爱情", "affection", ClassifyVpFragment.class, getBundle(NetWorkApi.MVOIETYPE_AFFECTION));
            viewPageInfo.setTitle("动作");
            viewPageInfo1.setTitle("恐怖");
            viewPageInfo2.setTitle("科幻");
            viewPageInfo3.setTitle("喜剧");
            viewPageInfo4.setTitle("剧情");
            viewPageInfo5.setTitle("爱情");
            items.add(viewPageInfo);
            items.add(viewPageInfo1);
            items.add(viewPageInfo2);
            items.add(viewPageInfo3);
            items.add(viewPageInfo4);
            items.add(viewPageInfo5);
        }

        ArrayList<ViewPageInfo> otherItems = new ArrayList<>();
        ArrayList<ViewPageInfo> otherTab = (ArrayList<ViewPageInfo>) PrefUtils.readObject("otherTab");
        if (otherTab!=null){
            otherItems=otherTab;
        }else {
            ViewPageInfo viewPageInfo6 = new ViewPageInfo("动画", "animation", ClassifyVpFragment.class, getBundle(NetWorkApi.MVOIETYPE_ANIMATION));
            ViewPageInfo viewPageInfo7 = new ViewPageInfo("悬疑", "suspense", ClassifyVpFragment.class, getBundle(NetWorkApi.MVOIETYPE_SUSPENSE));
            ViewPageInfo viewPageInfo8 = new ViewPageInfo("惊悚", "panic", ClassifyVpFragment.class, getBundle(NetWorkApi.MVOIETYPE_PANIC));
            ViewPageInfo viewPageInfo9 = new ViewPageInfo("战争", "war", ClassifyVpFragment.class, getBundle(NetWorkApi.MVOIETYPE_WAR));
            ViewPageInfo viewPageInfo10 = new ViewPageInfo("犯罪", "crime", ClassifyVpFragment.class, getBundle(NetWorkApi.MVOIETYPE_CRIME));

            viewPageInfo6.setTitle("动画");
            viewPageInfo7.setTitle("悬疑");
            viewPageInfo8.setTitle("惊悚");
            viewPageInfo9.setTitle("战争");
            viewPageInfo10.setTitle("犯罪");
            otherItems.add(viewPageInfo6);
            otherItems.add(viewPageInfo7);
            otherItems.add(viewPageInfo8);
            otherItems.add(viewPageInfo9);
            otherItems.add(viewPageInfo10);
        }


        GridLayoutManager manager = new GridLayoutManager(UIUtils.getContext(), 4);
        mRecyclerView.setLayoutManager(manager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        final ChannelAdapter adapter = new ChannelAdapter(UIUtils.getContext(), helper, items, otherItems);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                return viewType == ChannelAdapter.TYPE_MY || viewType == ChannelAdapter.TYPE_OTHER ? 1 : 4;
            }
        });
        mRecyclerView.setAdapter(adapter);

        adapter.setOnMyChannelItemClickListener(new ChannelAdapter.OnMyChannelItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
//                Toast.makeText(UIUtils.getContext(), items.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnCompleteEditListener(new OnCompleteEditListener() {
            @Override
            public void completeEdit(ArrayList<ArrayList<ViewPageInfo>> channelData) {
                mTabsAdapter.updateFragment(channelData.get(0));
                mTabStrip.notifyDataSetChanged();
                PrefUtils.saveObject("defaultTab",channelData.get(0));
                PrefUtils.saveObject("otherTab",channelData.get(1));
            }
        });
    }
}
