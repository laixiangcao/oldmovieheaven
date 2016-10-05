package com.jiumeng.movieheaven.moviedata;

import com.jiumeng.movieheaven.bean.MovieDao;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/28 0028.
 */
public class ClassicsParse {

    private static ArrayList<MovieDao> sMovieList;

    /**
     * 加载网页获取网页文本
     * @param path 需要加载的网页url
     * @return 网页文本
     */
    public static String getDataFromService(String path) {
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

    public static ArrayList<MovieDao> getMovieList(final String url)  {

        Thread t=new Thread(){
            @Override
            public void run() {
                String content = getDataFromService(url);
                ArrayList<String> movieUrls = MovieParse.getMovieUrl2(content);
                sMovieList = new ArrayList<>();
                for (String string : movieUrls) {
                    String text = getDataFromService(string);
                    MovieDao movieDao = MovieParse.getMovieContent(text, string);
                    sMovieList.add(movieDao);
                }
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return sMovieList;
    }
}
