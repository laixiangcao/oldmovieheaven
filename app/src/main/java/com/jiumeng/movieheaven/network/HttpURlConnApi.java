package com.jiumeng.movieheaven.network;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class HttpURlConnApi {
    public String RequestDate(String text) throws Exception{
        URL url=new URL("http://www.dy2018.com/e/search/index.php");
        HttpURLConnection conn=(HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(6000);
        conn.setReadTimeout(6000);

        //添加post请求头中自动添加的属性
        //流里的数据的mimetype
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  //这句可不写
        String content="classid=0&show=title%2Csmalltext&tempid=1&keyboard="+ URLEncoder.encode(text)+"&Submit=%C1%A2%BC%B4%CB%D1%CB%F7";
        //流里数据的长度
        conn.setRequestProperty("Content-Length",content.length()+"");

        conn.setDoOutput(true); //是否输入参数
        //获取连接对象的输出流
        OutputStream os = conn.getOutputStream();
        //把数据写入输出流中
        os.write(content.getBytes());

        if(conn.getResponseCode()==200){
            InputStream is=conn.getInputStream();
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
        return "";
    }
}
