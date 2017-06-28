package com.zsc.richtext.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zsc.richtext.utils.HtmlUtils;
import com.zsc.richtext.utils.LogUtils;


/**
 * 获取网络图片
 * <p>
 * Created by zhengshichao1 on 2016/10/13.
 */

public class HttpImageGetter implements Html.ImageGetter {

    private static final String TAG = "HttpImageGetter";

    private Context mContext;
    private EditText mEditText;

    public HttpImageGetter(Context mContext, EditText editText) {
        this.mContext = mContext;
        this.mEditText = editText;
        Glide.get(mContext).clearMemory();
    }

    @Override
    public Drawable getDrawable(String source) {
        LogUtils.d(TAG, "getDrawable----------->" + source);
        final URLDrawable urlDrawable = new URLDrawable();
        Glide.with(mContext)
                .load(source)
                .asBitmap()   //强制转换Bitmap
                .into(new CSimpleTarget(urlDrawable));
        return urlDrawable;
    }

    /**
     * 自定义一个Target,用于填充drawable
     */
    class CSimpleTarget extends SimpleTarget<Bitmap> {

        private URLDrawable drawable;

        public CSimpleTarget(URLDrawable drawable) {
            this.drawable = drawable;
        }

        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            int paddingLR = HtmlUtils.getPaddingLR(mContext);
            drawable.bitmap = HtmlUtils.getScalBitmap(mContext, bitmap, paddingLR);
            if (drawable.bitmap != null) {
                drawable.setBounds(paddingLR, 0, paddingLR + drawable.bitmap.getWidth(), drawable.bitmap.getHeight());
                // 如果不设置的话，图片显示不出来，已验证，重新设置之后，getDrawable()方法不会再次调用
                mEditText.setText(mEditText.getText()); //不设置的话图片显示不出来
            }
        }
    }
}
