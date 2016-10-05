package com.jiumeng.movieheaven.fragment.otherfragment;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiumeng.movieheaven.bean.MovieDao;
import com.jiumeng.movieheaven.network.AsyncHttpClientApi;
import com.jiumeng.movieheaven.parsedata.ParseData;
import com.jiumeng.movieheaven.utils.PrefUtils;
import com.jiumeng.movieheaven.utils.UIUtils;
import com.jiumeng.movieheaven.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.UnsupportedEncodingException;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/8/4 0004.
 */
public class MovieDetailsFragment extends Fragment implements View.OnClickListener{

    private View mRootView;
    private MovieDao mMovie;
    private LinearLayout mLl_file_size;
    private LinearLayout mLl_release_time;
    private TextView mTv_release_time;
    private TextView mTv_file_size;
    private ImageView mIv_img;
    private ImageView mIv_img2;
    private TextView mTv_title;
    private TextView mTv_years;
    private TextView mTv_subtitle;
    private TextView mTv_play_time;
    private TextView mTv_language;
    private TextView mTv_category;
    private TextView mTv_grade;
    private TextView mTv_update_time;
    private TextView mTv_country;
    private TextView mTv_starring;
    private TextView mTv_director;
    private TextView mTv_introduction;
    private ImageView mIv_share;
    private ImageView mIv_download;
    private ImageView iv_fav;
    private MovieDao mDetailsData;
    private boolean isShowNewestOrClassicsUI=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_movie_details,null);
        initUI();
        initData();

        return mRootView;
    }

    private void initUI() {
        mLl_file_size = (LinearLayout) mRootView.findViewById(R.id.ll_file_size);
        mLl_release_time = (LinearLayout) mRootView.findViewById(R.id.ll_release_time);
        mTv_release_time = (TextView) mRootView.findViewById(R.id.tv_release_time);
        mTv_file_size = (TextView) mRootView.findViewById(R.id.tv_file_size);
        mIv_img = (ImageView) mRootView.findViewById(R.id.iv_img);
        mIv_img2 = (ImageView) mRootView.findViewById(R.id.iv_img2);
        mTv_title = (TextView) mRootView.findViewById(R.id.tv_title);
        mTv_years = (TextView) mRootView.findViewById(R.id.tv_years);
        mTv_subtitle = (TextView) mRootView.findViewById(R.id.tv_subtitle);
        mTv_play_time = (TextView) mRootView.findViewById(R.id.tv_play_time);
        mTv_language = (TextView) mRootView.findViewById(R.id.tv_language);
        mTv_category = (TextView) mRootView.findViewById(R.id.tv_category);
        mTv_grade = (TextView) mRootView.findViewById(R.id.tv_ibdm);
        mTv_update_time = (TextView) mRootView.findViewById(R.id.tv_update_time);
        mTv_country = (TextView) mRootView.findViewById(R.id.tv_country);
        mTv_starring = (TextView) mRootView.findViewById(R.id.tv_starring);
        mTv_director = (TextView) mRootView.findViewById(R.id.tv_director);
        mTv_introduction = (TextView) mRootView.findViewById(R.id.tv_introduction);
        mIv_share = (ImageView) mRootView.findViewById(R.id.iv_share);
        mIv_download = (ImageView) mRootView.findViewById(R.id.iv_download);
        iv_fav = (ImageView) mRootView.findViewById(R.id.iv_fav);
        iv_fav.setOnClickListener(this);
        mIv_share.setOnClickListener(this);
        mIv_download.setOnClickListener(this);
    }

    private void initData() {

        Bundle bundle = getArguments();
        mMovie = (MovieDao) bundle.getSerializable("movie");
        String movieType = bundle.getString("movieType");
        showCommonUI();
        if (movieType.equals("synthesis")||movieType.equals("classics")){
            showSynthesisOrClassicsUI();
        }else if (movieType.equals("hottest")){
            showHottestUI();
        }else if (movieType.equals("classify")){
            showClassifyUI();
        }else if(movieType.equals("newest")){
            showNewestUI();
        }
    }

    /**
     * 显示共有的属性
     */
    private void showCommonUI() {
        mTv_title.setText(mMovie.minName);
        mTv_years.setText("年代："+ mMovie.years);
        mTv_category.setText("类别："+ mMovie.category);
        mTv_grade.setText("评分："+ mMovie.grade);
        mTv_update_time.setText("更新日期："+ mMovie.updatetime);
        mTv_country.setText("国家：　"+ mMovie.country);
    }

    /**
     * 显示综合或经典模块属性
     */
    private void showSynthesisOrClassicsUI() {
        ImageLoader.getInstance().displayImage(mMovie.jpgList.get(0),mIv_img, UIUtils.getDisplayImageOptions2());
        if ( mMovie.jpgList.size()>1) {
            ImageLoader.getInstance().displayImage(mMovie.jpgList.get(1),mIv_img2,UIUtils.getDisplayImageOptions());
        }
        mTv_subtitle.setText("字幕："+ mMovie.subtitle);
        mTv_language.setText("语言："+ mMovie.language);
        mTv_play_time.setText("片长："+ mMovie.play_time);
        mTv_director.setText(mMovie.director);
        mTv_starring.setText(mMovie.starring);
        mTv_introduction.setText(mMovie.introduction);
        isShowNewestOrClassicsUI=true;
    }

    /**
     * 显示最热模块属性
     */
    private void showHottestUI() {
        //已有的  url id name minNmae  日期 评分 类型 年代  国家  语言 字幕 上映日期
        mTv_subtitle.setText("字幕："+ mMovie.subtitle);
        mTv_language.setText("语言："+ mMovie.language);
        if (!mMovie.release.equals("")){
            mLl_release_time.setVisibility(View.VISIBLE);
            mTv_release_time.setText("上映日期："+mMovie.release);
        }
        //需要获取的 简介 海报图 详情图 下载链接 片长 导演 主演 文件大小
        AsyncHttpClientApi.requestData(mMovie.url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String content = null;
                try {
                    content = new String(responseBody, "GBK");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                 mDetailsData = ParseData.parseMovieDetails(content,false);

                for (int i = 0; i < mDetailsData.jpgList.size(); i++) {
                    if (i==0){
                        ImageLoader.getInstance().displayImage(mDetailsData.jpgList.get(0),mIv_img, UIUtils.getDisplayImageOptions2());
                    }else{
                        ImageLoader.getInstance().displayImage(mDetailsData.jpgList.get(1),mIv_img2,UIUtils.getDisplayImageOptions());
                    }
                }
                if (!mDetailsData.filesize.equals("")){
                    mLl_file_size.setVisibility(View.VISIBLE);
                    mTv_file_size.setText("文件大小："+mDetailsData.filesize);
                }
                mTv_play_time.setText("片长："+ mDetailsData.play_time);
                mTv_director.setText(mDetailsData.director);
                mTv_starring.setText(mDetailsData.starring);
                mTv_introduction.setText(mDetailsData.introduction);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
            }
        });

    }

    /**
     * 显示最新模块属性
     */
    private void showNewestUI(){
        //已有的  url id name minNmae  日期 评分 类型 年代  国家  语言 字幕 上映日期
        mTv_subtitle.setText("字幕："+ mMovie.subtitle);
        mTv_language.setText("语言："+ mMovie.language);
        if (!mMovie.release.equals("")){
            mLl_release_time.setVisibility(View.VISIBLE);
            mTv_release_time.setText("上映日期："+mMovie.release);
        }

        //需要获取的 年代  国家  语言 字幕 上映日期 简介 海报图 详情图 下载链接 片长 导演 主演 文件大小
        AsyncHttpClientApi.requestData(mMovie.url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String content = null;
                try {
                    content = new String(responseBody, "GBK");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                mDetailsData = ParseData.parseMovieDetails(content,false);

                for (int i = 0; i < mDetailsData.jpgList.size(); i++) {
                    if (i==0){
                        ImageLoader.getInstance().displayImage(mDetailsData.jpgList.get(0),mIv_img, UIUtils.getDisplayImageOptions2());
                    }else{
                        ImageLoader.getInstance().displayImage(mDetailsData.jpgList.get(1),mIv_img2,UIUtils.getDisplayImageOptions());
                    }
                }

                mTv_play_time.setText("片长："+ mDetailsData.play_time);
                mTv_director.setText(mDetailsData.director);
                mTv_starring.setText(mDetailsData.starring);
                if (!mDetailsData.filesize.equals("")){
                    mLl_file_size.setVisibility(View.VISIBLE);
                    mTv_file_size.setText("文件大小："+ mDetailsData.filesize);
                }
                mTv_introduction.setText(mDetailsData.introduction);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
            }
        });

    }
    /**
     * 显示分类电影模块属性
     */
    private void showClassifyUI(){
        //已有的 url id name minNmae 日期 类型 评分 导演 主演
        mTv_director.setText(mMovie.director);
        mTv_starring.setText(mMovie.starring);

        //需要获取的 年代  国家  语言 字幕 上映日期 简介 海报图 详情图 下载链接 片长 导演 主演 文件大小
        AsyncHttpClientApi.requestData(mMovie.url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String content = null;
                try {
                    content = new String(responseBody, "GBK");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                mDetailsData = ParseData.parseMovieDetails(content,true);

                for (int i = 0; i < mDetailsData.jpgList.size(); i++) {
                    if (i==0){
                        ImageLoader.getInstance().displayImage(mDetailsData.jpgList.get(0),mIv_img, UIUtils.getDisplayImageOptions2());
                    }else{
                        ImageLoader.getInstance().displayImage(mDetailsData.jpgList.get(1),mIv_img2,UIUtils.getDisplayImageOptions());
                    }
                }
                //年代  国家  语言 字幕 上映日期 简介 海报图 详情图 下载链接 片长  文件大小
                mTv_years.setText("年代："+mDetailsData.years);
                mTv_country.setText("国家：　"+mDetailsData.country);
                mTv_play_time.setText("片长："+ mDetailsData.play_time);
                if (!mDetailsData.filesize.equals("")){
                    mLl_file_size.setVisibility(View.VISIBLE);
                    mTv_file_size.setText("文件大小："+ mDetailsData.filesize);
                }
                mTv_introduction.setText(mDetailsData.introduction);

                mTv_subtitle.setText("字幕："+ mDetailsData.subtitle);
                mTv_language.setText("语言："+ mDetailsData.language);
                if (!mDetailsData.release.equals("")){
                    mLl_release_time.setVisibility(View.VISIBLE);
                    mTv_release_time.setText("上映日期："+mDetailsData.release);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
            }
        });

    }


    private void showShare() {
        ShareSDK.initSDK(UIUtils.getContext());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("电影天堂");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://oaydggmwi.bkt.clouddn.com/com.jiumeng.movieheaven.apk");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("您的好友分享了一部大片给你："+mMovie.minName);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://oaydggmwi.bkt.clouddn.com/com.jiumeng.movieheaven.apk");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://oaydggmwi.bkt.clouddn.com/com.jiumeng.movieheaven.apk");

        // 启动分享GUI
        oks.show(UIUtils.getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_share:
                showShare();
                break;
            case R.id.iv_download:
                String downUrl="";
                if (isShowNewestOrClassicsUI){
                    downUrl = mMovie.downlist.get(0);
                }else{
                    downUrl= mDetailsData.downlist.get(0);
                }

                if (isPkgInstalled()){
                    //已安装迅雷
                    if (!downUrl.equals("")){
                        startXunlei(downUrl);
                    }else {
                        Toast.makeText(UIUtils.getContext(), "未找到下载连接",0).show();
                    }
                }else {
                    //未安装迅雷
                    showInstallDia();
                }

                break;
            case R.id.iv_fav:
                break;
        }
    }

    private void showInstallDia(){
        AlertDialog.Builder normalDia=new AlertDialog.Builder(getContext());
//        normalDia.setIcon(R.drawable.ic_launcher);
        normalDia.setTitle("来自天堂的通知：");
        normalDia.setMessage("　　系统检测到您手机未安装\"手机迅雷软件\",将无法为您提供下载服务.\r\n\r\n  是否为您下载手机迅雷软件？");

        normalDia.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownloadXunlei();
            }
        });
        normalDia.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"很遗憾未能帮你下载该电影",0).show();
            }
        });
        normalDia.create().show();
    }

    /**
     * 启动迅雷下载
     * @param url 需要下载的链接
     */
    private void startXunlei(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addCategory("android.intent.category.DEFAULT");
        startActivity(intent);
    }

    /**
     * 用于下载迅雷
     */
    private void DownloadXunlei(){

        String str = "/Download/APK/手机迅雷.apk";
        String fileName = Environment.getExternalStorageDirectory() + str;
        File file = new File(fileName);
        boolean isExists = file.exists();

        //情况一：上次下载完成后 却没有完成安装
        //直接安装即可
        if (isExists){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            startActivity(intent);

        //情况二：从未下载过
        // 调用系统的下载管理器 下载并安装
        }else {
            //获取系统的下载管理器
            DownloadManager downloadManager = (DownloadManager) UIUtils.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            //设置下载连接
            Uri uri = Uri.parse("http://oaydggmwi.bkt.clouddn.com/手机迅雷.apk");

            //请求下载
            DownloadManager.Request request = new DownloadManager.Request(uri);

            //设置允许使用的网络类型，这里是移动网络和wifi都可以
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);

            // 设置下载路径和文件名
            request.setDestinationInExternalPublicDir("Download/APK", "手机迅雷.apk");

            request.setDescription("手机迅雷下载");

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            request.setMimeType("application/vnd.android.package-archive");

            // 设置为可被媒体扫描器找到
            request.allowScanningByMediaScanner();

            // 设置为可见和可管理
            request.setVisibleInDownloadsUi(true);

            long refernece = downloadManager.enqueue(request);
            // 把当前下载的ID保存起来
            PrefUtils.putLong("downloadID",refernece);
        }
    }

    /**
     * 判断是否安装了手机迅雷软件
     * @return
     */
    private boolean isPkgInstalled() {
        String pkgName="com.xunlei.downloadprovider";
        PackageInfo packageInfo = null;
        try {
            packageInfo = UIUtils.getContext().getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}
