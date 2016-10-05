package com.jiumeng.movieheaven.moviedata;


import com.jiumeng.movieheaven.bean.MovieDao;
import com.jiumeng.movieheaven.utils.PrefUtils;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by 7 on 2016/6/11.
 */
public class MovieHelper {

    private ArrayList<String> mUrlList;
    private static MovieHelper movieHelper = new MovieHelper();
    private static int loadPosition = 0;
    public int listSize;

    private MovieHelper() {
    }

    public static MovieHelper getInstance() {
        return movieHelper;
    }

    /**
     * 获取电影天堂首页 最新170部电影的Url连接
     * @return 最新170部电影的Url连接的集合
     */
    public void initMovieUrl() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                String webContent = getDataFromService("http://www.dytt8.net/");
                mUrlList = MovieParse.getMovieUrl(webContent);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取指定区间的电影集合
     * 采用多线程并发加载，大幅度提升加载速度
     * @param urls 最新170部电影的Url连接的集合
     * @param sta  需要加载的起始位置的URL
     * @param end  需要加载的结束位置的URL
     * @return 指定区间的电影集合
     */
    public ArrayList<MovieDao> getMovieList(final ArrayList<String> urls, int sta, int end) {
        final ArrayList<MovieDao> mMovieList = new ArrayList<>();
        Vector<Thread> threads = new Vector<Thread>();
        for (int i = sta; i < end; i++) {

            final int finalI = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String data = getDataFromService(urls.get(finalI));
                    if (data != null) {
                        MovieDao movieDao = MovieParse.getMovieContent(data, urls.get(finalI));
                        mMovieList.add(movieDao);
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }
        //遍历全部已启动的线程  等待全部线程都线程终止
        for (Thread iThread : threads) {
            try {
                // 等待该线程终止
                iThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(mMovieList, new MovieSort());
        //线程全部终止后 发送消息

        return mMovieList;
    }

    //第一次加载
    public ArrayList<MovieDao> initLoad() {
        listSize = mUrlList.size();
        loadPosition = 12;
        ArrayList<MovieDao> movieList = getMovieList(mUrlList, 0, loadPosition);
        return movieList;
    }

    //加载更多
    public ArrayList<MovieDao> loadMore() {
        if (mUrlList == null) {
            initMovieUrl();
        }
        int endPosition = loadPosition + 12;
        if (loadPosition > listSize) {
            return null;
        }
        if (endPosition > listSize) {
            endPosition = listSize;
        }
        ArrayList<MovieDao> movieList = getMovieList(mUrlList, loadPosition, endPosition);
        loadPosition += movieList.size();
        return movieList;
    }

    /**
     * 检测更新（下拉刷新）
     * 通过获取缓存的url链接 与 网页获取的全部连接比对 判断是否有更新
     * 如果有更新返回更新出的电影URL的集合，否则返回一个空集合
     * @return 更新出来的电影集合 如果返回空表示没有更新
     */
    public ArrayList<MovieDao> refreshData() {

        String url = PrefUtils.getString("NewestFirstUrl");
        if (url == null) {
            return null;
        }
        if (mUrlList == null) {
            initMovieUrl();
        }
        ArrayList<String> newUrls = new ArrayList<>();
        for (int i = 0; i < mUrlList.size(); i++) {
            if (url.equals(mUrlList.get(i))) {
                break;
            } else {
                newUrls.add(mUrlList.get(i));
            }
        }
        if (newUrls.size() == 0) {
            return null;
        } else {
            ArrayList<MovieDao> movieList = getMovieList(newUrls, 0, newUrls.size());
            ArrayList<MovieDao> newestDataCache = (ArrayList<MovieDao>) PrefUtils.readObject("NewestDataCache");
            for (int i = 0; i < movieList.size(); i++) {
                newestDataCache.remove(newestDataCache.size()-(i+1));//移除末尾的一个缓存的电影
                newestDataCache.add(i,movieList.get(i));//在缓冲的集合头部添加中刷新的数据
            }
            PrefUtils.putString("NewestFirstUrl", mUrlList.get(0));
            PrefUtils.saveObject("NewestDataCache",newestDataCache);//更新缓冲
            return movieList;
        }
    }

    /**
     * 加载网页获取网页文本
     * @param path 需要加载的网页url
     * @return 网页文本
     */
    private static String getDataFromService(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] bytes = new byte[1024 * 300];
                int len;
                while ((len = is.read(bytes)) != -1) {
                    bos.write(bytes, 0, len);
                }
                String result = new String(bos.toByteArray(), "GBK");
                bos.close();
                return result;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public ArrayList<MovieDao> initData() {
        //1、进入界面 默认先加载缓存的数据
        ArrayList<MovieDao> dataList = (ArrayList<MovieDao>) PrefUtils.readObject("NewestDataCache");
        listSize = PrefUtils.getInt("urlListSize");
        loadPosition = 12;
        //2、若缓存数据为空，则加载网络数据
        if (dataList == null || dataList.size() == 0) {
            initMovieUrl();
            PrefUtils.putInt("urlListSize", mUrlList.size());
            PrefUtils.putString("NewestFirstUrl", mUrlList.get(0));
            if (mUrlList != null) {
                dataList = initLoad();//初始化第一页数据
                PrefUtils.saveObject("NewestDataCache",dataList);
            }
        }
        if (dataList != null) {
            return dataList;
        }
        return null;
    }
}
