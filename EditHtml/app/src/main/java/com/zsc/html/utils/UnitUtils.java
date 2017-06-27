package com.zsc.html.utils;

import android.content.Context;

/**
 * Created by zhengshichao1 on 2015/10/27.
 */
public class UnitUtils {

    public static int dip2px(Context context, float dpValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } else {
            return 0;
        }
    }

    public static int px2dip(Context context, float pxValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        } else {
            return 0;
        }
    }

    public static int sp2px(Context context, float spValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (spValue * scale + 0.5f);
        } else {
            return 0;
        }
    }

    public static int px2sp(Context context, float pxValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (pxValue / scale + 0.5f);
        } else {
            return 0;
        }
    }

}
