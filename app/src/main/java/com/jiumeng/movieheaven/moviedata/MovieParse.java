package com.jiumeng.movieheaven.moviedata;

import com.jiumeng.movieheaven.bean.MovieDao;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 7 on 2016/4/24.
 */
public class MovieParse {
    /**
     * 解析根据网页文本 获取 170部电影的链接
     * @param text 接收主页面的全文本
     * @return 170部电影的链接
     */
    public static ArrayList<String> getMovieUrl(String text) {
        ArrayList<String> arrayList=null;
        if (text!=null){
            int index = text.indexOf("<!--{start:最新-->");
            int index2 = text.indexOf("<!--}end:最新-->");
            String string = text.substring(index, index2);
            String teg = "html.*html";
            Pattern p = Pattern.compile(teg);
            Matcher m = p.matcher(string);
            arrayList = new ArrayList<>();
            while (m.find()) {
                if (!m.group().equals("html/gndy/jddy/20160320/50523.html")) {
                    arrayList.add("http://www.dytt8.net/" + m.group());
                }
            }
        }

        return arrayList;
    }

    public static ArrayList<String> getMovieUrl2(String text) {
        ArrayList<String> arrayList=null;
        if (text!=null){
            int index = text.indexOf("<!--Content Start-->");
            int index2 = text.indexOf("<br><center>");
            String string = text.substring(index, index2);
            String teg = ">http.*html";
            Pattern p = Pattern.compile(teg);
            Matcher m = p.matcher(string);
            arrayList = new ArrayList<>();
            while (m.find()) {
                System.out.println(arrayList.add(m.group().replace(">", "")));
            }
        }
        return arrayList;
    }

    /**
     * 将网页文本解析并封装到对象中
     * @param text 接收一个网页全本文内容
     * @param url  接收一个电影的链接
     * @return 解析后的封装了一部电影的全部信息
     */
    public static MovieDao getMovieContent(String text, String url) {

        String string2;
        MovieDao movie;

        // 获取网页部分文本
        int index1 = text.indexOf("end:搜索");
        int index2 = text.indexOf("<center></center>");
        if (index1 != -1 && index2 != -1) {
            string2 = text.substring(index1, index2);
            movie = new MovieDao();
        }else{
            return null;
        }

        // 获取电影名字
        int index3 = string2.indexOf("color=#07519a>");
        int index4 = string2.indexOf("</font>");
        if(index3!=-1&&index4!=-1){
            movie.name = string2.substring(index3 + 14, index4);

            //获取电影名字（短）
            String teg = "《.*》";
            Pattern p = Pattern.compile(teg);
            Matcher m = p.matcher(movie.name);
            if (m.find()) {
                movie.minName = m.group().replace("《", "").replace("》", "").trim();
            }else {
                movie.minName="";
            }

        }else{
            movie.name="";
        }

        // 获取电影发布时间
        int index5 = string2.indexOf("发布时间");
        if(index5!=-1){
            movie.updatetime = string2.substring(index5 + 5, index5 + 15);
        }else{
            movie.updatetime ="";
        }

        //获取图片
        String teg2 = "src=\"http+://[^\\s]+(jpg|png)";
        Pattern p2 = Pattern.compile(teg2,Pattern.MULTILINE);
        Matcher m2 = p2.matcher(string2);
        movie.jpgList = new ArrayList<String>();
        while (m2.find()) {
            movie.jpgList.add(m2.group().replace("src=\"", ""));
        }

        //获取电影的下载地址
        String teg = "(>ftp://)+[^\\s]+(rmvb|mkv|mp4)";
        Pattern p = Pattern.compile(teg,Pattern.MULTILINE);
        Matcher m = p.matcher(string2);
        movie.downlist = new ArrayList<String>();
        while (m.find()) {
            movie.downlist.add(m.group().replace(">", ""));
        }

        movie.url = url;
        movie.id=toMD5(url);

        string2=string2.replaceAll("</strong></span>", "").replaceAll("&ldquo;", "\"").replaceAll("<span style=\"COLOR: red\"><strong>", "").replaceAll("&rdquo;", "\"").replaceAll("&mdash;", "—").replaceAll("&middot;", "·")
                .replaceAll("&hellip;", "...").replaceAll("&nbsp;", " ").replaceAll("<br />","\n");

        int mIndex = string2.indexOf("年　　代");
        int mIndex2 = string2.indexOf("◎", mIndex);
        if (mIndex == -1 || mIndex2 == -1) {
            movie.years = "未知";
        } else {
            movie.years = string2.substring(mIndex + 4, mIndex2).replaceAll("　","");
        }


        int mIndex3 = string2.indexOf("国　　家", mIndex2);
        int mIndex4 = string2.indexOf("◎", mIndex3);
        if (mIndex3 == -1 || mIndex4 == -1) {
            movie.country = "未知";
        } else {
            movie.country = string2.substring(mIndex3 + 4, mIndex4).replaceAll("　","");
        }


        int mIndex5 = string2.indexOf("类　　别", mIndex4);
        int mIndex6 = string2.indexOf("◎", mIndex5);
        if (mIndex5 == -1 || mIndex6 == -1) {
            movie.category = "未知";
        } else {
            movie.category = string2.substring(mIndex5 + 4, mIndex6).replaceAll("　","");
        }

        int mIndex7 = string2.indexOf("语　　言", mIndex6);
        int mIndex8 = string2.indexOf("◎", mIndex7);
        if (mIndex7 == -1 || mIndex8 == -1) {
            movie.language = "未知";
        } else {
            movie.language = string2.substring(mIndex7 + 4, mIndex8).replaceAll("　","");
        }

        int mIndex9 = string2.indexOf("字　　幕", mIndex8);
        int mIndex10 = string2.indexOf("◎", mIndex9);
        if (mIndex9 == -1 || mIndex10 == -1) {
            movie.subtitle = "未知";
        } else {
            movie.subtitle = string2.substring(mIndex9 + 4, mIndex10).replaceAll("　","");
        }

        int mIndex11 = string2.indexOf("评分", mIndex10);
        int mIndex12 = string2.indexOf("/", mIndex11);
        if (mIndex11 == -1 || mIndex12 == -1) {
            movie.grade = "无";
        } else {
            movie.grade = string2.substring(mIndex12-3, mIndex12).replaceAll("　","");
        }


        int mIndex13 = string2.indexOf("文件格式", mIndex12);
        int mIndex14 = string2.indexOf("◎", mIndex13);
        if (mIndex13 == -1 || mIndex14 == -1) {
            movie.movieFormat = "未知";
        } else {
            movie.movieFormat = string2.substring(mIndex13 + 4, mIndex14).replaceAll("　","");
        }

        int mIndex15 = string2.indexOf("片　　长", mIndex14);
        int mIndex16 = string2.indexOf("◎", mIndex15);
        if (mIndex15 == -1 || mIndex16 == -1) {
            movie.play_time = "未知";
        } else {
            movie.play_time = string2.substring(mIndex15 + 4, mIndex16).replaceAll("　","");
        }

        int mIndex17 = string2.indexOf("导　　演", mIndex16);
        int mIndex18 = string2.indexOf("◎", mIndex17);
        if (mIndex17 == -1 || mIndex18 == -1) {
            movie.director = "未知";
        } else {
            movie.director = string2.substring(mIndex17 + 4, mIndex18).replaceAll("　","");
        }
        int mIndex19 = string2.indexOf("主　　演", mIndex18);
        int mIndex20 = string2.indexOf("◎", mIndex19);
        if (mIndex19 == -1 || mIndex20 == -1) {
            movie.starring = "未知";
        } else {
            movie.starring = string2.substring(mIndex19 + 4, mIndex20).replaceAll("　","");
        }
        int mIndex21 = string2.indexOf("简　　介", mIndex20);
        int mIndex22 = string2.indexOf("<img", mIndex21);
        if (mIndex21 == -1||mIndex22==-1) {
            movie.introduction = "无";
        } else {
            movie.introduction = string2.substring(mIndex21 + 4, mIndex22);
        }

        return movie;
    }

    public static String toMD5(String plainText) {
        try {
            //生成实现指定摘要算法的 MessageDigest 对象。
            MessageDigest md = MessageDigest.getInstance("MD5");
            //使用指定的字节数组更新摘要。
            md.update(plainText.getBytes());
            //通过执行诸如填充之类的最终操作完成哈希计算。
            byte b[] = md.digest();
            //生成具体的md5密码到buf数组
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
//            System.out.println("32位: " + buf.toString());// 32位的加密
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
