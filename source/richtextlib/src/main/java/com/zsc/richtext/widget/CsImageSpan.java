package com.zsc.richtext.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.style.ImageSpan;

import java.io.File;

/**
 * 自定义 ImageSpan 主要用去区分网络获取的还是本地新添加的
 * Created by zhengshichao1 on 2016/10/11.
 */

public class CsImageSpan extends ImageSpan {

    /**
     * 设置file 对象
     */
    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public CsImageSpan(Context context, Bitmap b) {
        super(context, b);
    }
}
