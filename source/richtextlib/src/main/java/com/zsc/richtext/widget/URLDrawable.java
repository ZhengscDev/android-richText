package com.zsc.richtext.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

/**
 * 用于填充富文本中的图片
 */
public class URLDrawable extends BitmapDrawable {
    protected Bitmap bitmap;

    @Override
    public void draw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, getPaint());
        }
    }
}