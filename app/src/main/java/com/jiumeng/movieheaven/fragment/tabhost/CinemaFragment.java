package com.jiumeng.movieheaven.fragment.tabhost;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiumeng.movieheaven.base.BaseFragment;
import com.jiumeng.movieheaven.bean.MovieDao;
import com.jiumeng.movieheaven.network.NetWorkApi;
import com.jiumeng.movieheaven.utils.UIUtils;
import com.jiumeng.movieheaven.views.LoadingPage;
import com.jiumeng.movieheaven.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * 最新院线大片页面
 * Created by Administrator on 2016/7/14 0014.
 */
public class CinemaFragment extends BaseFragment {


    private ArrayList<MovieDao> mMovieData;

    @Override
    public void requestNetData() {
        NetWorkApi.getMovieList(NetWorkApi.MVOIETYPE_CINEMAMOVIE,1,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    mMovieData = jsonToList(new String(responseBody, "GBK"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                loadDataComplete(LoadingPage.ResultState.STATE_SUCCESS);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

        });
    }

    @Override
    public View onCreateSuccessView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_demo, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CinemaAdapter(mMovieData));
        return view;
    }


    class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.CinemaHolder>{

        private ArrayList<MovieDao> movieData;

        public CinemaAdapter(ArrayList<MovieDao> movieData){
            this.movieData=movieData;
        }

        @Override
        public CinemaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CinemaHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_cinema,parent,false));
        }

        @Override
        public void onBindViewHolder(CinemaHolder holder, int position) {
            MovieDao movieDao = movieData.get(position);
            ImageLoader.getInstance().displayImage(movieDao.jpgList.get(0),holder.iv_poster, UIUtils.getDisplayImageOptions());
            holder.tv_title.setText(movieDao.name);
            holder.tv_release.setText(movieDao.release);
        }

        @Override
        public int getItemCount() {
            return movieData.size();
        }

        class CinemaHolder extends RecyclerView.ViewHolder{

            ImageView iv_poster;
            TextView tv_title;
            TextView tv_release;

            public CinemaHolder(View itemView) {
                super(itemView);
                tv_title= (TextView) itemView.findViewById(R.id.tv_title);
                tv_release= (TextView) itemView.findViewById(R.id.tv_release);
                iv_poster= (ImageView) itemView.findViewById(R.id.iv_pic);
            }
        }
    }

    private ArrayList<MovieDao> jsonToList(String response){
        ArrayList<MovieDao> mMovieData = null;
        try {
            mMovieData = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray movielist = jsonObject.getJSONArray("movielist");
            for (int i = 0; i < movielist.length(); i++) {
                JSONObject jsonObject1 = movielist.getJSONObject(i).getJSONObject("MovieDao");
                MovieDao movieDao = new MovieDao();
                movieDao.name = jsonObject1.getString("name");
                movieDao.introduction = jsonObject1.getString("introduction");
                movieDao.country = jsonObject1.getString("country");
                movieDao.subtitle = jsonObject1.getString("subtitle");
                movieDao.play_time = jsonObject1.getString("play_time");
                movieDao.language = jsonObject1.getString("language");
                movieDao.category = jsonObject1.getString("category");
                movieDao.years = jsonObject1.getString("years");
                movieDao.starring=jsonObject1.getString("starring");
                movieDao.director = jsonObject1.getString("director");
                movieDao.filesize = jsonObject1.getString("filesize");
                movieDao.grade = jsonObject1.getString("grade");
                movieDao.release = jsonObject1.getString("release");
                movieDao.updatetime = jsonObject1.getString("updatetime");
                movieDao.other = jsonObject1.getString("download");
                JSONArray jpglist = jsonObject1.getJSONArray("jpglist");
                movieDao.jpgList=new ArrayList<>();
                for (int j=0;j<jpglist.length();j++){
                    movieDao.jpgList.add(jpglist.getString(j));
                }
                mMovieData.add(movieDao);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mMovieData;
    }
}
