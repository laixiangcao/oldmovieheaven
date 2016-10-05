package com.jiumeng.movieheaven.network;

import com.jiumeng.movieheaven.utils.UIUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by Administrator on 2016/7/17 0017.
 */
public class AsyncHttpClientApi {


    private AsyncHttpClientApi() {
    }

    private static AsyncHttpClient client = new AsyncHttpClient();
    public static AsyncHttpClient getInstance(){
        return client;
    }
    public static void requestData(String url, AsyncHttpResponseHandler handler) {
        client.get(UIUtils.getContext(), url, handler);
    }
    public static void requestData(String url, JsonHttpResponseHandler handler) {
        String path = url;
        client.get(UIUtils.getContext(), path, handler);
    }
}
