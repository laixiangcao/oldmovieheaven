package com.jiumeng.movieheaven.parsedata;

import com.jiumeng.movieheaven.bean.MovieDao;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/31 0031.
 */
public class ParseData {

    /**
     * 获取分类电影的页数
     * @param text 该分类的链接
     * @return 页数
     */
    public static int getCategoryPages(String text) {
        int index1 = text.lastIndexOf("页</option></select>");
        int index2 = text.lastIndexOf("第", index1);
        if (index1 != -1 && index2 != -1) {
            String page = text.substring(index2 + 1, index1).trim();
            return Integer.parseInt(page);
        }
        return 500;
    }

    /**
     * 解析分类电影
     * url id name minNmae 日期 类型 评分 导演 主演
     * @param text 网页文本
     * @return 电影集合
     */
    public static ArrayList<MovieDao> parseClassifyMovie(String text) {
        String contents;
        // 截取网页主要文本文本
        ArrayList<MovieDao> movieList;
        int index1 = text.indexOf("co_content8");
        int index2 = text.indexOf("尾页");
        if (index1 != -1 && index2 != -1) {
            contents = text.substring(index1, index2);
            movieList = new ArrayList<>();
        } else {
            return null;
        }
        String[] split = contents.split("</table><table");
        for (int i = 0; i < split.length; i++) {
            String content = split[i].replaceAll("\r\n", "").replaceAll(" ", "");
            MovieDao movieDao = new MovieDao();
            // 获取url连接
            int index3 = content.indexOf("<ahref=\"");

            int index4 = content.indexOf(".html", index3);
            if (index3 != -1 && index4 != -1) {
                movieDao.url = "http://www.dy2018.com/" + content.substring(index3 + 9, index4 + 5);
                movieDao.id = textToMD5(movieDao.url);
            } else {
                movieDao.url = "";
                movieDao.id = "";
            }

            // 获取title
            int index5 = content.indexOf("title");
            int index6 = content.indexOf("\">", index5);
            if (index5 != -1 && index6 != -1) {
                movieDao.name = content.substring(index5 + 7, index6);
            } else {
                movieDao.name = "";
            }

            // 获取日期
            int index7 = content.indexOf("日期");

            if (index7 != -1) {
                movieDao.updatetime = content.substring(index7 + 3, index7 + 13);
            } else {
                movieDao.updatetime = "";
            }
            // 获取评分
            int index8 = content.indexOf("评分");
            if (index8 != -1) {
                movieDao.grade = content.substring(index8 + 3, index8 + 6).replace("</", "");
            } else {
                movieDao.grade = "";
            }

            // 获取片名
            int index9 = content.indexOf("片名");
            int index10 = content.indexOf("◎", index9);
            if (index9 != -1 && index10 != -1) {
                movieDao.minName = content.substring(index9 + 3, index10);
            } else {
                movieDao.minName = "";
            }
            // 获取导演
            int index11 = content.indexOf("导演",index7);
            int index12 = content.indexOf("</p>", index11);
            if (index11 != -1 && index12 != -1) {
                movieDao.director = content.substring(index11 + 3, index12);
            } else {
                movieDao.director = "";
            }
            // 获取类型
            int index13 = content.indexOf("类型");
            int index14 = content.indexOf("◎", index13);
            if (index13 != -1 && index14 != -1) {
                movieDao.category = content.substring(index13 + 3, index14);
            } else {
                movieDao.category = "";
            }
            // 获取主演
            int index15 = content.indexOf("主演");
            int index16 = content.indexOf("</p>", index15);
            if (index15 != -1 && index16 != -1) {
                movieDao.starring = content.substring(index15 + 3, index16);
            } else {
                movieDao.starring = "";
            }
            movieList.add(movieDao);
        }
        return movieList;
    }

    /**
     * 解析新片精品和必看热片
     * url id name minNmae 日期 年代 类型 评分 国家 语言 字幕 上映日期
     * @param text
     * @return
     */
    public static ArrayList<MovieDao> parseNewAndHotMovie(String text) {
        String contents;
        // 截取网页主要文本文本
        ArrayList<MovieDao> movieList;
        int index1 = text.indexOf("co_content8");
        int index2 = text.indexOf("尾页");
        if (index1 != -1 && index2 != -1) {
            contents = text.substring(index1, index2);
            movieList = new ArrayList<>();
        } else {
            return null;
        }
        String[] split = contents.split("</table><table");
        for (int i = 0; i < split.length; i++) {
            String content = split[i].replaceAll("\r\n", "").replaceAll(" ", "").replaceAll("&nbsp;&nbsp;", "　").replaceAll("　", "");
            MovieDao movieDao = new MovieDao();
            // 获取url连接
            int index3 = content.indexOf("<ahref=\"");
            int index4 = content.indexOf(".html", index3);
            if (index3 != -1 && index4 != -1) {
                movieDao.url = "http://www.dy2018.com/" + content.substring(index3 + 9, index4 + 5);
                movieDao.id = textToMD5(movieDao.url);
            } else {
                movieDao.url = "";
                movieDao.id = "";
            }

            // 获取title
            int index5 = content.indexOf("title");
            int index6 = content.indexOf("\">", index5);
            if (index5 != -1 && index6 != -1) {
                movieDao.name = content.substring(index5 + 7, index6);
            } else {
                movieDao.name = "";
            }

            // 获取日期
            int index7 = content.indexOf("日期");

            if (index7 != -1) {
                movieDao.updatetime = content.substring(index7 + 3, index7 + 13);
            } else {
                movieDao.updatetime = "";
            }
            // 获取评分
            int index8 = content.indexOf("◎豆瓣评分");
            if (index8 == -1) {
                index8 = content.indexOf("◎IMDB评分");
            }
            if (index8 == -1) {
                index8 = content.indexOf("◎IMDb评分");
            }
            if (index8 == -1) {
                index8 = content.indexOf("◎IMDB分数");
            }
            int index8_1=content.indexOf("/",index8);
            if (index8 != -1) {
                movieDao.grade = content.substring(index8 + 5, index8_1).replace("评分", "").replace("Ratings", "");
                if(movieDao.grade.length()>4)movieDao.grade="未知";
            } else {
                movieDao.grade = "未知";
            }

            // 获取片名
            int index9 = movieDao.name.indexOf("《");
            int index10 = movieDao.name.indexOf("》", index9);
            if (index9 != -1 && index10 != -1) {
                movieDao.minName = movieDao.name.substring(index9 + 1, index10);
            } else {
                movieDao.minName = "";
            }
            // 获取年代
            int index11 = content.indexOf("年代");
            int index12 = content.indexOf("◎", index11);
            if (index11 != -1 && index12 != -1) {
                movieDao.years = content.substring(index11 + 2, index12);
            } else {
                movieDao.years = "";
            }
            // 获取类型
            int index13 = content.indexOf("类别");
            if (index13 == -1) {
                index13 = content.indexOf("分类");
            }
            int index14 = -1;
            if (index13 != -1) {
                index14 = content.indexOf("◎", index13);
            }

            if (index13 != -1 && index14 != -1) {
                movieDao.category = content.substring(index13 + 2, index14);
            } else {
                movieDao.category = "";
            }
            // 获取国家
            int index15 = content.indexOf("国家");
            if (index15 == -1) {
                index15 = content.indexOf("国别");
            }
            int index16=-1;
            if(index15!=-1) index16 = content.indexOf("◎", index15);
            if (index15 != -1 && index16 != -1) {
                movieDao.country = content.substring(index15+2, index16);
            } else {
                movieDao.country = "";
            }
            int index17 = content.indexOf("语言");
            int index18 = content.indexOf("◎", index17);
            if (index17 != -1 && index18 != -1) {
                movieDao.language = content.substring(index17 + 2, index18);
            } else {
                movieDao.language = "";
            }
            int index19 = content.indexOf("字幕",index7);
            int index20 = content.indexOf("◎", index19);
            if (index19 != -1 && index20 != -1) {
                movieDao.subtitle = content.substring(index19+2, index20);
            } else {
                movieDao.subtitle = "";
            }
            int index21 = content.indexOf("上映日期");
            int index22 = content.indexOf("◎", index21);
            if (index21 != -1 && index22 != -1) {
                movieDao.release = content.substring(index21 + 4, index22);
            } else {
                movieDao.release = "";
            }
            movieList.add(movieDao);
        }
        return movieList;
    }

    //如果是最新或者最热模块  需要获取的字段： 海报图 详情图 下载链接 片长 导演 主演 文件大小 简介
    //如果是分类电影模块 除了上面的还需要 ：年代  国家  语言 字幕 上映日期
    // 截取网页主要文本文本
    public static MovieDao parseMovieDetails(String text,boolean isClassify){
        String contents;
        MovieDao movie;
        int index1 = text.indexOf("Content Start");
        int index2 = text.indexOf("</script>",index1);
        if (index1 != -1 && index2 != -1) {
            contents =  text.substring(index1, index2).replaceAll("　", "")
                    .replaceAll("</strong></span>", "").replaceAll("&ldquo;", "\"")
                    .replaceAll("<span style=\"COLOR: red\"><strong>", "").replaceAll("&rdquo;", "\"")
                    .replaceAll("&mdash;", "—").replaceAll("&middot;", "·")
                    .replaceAll("&hellip;", "...").replaceAll("&nbsp;", " ").replaceAll("<br />","\n")
                    .replaceAll("</p>","").replaceAll("<p>","").replaceAll("</span>", "")
                    .replace("<span style=\"color:rgb(0,0,0);\">", "")
                    .replace("<span style=\"font-size: 14px;\">", "")
                    .replace("<span style=\"color: #000000\">", "")
                    .replace("<span style=\"color: rgb(0, 0, 0);\">", "")
                    .replaceAll("&bull;", "•");
            movie = new MovieDao();
        } else {
            return null;
        }
        //获取图片
        String teg2 = "src=\"http+://[^\\s]+(jpg|png)";
        Pattern p2 = Pattern.compile(teg2,Pattern.MULTILINE);
        Matcher m2 = p2.matcher(contents);
        movie.jpgList = new ArrayList<String>();
        while (m2.find()) {
            movie.jpgList.add(m2.group().replace("src=\"", ""));
        }
        //获取电影的下载地址
        String teg = "(>ftp://)+[^\\s]+(rmvb|mkv|mp4)";
        Pattern p = Pattern.compile(teg,Pattern.MULTILINE);
        Matcher m = p.matcher(contents);
        movie.downlist = new ArrayList<String>();
        while (m.find()) {
            movie.downlist.add(m.group().replace(">", ""));
        }

        if (isClassify){
            //年代
            int idx01 = contents.indexOf("年代");
            int idx02 = contents.indexOf("◎", idx01);
            if (idx01 == -1 || idx02 == -1) {
                movie.years = "";
            } else {
                movie.years = contents.substring(idx01 + 2, idx02);
            }

            // 获取国家
            int idx03 = contents.indexOf("国家");
            if (idx03 == -1) {
                idx03 = contents.indexOf("国别");
            }
            int idx04=-1;
            if(idx03!=-1) idx04 = contents.indexOf("◎", idx03);
            if (idx03 != -1 && idx04 != -1) {
                movie.country = contents.substring(idx03+2, idx04);
            } else {
                movie.country = "";
            }

            //语言
            int idx05 = contents.indexOf("语言");
            int idx06 = contents.indexOf("◎", idx05);
            if (idx05 == -1 || idx06 == -1) {
                movie.language = "";
            } else {
                movie.language = contents.substring(idx05 + 2, idx06);
            }

            //字幕
            int idx07 = contents.indexOf("字幕");
            int idx08 = contents.indexOf("◎", idx07);
            if (idx07 == -1 || idx08 == -1) {
                movie.subtitle = "";
            } else {
                movie.subtitle = contents.substring(idx07 + 2, idx08);
            }

            //上映日期
            int idx09 = contents.indexOf("上映日期");
            int idx10 = contents.indexOf("◎", idx09);
            if (idx09 == -1 || idx10 == -1) {
                movie.release = "";
            } else {
                movie.release = contents.substring(idx09 + 4, idx10);
            }
        }

        //文件大小
        int mIndex3 = contents.indexOf("文件大小");
        int mIndex4 = contents.indexOf("◎", mIndex3);
        if (mIndex3 == -1 || mIndex4 == -1) {
            movie.filesize = "";
        } else {
            movie.filesize = contents.substring(mIndex3 + 4, mIndex4);
        }
        //片长
        int mIndex5 = contents.indexOf("片长");
        int mIndex6 = contents.indexOf("◎", mIndex5);
        if (mIndex5 == -1 || mIndex6 == -1) {
            movie.play_time = "";
        } else {
            movie.play_time = contents.substring(mIndex5 + 2, mIndex6);
        }

        //导演
        int mIndex7 = contents.indexOf("导演",mIndex5);
        int mIndex8 = contents.indexOf("◎", mIndex7);
        if (mIndex7 == -1 || mIndex8 == -1) {
            movie.director = "";
        } else {
            movie.director = contents.substring(mIndex7 + 2, mIndex8);
        }
        //主演
        int mIndex9 = contents.indexOf("主演");
        int mIndex10 = contents.indexOf("◎", mIndex9);
        if (mIndex9 == -1 || mIndex10 == -1) {
            movie.starring = "";
        } else {
            movie.starring = contents.substring(mIndex9 + 2, mIndex10).replaceAll("  ", "");
        }
        //简介
        int mIndex11 = contents.indexOf("简介");
        int mIndex12 = contents.indexOf("◎", mIndex11);
        if (mIndex12==-1){
            mIndex12 = contents.indexOf("<img", mIndex11);
        }
        if (mIndex11 == -1 || mIndex12 == -1) {
            movie.introduction = "";
        } else {
            movie.introduction = contents.substring(mIndex11 + 2, mIndex12);
        }
        return movie;
    }

    private static String textToMD5(String plainText) {
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
