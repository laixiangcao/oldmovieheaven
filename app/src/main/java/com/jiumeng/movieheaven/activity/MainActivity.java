package com.jiumeng.movieheaven.activity;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.jiumeng.movieheaven.bean.MainTab;
import com.jiumeng.movieheaven.network.AsyncHttpClientApi;
import com.jiumeng.movieheaven.utils.UIUtils;
import com.jiumeng.movieheaven.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, TabHost.OnTabChangeListener {

    private FragmentTabHost mTabHost;
    private String mTitle;
    private long mBackPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setElevation(0);
            actionBar.setTitle(mTitle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        restoreActionBar();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search:
                Intent mIntent = new Intent(this, EmptyActivity.class);
                mIntent.putExtra("fragmentType",EmptyActivity.FRAGMENTTYPE_SEARCH);
                startActivity(mIntent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mTitle = UIUtils.getString(R.string.main_tab__name_home);
        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }
        initTabs();
        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);
    }

    private void initTabs() {

        MainTab[] tabs = MainTab.values();
        int size = tabs.length;
        for (int i = 0; i < size; i++) {
            View indicator = View.inflate(this, R.layout.tab_indicator, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            ImageView icon = (ImageView) indicator.findViewById(R.id.iv_icon);
            MainTab mainTab = tabs[i];
            TabHost.TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
            Drawable drawable = this.getResources().getDrawable(mainTab.getResIcon());
            icon.setImageDrawable(drawable);
            title.setText(getString(mainTab.getResName()));
            tab.setIndicator(indicator);
            tab.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });
            mTabHost.addTab(tab, mainTab.getClz(), null);
            mTabHost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onTabChanged(String tabId) {
        if (tabId.equals("综合")){
            tabId="电影天堂";
        }
        mTitle = tabId;
        supportInvalidateOptionsMenu();
    }


    /**
     * 双击返回退出
     */
    public void onBackPressed() {
        long curTime = SystemClock.uptimeMillis();
        if ((curTime - mBackPressedTime) < (3 * 1000)) {
            finish();
        } else {
            mBackPressedTime = curTime;
            Toast.makeText(this, "再次点击退出电影天堂", Toast.LENGTH_SHORT).show();
        }
    }
    public String search2() throws Exception {
        URL url=new URL("http://www.dy2018.com/e/search/index.php");
        HttpURLConnection conn=(HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(6000);
        conn.setReadTimeout(6000);

        //添加post请求头中自动添加的属性
        //流里的数据的mimetype
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  //这句可不写
        String content="classid=0&show=title%2Csmalltext&tempid=1&keyboard=%BA%AE%D5%BD&Submit=%C1%A2%BC%B4%CB%D1%CB%F7";
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
        return "无";
    }

    public void search() throws UnsupportedEncodingException {
        RequestParams params = new RequestParams();

//        String a="show=title,smalltext&tempid=1&keyboard=寒战&Submit=立即搜索";
//        String encode = URLEncoder.encode(a);
        params.put("show",URLEncoder.encode("title,smalltext","UTF-8"));
        params.put("tempid",URLEncoder.encode("1","UTF-8"));
        params.put("keyboard", URLEncoder.encode("2012","UTF-8"));
        params.put("Submit",URLEncoder.encode("立即搜索","UTF-8"));
        AsyncHttpClient httpClient = AsyncHttpClientApi.getInstance();
        httpClient.post("http://www.dy2018.com/e/search/index.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String content = null;
                try {
                    content = new String(responseBody, "GBK");
                    int b = content.indexOf("寒战II");
                    Log.e("aaa",b+content);
                    Toast.makeText(getApplicationContext(),b+content,Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
