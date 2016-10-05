/*
package com.jiumeng.newmovieheaven.cache;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;

import MovieDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

*/
/**
 * Created by Administrator on 2016/7/25 0025.
 *//*

public class MovieCache2 {
    //将集合中的链接写入本地文件
    public void writeUrlToLocalFile(ArrayList<String> urlList) {

        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "movieheaven");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        File file = new File(fileDir, "/url.txt");

        try {
            FileOutputStream fos = new FileOutputStream(file);
            byte[] bytes = urlList.toString().getBytes();
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //将集合中的数据缓存为json文件到本地
    public void writeJsonToLocalFile(ArrayList<MovieDao> data, int current_page) {

        int index = current_page + 1;
        String nextPage = "newest" + index + ".json";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("currentPage", current_page);//往jsonObject 放入当前页字段
            jsonObject.put("nextPage", nextPage);//往jsonObject 放入下一页字段

            JSONArray jsonArray = new JSONArray();

            for (int j = 0; j < data.size(); j++) {
                JSONObject jsonObject1 = new JSONObject();
                MovieDao movieDao = data.get(j);

                jsonObject1.put("name", movieDao.name);
                jsonObject1.put("minName", movieDao.minName);
                jsonObject1.put("poster_img_url", movieDao.poster_img_url);
                jsonObject1.put("screenshot_img_url", movieDao.screenshot_img_url);
                jsonObject1.put("time", movieDao.time);
                jsonObject1.put("url", movieDao.url);
                jsonObject1.put("introduction", movieDao.introduction);
                jsonObject1.put("downloadUrl", movieDao.downloadUrl);
                jsonObject1.put("country", movieDao.country);
                jsonObject1.put("subtitle", movieDao.subtitle);
                jsonObject1.put("play_time", movieDao.play_time);
                jsonObject1.put("language", movieDao.language);
                jsonObject1.put("category", movieDao.category);
                jsonObject1.put("years", movieDao.years);
                jsonObject1.put("ibdm", movieDao.ibdm);
                jsonObject1.put("movieFormat", movieDao.movieFormat);
                jsonObject1.put("director", movieDao.director);

                jsonArray.put(jsonObject1);
            }
            jsonObject.put("movies", jsonArray);
//            String fileName = "/newest" + current_page + ".json";
            String fileName = "/jsonCache.json";
            writeFileToLocal(jsonObject, fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getJsonCache() {
        StringBuilder sb = null;
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "movieheaven", "/jsonCache.json");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String s;
            sb = new StringBuilder();
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = null;
        if (sb!=null){
            result = sb.toString();
        }
        return result;
    }

    public void addJsonToLocalFile(ArrayList<MovieDao> data) {
        String result = getJsonCache();//先从本地获取json缓存
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("movies");
            int index=12;
            for (int i = 0; i < data.size(); i++) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    jsonArray.remove(index--);
                }
                JSONObject jsonObject1 = new JSONObject();
                MovieDao movieDao = data.get(i);

                jsonObject1.put("name", movieDao.name);
                jsonObject1.put("minName", movieDao.minName);
                jsonObject1.put("poster_img_url", movieDao.poster_img_url);
                jsonObject1.put("screenshot_img_url", movieDao.screenshot_img_url);
                jsonObject1.put("time", movieDao.time);
                jsonObject1.put("url", movieDao.url);
                jsonObject1.put("introduction", movieDao.introduction);
                jsonObject1.put("downloadUrl", movieDao.downloadUrl);
                jsonObject1.put("country", movieDao.country);
                jsonObject1.put("subtitle", movieDao.subtitle);
                jsonObject1.put("play_time", movieDao.play_time);
                jsonObject1.put("language", movieDao.language);
                jsonObject1.put("category", movieDao.category);
                jsonObject1.put("years", movieDao.years);
                jsonObject1.put("ibdm", movieDao.ibdm);
                jsonObject1.put("movieFormat", movieDao.movieFormat);
                jsonObject1.put("director", movieDao.director);
                jsonArray.put(jsonObject1);
            }

            jsonObject.put("movies", jsonArray);
//            String fileName = "/newest" + current_page + ".json";
            String fileName = "/jsonCache.json";
            writeFileToLocal(jsonObject, fileName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    */
/**
     * 写入JsonObject文件到外置存储空间默认文件夹是movieheaven
     *//*

    private void writeFileToLocal(JSONObject jsonObject, String fileName) {

        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "movieheaven");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }

        File file = new File(fileDir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] bytes = jsonObject.toString().getBytes();
            fos.write(bytes, 0, bytes.length);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public ArrayList<MovieDao> getCacheData() {
        String cache;
        ArrayList<MovieDao> dataList = null;
        if ((cache = getJsonCache()) != null) {
            try {
                JSONObject jsonObject = new JSONObject(cache);
                dataList = new ArrayList<MovieDao>();
                JSONArray movies = jsonObject.getJSONArray("movies");
                for (int i = 0; i < movies.length(); i++) {
                    JSONObject jsonObject1 = movies.getJSONObject(i);
                    MovieDao movieDao = new MovieDao();
                    movieDao.name = jsonObject1.getString("name");
                    movieDao.minName = jsonObject1.getString("minName");
                    movieDao.poster_img_url = jsonObject1.getString("poster_img_url");
                    movieDao.screenshot_img_url = jsonObject1.getString("screenshot_img_url");
                    movieDao.time = jsonObject1.getString("time");
                    movieDao.url = jsonObject1.getString("url");
                    movieDao.introduction = jsonObject1.getString("introduction");
                    movieDao.downloadUrl = jsonObject1.getString("downloadUrl");
                    movieDao.country = jsonObject1.getString("country");
                    movieDao.subtitle = jsonObject1.getString("subtitle");
                    movieDao.play_time = jsonObject1.getString("play_time");
                    movieDao.language = jsonObject1.getString("language");
                    movieDao.category = jsonObject1.getString("category");
                    movieDao.years = jsonObject1.getString("years");
                    movieDao.ibdm = jsonObject1.getString("ibdm");
                    movieDao.movieFormat = jsonObject1.getString("movieFormat");
                    movieDao.director = jsonObject1.getString("director");
                    dataList.add(movieDao);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }

    public boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
*/
