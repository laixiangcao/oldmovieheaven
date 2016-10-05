package com.jiumeng.movieheaven.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by Administrator on 2016/8/3 0003.
 */
public class PrefUtils {

    public static void moveFile(){
        InputStream is;
        FileOutputStream fos;
        try {
            File file = new File("data/data/com.jiumeng.movieheaven/shared_prefs", "/ObjectCache.xml");
            is = UIUtils.getContext().getResources().getAssets().open("ObjectCache.xml");
            fos=new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            while ((is.read(bytes))!=-1){
                fos.write(bytes);
            }
            is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void putString(String key, String value) {
        SharedPreferences config = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = config.edit();
        edit.putString(key, value);
        edit.commit();
    }
    public static String getString(String key){
        SharedPreferences config = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        return config.getString(key,null);
    }
    public static void putInt(String key, int value) {
        SharedPreferences config = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = config.edit();
        edit.putInt(key, value);
        edit.commit();
    }
    public static int getInt(String key){
        SharedPreferences config = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        return config.getInt(key,-1);
    }

    public static void putLong(String key, Long value) {
        SharedPreferences config = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = config.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public static long getLong(String key){
        SharedPreferences config = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        return config.getLong(key,-1);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences config = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = config.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static boolean getBoolean(String key){
        SharedPreferences config = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        return config.getBoolean(key,false);
    }

    /**
     * 设置缓存时间戳
     */
    public static void setCacheTime(String key){
        SharedPreferences.Editor edit = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE).edit();
        edit.putLong(key, System.currentTimeMillis());
        edit.commit();
    }

    /**
     * 获取缓存时间戳
     */
    public static long getCacheTime(String key){
        SharedPreferences config = UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        return config.getLong(key,-1);
    }

    /**
     * 保存对象
     * @param obj 要保存的对象，只能保存实现了serializable的对象
     * modified:
     */
    public static void saveObject(String key ,Object obj){
        try {
            // 保存对象
            SharedPreferences.Editor sharedata = UIUtils.getContext().getSharedPreferences("ObjectCache", Context.MODE_PRIVATE).edit();
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            ObjectOutputStream os=new ObjectOutputStream(bos);
            //将对象序列化写入byte缓存
            os.writeObject(obj);
            //将序列化的数据转为16进制保存
            String bytesToHexString = bytesToHexString(bos.toByteArray());
            //保存该16进制数组
            sharedata.putString(key, bytesToHexString);
            sharedata.commit();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("", "保存obj失败");
        }
    }
    /**
     * desc:将数组转为16进制
     * @param bArray
     * @return

     */
    private static String bytesToHexString(byte[] bArray) {
        if(bArray == null){
            return null;
        }
        if(bArray.length == 0){
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
    /**
     * desc:获取保存的Object对象
     * @param key
     * @return
     */
    public static Object readObject(String key ){
        try {
            SharedPreferences sharedata = UIUtils.getContext().getSharedPreferences("ObjectCache", Context.MODE_PRIVATE);
            if (sharedata.contains(key)) {
                String string = sharedata.getString(key, "");
                if(TextUtils.isEmpty(string)){
                    return null;
                }else{
                    //将16进制的数据转为数组，准备反序列化
                    byte[] stringToBytes = StringToBytes(string);
                    ByteArrayInputStream bis=new ByteArrayInputStream(stringToBytes);
                    ObjectInputStream is=new ObjectInputStream(bis);
                    //返回反序列化得到的对象
                    Object readObject = is.readObject();
                    return readObject;
                }
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //所有异常返回null
        return null;

    }
    /**
     * desc:将16进制的数据转为数组
     */
    private static byte[] StringToBytes(String data){
        String hexString=data.toUpperCase().trim();
        if (hexString.length()%2!=0) {
            return null;
        }
        byte[] retData=new byte[hexString.length()/2];
        for(int i=0;i<hexString.length();i++)
        {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
            int int_ch1;
            if(hex_char1 >= '0' && hex_char1 <='9')
                int_ch1 = (hex_char1-48)*16;   //// 0 的Ascll - 48
            else if(hex_char1 >= 'A' && hex_char1 <='F')
                int_ch1 = (hex_char1-55)*16; //// A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
            int int_ch2;
            if(hex_char2 >= '0' && hex_char2 <='9')
                int_ch2 = (hex_char2-48); //// 0 的Ascll - 48
            else if(hex_char2 >= 'A' && hex_char2 <='F')
                int_ch2 = hex_char2-55; //// A 的Ascll - 65
            else
                return null;
            int_ch = int_ch1+int_ch2;
            retData[i/2]=(byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }
}
