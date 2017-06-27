package com.zsc.html;

import android.app.Application;

/**
 * Created by zhengshichao1 on 2017/6/27.
 */

public class MainApplication extends Application {

    private static MainApplication instance;

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
