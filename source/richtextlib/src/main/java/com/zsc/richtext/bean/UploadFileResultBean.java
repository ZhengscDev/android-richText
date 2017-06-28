package com.zsc.richtext.bean;

import java.util.ArrayList;

/**
 * 上传图片返回的结果
 * Created by zhengshichao1 on 2016/10/11.
 */
public class UploadFileResultBean{

    public DataBean data;

    public class DataBean{
        public ArrayList<FileResult> file;
        public String state;

        public class FileResult {
            public String originalName;
            public String type;
            public String url;
            public String size;
        }
    }
}
