package com.jiumeng.movieheaven.cache;

import com.jiumeng.movieheaven.bean.MovieDao;
import com.jiumeng.movieheaven.utils.PrefUtils;
import com.jiumeng.movieheaven.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/25 0025.
 */
public class MovieCache {

    // wifi缓存时间为3小时
    private long wifi_cache_time = 60 * 60 * 1000;
    // 其他网络环境为6小时
    private long other_cache_time = 6 * 60 * 60 * 1000;

    /**
     * 判断缓存是否已经失效
     *
     * @return
     */
    public boolean isCacheDataFailure(String key) {
        long cacheTime = PrefUtils.getCacheTime(key);
        if (cacheTime == -1) {
            //缓存不存在
            return false;
        }
        long existTime = System.currentTimeMillis() - cacheTime;
        boolean failure;
        if (UIUtils.getNetworkType() == 2) {
            //wifi网络
            failure = existTime > wifi_cache_time ? true : false;
        } else if (UIUtils.getNetworkType() == 1) {
            //移动网络
            failure = existTime > other_cache_time ? true : false;
        } else {
            //没有网络
            failure = true;
        }
        return failure;
    }

    public ArrayList<MovieDao> getCacheData(String key){
        ArrayList<MovieDao> movieData = (ArrayList<MovieDao>) PrefUtils.readObject(key);
        return movieData;
    }


//    public boolean saveObject(Serializable ser, String file) {
//        FileOutputStream fos = null;
//        ObjectOutputStream oos = null;
//        try {
//            fos = UIUtils.getContext().openFileOutput(file, Context.MODE_PRIVATE);
//            oos = new ObjectOutputStream(fos);
//            oos.writeObject(ser);
//            oos.flush();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            try {
//                if (fos!=null){
//                    fos.close();
//                }
//                if (oos!=null){
//                    oos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public Serializable readObject(String file){
//        FileInputStream fis = null;
//        ObjectInputStream ois = null;
//        try {
//            fis = UIUtils.getContext().openFileInput(file);
//            ois = new ObjectInputStream(fis);
//            return (Serializable)ois.readObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//            // 反序列化失败 - 删除缓存文件
//            if (e instanceof InvalidClassException) {
//                File data = UIUtils.getContext().getFileStreamPath(file);
//                data.delete();
//            }
//            return null;
//        } finally {
//            try {
//                if (fis!=null){
//                    fis.close();
//                }
//                if (ois!=null){
//                    ois.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    /**
//     * 判断缓存是否存在
//     * @param cacheFile
//     * @return
//     */
//    public boolean isExistCacheData(String cacheFile) {
//        File file = UIUtils.getContext().getFileStreamPath(cacheFile);
//        if (file.exists()) {
//            return true;
//        } else {
//            return false;
//        }
//    }

//    /**
//     * 判断缓存是否已经失效
//     * @param cacheFile
//     * @return
//     */
//    public boolean isCacheDataFailure(String cacheFile) {
//        File data = UIUtils.getContext().getFileStreamPath(cacheFile);
//        if (!data.exists()) {
//            return false;
//        }
//        long existTime = System.currentTimeMillis() - data.lastModified();
//        boolean failure = false;
//        if (getNetworkType() == 2) {
//            failure = existTime > wifi_cache_time ? true : false;
//        } else {
//            failure = existTime > other_cache_time ? true : false;
//        }
//        return failure;
//    }

}
