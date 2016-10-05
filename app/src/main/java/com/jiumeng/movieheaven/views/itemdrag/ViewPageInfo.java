package com.jiumeng.movieheaven.views.itemdrag;

import android.os.Bundle;
import java.io.Serializable;


public class ViewPageInfo implements Serializable {


    public String title;
    public  String tag;
    public  Class<?> clss;
    public Bundle mBundle;

    public ViewPageInfo(String _title, String _tag, Class<?> _class, Bundle bundle) {
        title = _title;
        tag = _tag;
        clss = _class;
        mBundle=bundle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
