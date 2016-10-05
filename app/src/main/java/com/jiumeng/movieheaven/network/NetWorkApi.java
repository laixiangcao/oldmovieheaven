package com.jiumeng.movieheaven.network;

import com.loopj.android.http.AsyncHttpResponseHandler;


/**
 * 实现请求网络数据的封装
 * 加载网络数据时，只需要传入：分类 页面索引 接受请求结果的handler
 * Created by Administrator on 2016/7/27 0027.
 */
public class NetWorkApi {

    //CinemaMovie
    /**
     * 影院电影类型
     */
    public static final int MVOIETYPE_CINEMAMOVIE = 14;
    /**
     * 最新电影类型
     */
    public static final int MVOIETYPE_NEWEST = 0;
    /**
     * 最热电影类型
     */
    public static final int MVOIETYPE_HOTTEST = 2;
    /**
     * 剧情
     */
    public static final int MVOIETYPE_SCENARIO = 3;
    /**
     * 喜剧
     */
    public static final int MVOIETYPE_COMEDY = 4;
    /**
     * 动作
     */
    public static final int MVOIETYPE_ACTION = 5;
    /**
     * 爱情
     */
    public static final int MVOIETYPE_AFFECTION = 6;
    /**
     * 科幻
     */
    public static final int MVOIETYPE_SCIENCE_FICTION = 7;
    /**
     * 动画
     */
    public static final int MVOIETYPE_ANIMATION = 8;
    /**
     * 悬疑
     */
    public static final int MVOIETYPE_SUSPENSE = 9;
    /**
     * 惊悚
     */
    public static final int MVOIETYPE_PANIC = 10;
    /**
     * 战争
     */
    public static final int MVOIETYPE_WAR = 11;
    /**
     * 犯罪
     */
    public static final int MVOIETYPE_CRIME = 12;
    /**
     * 恐怖
     */
    public static final int MVOIETYPE_TERROR = 13;


    private final static String MYHOST = "http://oaydggmwi.bkt.clouddn.com";//主连接
    private final static String HOST = "http://www.dy2018.com";//主连接

    /**
     * 获取指定类型和指定页数的电影集合
     *
     * @param catalog 电影类型
     * @param page    页
     * @param handler 接收请求结果的handler
     */
    public static void getMovieList(int catalog, int page, AsyncHttpResponseHandler handler) {
        String path = "";
        switch (catalog) {
            case MVOIETYPE_NEWEST:
                if (page == 1) {
                    path = HOST + "/html/gndy/dyzz/index.html";
                } else {
                    path = HOST + "/html/gndy/dyzz/index_" + page + ".html";
                }
                break;
            case MVOIETYPE_HOTTEST:
                if (page == 1) {
                    path = HOST + "/html/bikan/";
                } else {
                    path = HOST + "/html/bikan/index_" + page + ".html";
                }
                break;
            case MVOIETYPE_SCENARIO:
                if (page == 1) {
                    path = HOST + "/0/";
                } else {
                    path = HOST + "/0/index_" + page + ".html";
                }
                break;
            case MVOIETYPE_COMEDY:
                if (page == 1) {
                    path = HOST + "/1/";
                } else {
                    path = HOST + "/1/index_" + page + ".html";
                }
                break;
            case MVOIETYPE_ACTION:
                if (page == 1) {
                    path = HOST + "/2/";
                } else {
                    path = HOST + "/2/index_" + page + ".html";
                }
                break;
            case MVOIETYPE_AFFECTION:
                if (page == 1) {
                    path = HOST + "/3/";
                } else {
                    path = HOST + "/3/index_" + page + ".html";
                }
                break;
            case MVOIETYPE_SCIENCE_FICTION:
                if (page == 1) {
                    path = HOST + "/4/";
                } else {
                    path = HOST + "/4/index_" + page + ".html";
                }
                break;
            case MVOIETYPE_ANIMATION:
                if (page == 1) {
                    path = HOST + "/5/";
                } else {
                    path = HOST + "/5/index_" + page + ".html";
                }
                break;
            case MVOIETYPE_SUSPENSE:
                if (page == 1) {
                    path = HOST + "/6/";
                } else {
                    path = HOST + "/6/index_" + page + ".html";
                }
                break;
            case MVOIETYPE_PANIC:
                if (page == 1) {
                    path = HOST + "/7/";
                } else {
                    path = HOST + "/7/index_" + page + ".html";
                }
                break;
            case MVOIETYPE_WAR:
                if (page == 1) {
                    path = HOST + "/14/";
                } else {
                    path = HOST + "/14/index_" + page + ".html";
                }
                break;
            case MVOIETYPE_CRIME:
                if (page == 1) {
                    path = HOST + "/15/";
                } else {
                    path = HOST + "/15/index_" + page + ".html";
                }
                break;
            case MVOIETYPE_TERROR:
                if (page == 1) {
                    path = HOST + "/8/";
                } else {
                    path = HOST + "/8/index_" + page + ".html";
                }
                break;
            case MVOIETYPE_CINEMAMOVIE:
                path = MYHOST + "/CinemaMovie/CinemaMovieList" + page + ".json";
                break;
        }
        AsyncHttpClientApi.requestData(path, handler);
    }
}
