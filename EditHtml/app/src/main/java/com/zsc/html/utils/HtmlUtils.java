package com.zsc.html.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zsc.html.bean.UploadFileResultBean;
import com.zsc.html.widget.CsImageSpan;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 操作html 标签
 * <p>
 * Created by zhengshichao1 on 2016/10/12.
 */

public class HtmlUtils {

    private static final String TAG = "HtmlUtils";

    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式


    /**
     * @param htmlStr
     * @return 删除Html标签
     */
    public static String delHTMLTag(String htmlStr) {
        if (CustomTextUtils.isEmpty(htmlStr)) {
            return "";
        }
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签
        return htmlStr.trim(); // 返回文本字符串
    }

    public static String getTextFromHtml(String htmlStr) {
        htmlStr = delHTMLTag(htmlStr);
        htmlStr = htmlStr.replaceAll("&nbsp;", " ");
        htmlStr = htmlStr.substring(0, htmlStr.indexOf("。") + 1);
        return htmlStr;
    }

    public static void main(String[] args) {
        String str = "<div style='text-align:center;'> 整治“四风”   清弊除垢<br/><span style='font-size:14px;'> </span><span style='font-size:18px;'>公司召开党的群众路线教育实践活动动员大会。</span><br/></div>";
        System.out.println(getTextFromHtml(str));
    }

    /**
     * 添加图片
     *
     * @param context
     * @param imgPath  图片路径
     * @param editText
     */
    public static void appendImage(Context context, String imgPath, EditText editText) {
        try {
            if (CustomTextUtils.isEmpty(imgPath)) {
                return;
            }
            File file = new File(imgPath);
            if (!file.exists()) {
                return;
            }
            SpannableString sp = new SpannableString("1");
            Bitmap bitmap = HtmlUtils.getLocalBitmap(context, imgPath);
            if (bitmap == null) {
                return;
            }
            CsImageSpan csImageSpan = new CsImageSpan(context, bitmap);
            //设置file对象
            csImageSpan.setFile(file);
            sp.setSpan(csImageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //做插入和删除操作
            SpannableStringBuilder builder;
            int nextLineCount = 0;
            int start = editText.getSelectionStart();
            if (start > 0 && start <= editText.length()) {
                builder = new SpannableStringBuilder(editText.getText().subSequence(0, start));
                //判断前置换行是否需要 ，如果当前坐标的位置为0 则不需要前置换行
                Character startC = editText.getText().charAt(start - 1);
                if (!"\n".equals(startC.toString())) {
                    builder.append("\n");
                    nextLineCount++;
                }
                //拼接图片
                builder.append(sp);
                //判断后置图片后置换行是否需要，如果当前位置的后一个字符不是换行符，则需拼接一个后置换行
                if (editText.getText().length() > start) {
                    Character endC = editText.getText().charAt(start);
                    if (!"\n".equals(endC.toString())) {
                        builder.append("\n");
                    }
                } else {
                    builder.append("\n");
                }
                nextLineCount++;
                //拼接图片后面的内容
                builder.append(editText.getText().subSequence(start, editText.length()));
                editText.setText(builder);
                editText.setSelection(start + nextLineCount + sp.length());
            } else {
                builder = new SpannableStringBuilder();
                builder.append(sp);
                if (start <= 0) {
                    builder.append("\n");
                    nextLineCount++;
                }
                builder.append(editText.getText());
                editText.setText(builder);
                editText.setSelection((start < 0 ? 0 : start) + nextLineCount + sp.length());
            }
        } catch (Exception e) {
            if (AppConfig.isDebug) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加股票链接
     *
     * @param context
     * @param etView
     * @param stocklink
     */
    public static void appendText(final Context context, EditText etView, final String stocklink) {
        try {
            if (CustomTextUtils.isEmpty(stocklink)) {
                return;
            }
            SpannableString sp = new SpannableString(stocklink);
            sp.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(context, stocklink + " 被点击", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.parseColor("#089fe6"));
                    ds.setUnderlineText(false);
                }
            }, 0, stocklink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //插入文字
            int start = etView.getSelectionStart();
            if (start >= 0 && etView.length() > start) {
                SpannableStringBuilder builder = new SpannableStringBuilder(etView.getText().subSequence(0, start));
                builder.append(sp).append(etView.getText().subSequence(start, etView.length()));
                etView.setText(builder);
                etView.setSelection(start + sp.length());
            } else {
                etView.append(sp);
                etView.setSelection((start < 0 ? 0 : start) + sp.length());
            }
        } catch (Exception e) {
            if (AppConfig.isDebug) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将内容中的空格替换成html的转义字符&nbsp; <p/>
     * NOTE： img标签内容中的空格不会被转换
     *
     * @param value 要转换的字符串
     * @return
     */
    public static String replaceSpaceToHtmlChar(String value) {
        if (CustomTextUtils.isEmpty(value)) {
            return "";
        }
        String imgTagReg = "(<img[\\s]+src=([\\S]+?)[\\s]+_src=([\\S]+?)>)";
        Pattern pattern = Pattern.compile(imgTagReg);
        Matcher matcher = pattern.matcher(value);
        int recordIndex = 0;
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String groupStr = matcher.group();
            int start = matcher.start();
            String str = value.substring(recordIndex, start);
            LogUtils.d(TAG, "-------截取的内容---------->" + str);
            if (!TextUtils.isEmpty(str)) {
                sb.append(str.replaceAll(" ", "&nbsp;"));
            }
            sb.append(groupStr);
            recordIndex = matcher.end();
        }
        String str = value.substring(recordIndex, value.length());
        if (!TextUtils.isEmpty(str)) {
            sb.append(str.replaceAll(" ", "&nbsp;"));
        }
        LogUtils.d(TAG, "---replaceSpaceToHtmlChar------->>" + sb.toString());
        return sb.toString();
    }

    /**
     * 将股票描述转换成a标签 <p/>
     * <p>
     * 例如：$中毅达B(SH900906)$ 转换成 <a target="_blank" href="/SH900906">$中毅达B(SH900906)$</a>
     *
     * @param value
     * @return
     */
    //服务端现在已经做了处理，此方法暂时没用
    @Deprecated
    private static String addTextHyperLink(String value) {
        if (CustomTextUtils.isEmpty(value)) {
            return "";
        }
        String patternStr = "(\\$(\\w|[\\u4e00-\\u9fa5]|[-]|[\\*])*\\(SH.*?\\$)|(\\$(\\w|[\\u4e00-\\u9fa5]|[-]|[\\*])*\\(SZ.*?\\$)";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(value);
        int recordIndex = 0;
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String stockDesc = matcher.group();
            int start = matcher.start();
            String str = value.substring(recordIndex, start);
            if (!TextUtils.isEmpty(str)) {
                sb.append(str);
            }
            //获取代码code
            String stockCode = stockDesc.substring(stockDesc.indexOf("(") + 1, stockDesc.indexOf(")"));
            //拼接超链接html tag
            sb.append("<a target=\"_blank\" href=\"/").append(stockCode).append("\">").append(stockDesc)
                    .append("</a>");
            recordIndex = matcher.end();
        }
        String str = value.substring(recordIndex, value.length());
        if (!TextUtils.isEmpty(str)) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 构建Html 将换行替换成 <br/>
     *
     * @return
     */
    public static String buildHtmlStr(String content) {
        try {
            if (CustomTextUtils.isEmpty(content)) {
                return "";
            }
            //将字符串的空格转换成&nbsp;
            content = replaceSpaceToHtmlChar(content);
            StringBuilder sb = new StringBuilder();
            Pattern pattern = Pattern.compile("\n");
            Matcher matcher = pattern.matcher(content);
            int recordIndex = 0;
            while (matcher.find()) {
                int start = matcher.start();
                String str = content.substring(recordIndex, start);
                LogUtils.d(TAG, "-------截取的内容---------->" + str);
                if (!TextUtils.isEmpty(str)) {
                    sb.append("<p>").append(str).append("</p>");
                }
                recordIndex = matcher.end();
            }
            String str = content.substring(recordIndex, content.length());
            if (!TextUtils.isEmpty(str)) {
                sb.append("<p>").append(str).append("</p>");
            }
            LogUtils.d(TAG, "---buildHtmlStr------->>" + sb.toString());
            return sb.toString();
        } catch (Exception e) {
            if (AppConfig.isDebug) {
                e.printStackTrace();
            }
        }
        return CustomTextUtils.checkEmpty(content, "");
    }

    /**
     * 找出ImageSpan 并替换成<img>标签
     *
     * @param result
     * @param editable
     * @return
     */
    public static String buildLocalImagTag(UploadFileResultBean result, Editable editable) {
        return buildLocalImagTag(result, editable, false);
    }

    /**
     * 找出ImageSpan 并替换成<img>标签
     *
     * @param result
     * @param editable
     * @param mergeSpan 是否合并本地和网络ImageSpan
     * @return
     */
    public static String buildLocalImagTag(UploadFileResultBean result, Editable editable, boolean mergeSpan) {
        if (TextUtils.isEmpty(editable)) {
            return "";
        }
        try {
            SpannableStringBuilder builder = new SpannableStringBuilder(editable);
            if (result != null && result.data != null && result.data.file != null && !result.data.file.isEmpty()) {
                CsImageSpan[] spans = builder.getSpans(0, builder.length(), CsImageSpan.class);
                for (int i = 0; i < spans.length; i++) {
                    CsImageSpan indexSpan = spans[i];
                    int start = builder.getSpanStart(indexSpan);
                    int end = builder.getSpanEnd(indexSpan);
                    Iterator<UploadFileResultBean.DataBean.FileResult> iterator = result.data.file.iterator();
                    while (iterator.hasNext()) {
                        UploadFileResultBean.DataBean.FileResult bean = iterator.next();
                        if (!CustomTextUtils.isEmpty(bean.originalName) && bean.originalName.equals(indexSpan.getFile() != null ? indexSpan.getFile().getName() : "")) {
                            builder.replace(start, end, getImgTagsByUrl(bean.url));
                            iterator.remove();
                        }
                    }
                    //删除 不用的CsImageSpan
                    builder.removeSpan(indexSpan);
                }
            }
            if (mergeSpan) {
                return buildNetworkImagTag(builder);
            }
            LogUtils.d(TAG, "-----buildLocalImagTag----------->>" + builder);
            return builder.toString();
        } catch (Exception e) {
            if (AppConfig.isDebug) {
                e.printStackTrace();
            }
        }
        return editable.toString();
    }

    /**
     * 找出ImageSpan 并替换成<img>标签
     *
     * @param editable
     * @return
     */
    public static String buildNetworkImagTag(Editable editable) {
        if (TextUtils.isEmpty(editable)) {
            return "";
        }
        try {
            SpannableStringBuilder builder = new SpannableStringBuilder(editable);
            ImageSpan[] spans = builder.getSpans(0, builder.length(), ImageSpan.class);
            if (hasImageSpan(spans)) {
                for (int i = 0; i < spans.length; i++) {
                    ImageSpan indexSpan = spans[i];
                    int start = builder.getSpanStart(indexSpan);
                    int end = builder.getSpanEnd(indexSpan);
                    builder.replace(start, end, getImgTagsByUrl(indexSpan.getSource()));
                }
                LogUtils.d(TAG, "-----buildNetworkImagTag----------->>" + builder);
            }
            return builder.toString();
        } catch (Exception e) {
            if (AppConfig.isDebug) {
                e.printStackTrace();
            }
        }
        return editable.toString();
    }

    /**
     * 获取ImageSpan[] 数组
     *
     * @param editable
     * @return
     */
    public static ImageSpan[] getImageSpans(Editable editable) {
        if (!TextUtils.isEmpty(editable)) {
            return editable.getSpans(0, editable.length(), ImageSpan.class);
        }
        return null;
    }

    /**
     * 获取CsImageSpan[] 数组  只有本地新建的ImageSpan才会用到CsImageSpan
     *
     * @param editable
     * @return
     */
    public static CsImageSpan[] getCsImageSpans(Editable editable) {
        if (!TextUtils.isEmpty(editable)) {
            return editable.getSpans(0, editable.length(), CsImageSpan.class);
        }
        return null;
    }

    /**
     * 判断是否存在ImageSpan
     *
     * @param editable 传入 Editable 对象
     * @return
     */
    public static boolean hasImageSpan(Editable editable) {
        if (!TextUtils.isEmpty(editable)) {
            ImageSpan[] spans = getImageSpans(editable);
            if (spans != null && spans.length > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否存在CsImageSpan 只有本地新建的ImageSpan才会用到CsImageSpan
     *
     * @param editable 传入 Editable 对象
     * @return
     */
    public static boolean hasCsImageSpan(Editable editable) {
        if (!TextUtils.isEmpty(editable)) {
            CsImageSpan[] spans = getCsImageSpans(editable);
            if (spans != null && spans.length > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否存在CsImageSpan 只有本地新建的ImageSpan才会用到CsImageSpan
     *
     * @param spans 传入 ImageSpan[] 数组
     * @return
     */
    public static boolean hasCsImageSpan(CsImageSpan[] spans) {
        if (spans != null && spans.length > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否存在ImageSpan
     *
     * @param spans 传入 ImageSpan[] 数组
     * @return
     */
    public static boolean hasImageSpan(ImageSpan[] spans) {
        if (spans != null && spans.length > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取img标签
     *
     * @param url
     * @return
     */
    private static String getImgTagsByUrl(String url) {
        if (CustomTextUtils.isEmpty(url)) {
            return "";
        }
        StringBuilder sb = new StringBuilder("<img src=\"");
        sb.append(url).append("\" _src=\"").append(url).append("\">");
        LogUtils.d(TAG, "------getImgTagsByUrl------>" + sb.toString());
        return sb.toString();
    }

    /**
     * 根据屏幕显示图片 缩放或者放大图片
     *
     * @param context
     * @param bitmap
     * @param paddingLR 左右间距
     * @return
     */
    public static Bitmap getScalBitmap(Context context, Bitmap bitmap, int paddingLR) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int targetWidth = dm.widthPixels - paddingLR * 2;
        LogUtils.d(TAG, "----targetWidth---->>" + targetWidth);
        float scale = targetWidth / (float) width;
        LogUtils.d(TAG, "----scale---->>" + scale);
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        bitmap = null;
        return newBitmap;
    }


    /**
     * 根据本地的路劲生成适合屏幕的Bitmap
     *
     * @param path
     * @return
     */
    public static Bitmap getLocalBitmap(Context context, String path) {
        try {
            Bitmap bitmap = CameraUtils.getCompressBitmap(path, 480, 800);
            int paddingLR = UnitUtils.dip2px(context, 15);
            return getScalBitmap(context, bitmap, paddingLR);
        } catch (Exception e) {
            if (AppConfig.isDebug) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取图片左右两侧的宽度
     *
     * @param context
     * @return
     */
    public static int getPaddingLR(Context context) {
        return UnitUtils.dip2px(context, 15);
    }

    /**
     * 获取本地图片的地址
     *
     * @param editable
     * @return
     */
    public static ArrayList<File> getLocalImagePath(Editable editable) {
        ArrayList<File> filePathList = new ArrayList<>();
        if (editable != null) {
            CsImageSpan[] spanArray = getCsImageSpans(editable);
            if (hasCsImageSpan(spanArray)) {
                for (int i = 0; i < spanArray.length; i++) {
                    CsImageSpan imageSpan = spanArray[i];
                    if (imageSpan != null) {
                        filePathList.add(imageSpan.getFile());
                    }
                }
            }
        }
        return filePathList;
    }

    /**
     * 过滤html特殊标签
     *
     * @param str
     * @return
     */
    public static String filterHtmlTag(String str) {
        if (CustomTextUtils.isEmpty(str)) {
            return str;
        }
        Html.fromHtml(str);
        if (str.contains("<p>")) {
            str = str.replace("<p>", "").replace("</p>", "");
        }
        if (str.contains("<strong>")) {
            str = str.replace("<strong>", "").replace("</strong>", "");
        }
        if (str.contains("<b>")) {
            str = str.replace("<b>", "").replace("</b>", "");
        }
        return str;
    }
}
