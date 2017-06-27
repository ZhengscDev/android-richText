package com.zsc.html.utils;

import android.text.TextUtils;

/**
 * 字符串判空，统一处理字符串判断处理
 * Created by zhengshichao1 on 2016/6/27.
 */
public class CustomTextUtils {

    /**
     * 判断是否为空
     * @param value
     * @return
     */
    public static boolean isEmpty(String value){
        if (TextUtils.isEmpty(value) || "null".equalsIgnoreCase(value)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为空，如果为空取默认值
     * @param value 要判断的值
     * @param defaultValue 默认值
     * @return
     */
    public static String checkEmpty(String value, String defaultValue){
        if (TextUtils.isEmpty(value) || "null".equalsIgnoreCase(value)) {
            return defaultValue;
        }
        return value;
    }

}
