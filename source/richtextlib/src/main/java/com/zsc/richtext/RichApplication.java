package com.zsc.richtext;

import android.app.Application;

import com.zsc.richtext.utils.AppConfig;
import com.zsc.richtext.utils.LogUtils;

/**
 * Created by zhengshichao1 on 2017/6/27.
 */

public class RichApplication extends Application {

    private static RichApplication instance;

    public static RichApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.setLogEnable(AppConfig.isDebug);
        instance = this;
    }
}
