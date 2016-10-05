package com.jiumeng.movieheaven.parseserve;

import com.jiumeng.movieheaven.bean.MovieDao;
import com.jiumeng.movieheaven.moviedata.MovieParse;
import com.jiumeng.movieheaven.utils.PrefUtils;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/28 0028.
 */
public class ClassicsParseService {
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

    public static void getMovieList() throws Exception {

        ArrayList<MovieDao> movieList = new ArrayList<>();
        String string2 = getDataFromService("http://dytt8.net/html/gndy/jddy/20160320/50523.html");
        String string3 = getDataFromService("http://dytt8.net/html/gndy/jddy/20160320/50523_2.html");
        String string4 = getDataFromService("http://dytt8.net/html/gndy/jddy/20160320/50523_3.html");
        String string5 = getDataFromService("http://dytt8.net/html/gndy/jddy/20160320/50523_4.html");
        ArrayList<String> movieUrls2 = MovieParse.getMovieUrl2(string2);
        ArrayList<String> movieUrls3 = MovieParse.getMovieUrl2(string3);
        ArrayList<String> movieUrls4 = MovieParse.getMovieUrl2(string4);
        ArrayList<String> movieUrls5 = MovieParse.getMovieUrl2(string5);
        movieUrls2.addAll(movieUrls3);
        movieUrls2.addAll(movieUrls4);
        movieUrls2.addAll(movieUrls5);
        int i = 1;
        int page=0;
        for (String string : movieUrls2) {
            String text = getDataFromService(string);
            MovieDao movieDao = MovieParse.getMovieContent(text, string);

            if (movieDao != null) {
                if(i%20==0){
                    PrefUtils.saveObject("classice_"+(page++)+".object",movieList);
                    movieList=new ArrayList<>();
                }
                movieList.add(movieDao);
                System.out.println("当前" + (i++) + "----" + movieDao.minName);
            }
        }
        PrefUtils.saveObject("classice_"+(page++)+".object",movieList);
    }
}
