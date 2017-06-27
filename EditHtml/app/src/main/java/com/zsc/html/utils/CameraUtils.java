package com.zsc.html.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.DecimalFormat;

/**
 * CameraUtils
 * Created by zhengshichao1 on 2015/11/16.
 */
public class CameraUtils {

    /**
     * 根据提供的宽高获取缩放的图片
     * @param path
     * @param resultW
     * @param resultH
     * @return
     */
    public static Bitmap getCompressBitmap(String path, float resultW, float resultH) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int width = options.outWidth;
        int height = options.outHeight;
        float zoomRatio = 1;
        if (width >= height && width > resultW) {
            zoomRatio = width / resultW;
            zoomRatio = Integer.valueOf(getDecimal(zoomRatio, "0"));
            //如果zoomRatio不是2的倍数，则向上加1，增大缩放效果
            if (zoomRatio % 2 != 0) {
                zoomRatio += 1;
            }
        } else if (width <= height && height > resultH) {
            zoomRatio = height / resultH;
            zoomRatio = Integer.valueOf(getDecimal(zoomRatio, "0"));
            //如果zoomRatio不是2的倍数，则向上加1，增大缩放效果
            if (zoomRatio % 2 != 0) {
                zoomRatio += 1;
            }
        }
        if (zoomRatio < 0) {
            zoomRatio = 1;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) zoomRatio;
        LogUtils.d("CameraUtils", "----------->>" + options.inSampleSize);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(path, options);
        } catch (Error e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String getDecimal(float value, String decimal) {
        DecimalFormat df = new DecimalFormat(decimal);
        return df.format(value);
    }

}
