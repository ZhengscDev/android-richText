package com.zsc.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zsc.richtext.utils.FileUtils;
import com.zsc.richtext.utils.GalleryUtils;
import com.zsc.richtext.utils.HtmlUtils;
import com.zsc.richtext.utils.LogUtils;
import com.zsc.richtext.widget.HttpImageGetter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText richEdit;
    private EditText linkTextEdit;

    // 本地图库
    public static final int SHOW_LOCAL_GALLERY = 101;
    // 拍照
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;
    // 拍照功能
    private File mCameraPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        richEdit = (EditText) findViewById(R.id.et_content);
        linkTextEdit = (EditText) findViewById(R.id.ed_link_text);
        richEdit.setMovementMethod(LinkMovementMethod.getInstance());

        initListener();
        initData();
    }


    /**
     * 其中contentBase64是Base64处理的测试数据
     */
    private void initData() {
        String contentBase64 = "PHA+PC9wPjxwPuWPkeS4gOS4que+juWlszwvcD48cD48aW1nIHNyYz0iaHR0cDovL3d3dy5xaXdlbjAwNy5jb20vaW1hZ2VzL2V4dF9qcGcvMjAxNjA4LzExXzEzNDUzMDh5LmpwZyIgX3NyYz0iaHR0cDovL3d3dy5xaXdlbjAwNy5jb20vaW1hZ2VzL2V4dF9qcGcvMjAxNjA4LzExXzEzNDUzMDh5LmpwZyI+PC9wPjxwPuWPpuS4gOS4que+juWlszwvcD48cD48L3A+PHA+PC9wPjxwPjxpbWcgc3JjPSJodHRwOi8vd3d3LnFpd2VuMDA3LmNvbS9pbWFnZXMvaW1hZ2UvMjAxNi8wNzIyLzYzNjA0NzkzNjM3ODQ3NjgyNTg3MTA1MDcuanBnIiBfc3JjPSJodHRwOi8vd3d3LnFpd2VuMDA3LmNvbS9pbWFnZXMvaW1hZ2UvMjAxNi8wNzIyLzYzNjA0NzkzNjM3ODQ3NjgyNTg3MTA1MDcuanBnIj48L3A+PHA+5pyA5ZCO5LiA5bygPC9wPjxwPjxpbWcgc3JjPSJodHRwOi8vd3d3LnFpd2VuMDA3LmNvbS9pbWFnZXMvZXh0X2pwZy8yMDE2MDgvMTFfMTM0MjU0ZzEuanBnIiBfc3JjPSJodHRwOi8vd3d3LnFpd2VuMDA3LmNvbS9pbWFnZXMvZXh0X2pwZy8yMDE2MDgvMTFfMTM0MjU0ZzEuanBnIj48L3A+PHA+PC9wPg==";
        byte[] contentBytes = Base64.decode(contentBase64.getBytes(Charset.forName("UTF-8")),Base64.DEFAULT);
        richEdit.setText(Html.fromHtml(HtmlUtils.delHTMLTag(new String(contentBytes,Charset.forName("UTF-8"))), new HttpImageGetter(this, richEdit), null));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case SHOW_LOCAL_GALLERY:
                onLocalGalleryActivityResult(data);
                break;
            case PHOTO_REQUEST_TAKEPHOTO:
                onCameraActivityResult();
                break;
        }
    }

    /**
     * init Listener
     */
    private void initListener() {
        findViewById(R.id.bt_select_local_pic).setOnClickListener(this);
        findViewById(R.id.bt_camera_pic).setOnClickListener(this);
        findViewById(R.id.bt_insert_link_text).setOnClickListener(this);
        findViewById(R.id.bt_generate_html).setOnClickListener(this);
    }

    /**
     * 启动相机
     *
     * @param activity
     * @param requestCode
     */
    private void startSystemCamera(Activity activity, int requestCode) {
        try {
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".jpg"; // 拍照添加扩展名
            mCameraPicture = FileUtils.createSDFile(FileUtils.DIR_MESSAGE_IMAGE, fileName);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraPicture));
            activity.startActivityForResult(intent, requestCode);
        } catch (IOException e) {
            LogUtils.e("print", e.getMessage());
        }
    }

    /**
     * 处理拍照结果
     */
    private void onCameraActivityResult() {
        try {
            final String path = mCameraPicture.getAbsolutePath();
            LogUtils.e("print", "--> onCameraActivityResult 图片路径：" + path);
            // 扫描图片
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(mCameraPicture));
            sendBroadcast(intent);
            mCameraPicture = null;
            HtmlUtils.appendImage(this, path, richEdit);
        } catch (Exception e) {
            LogUtils.e("print", e.getMessage());
        }
    }

    /**
     * 处理相册选中的照片
     *
     * @param data
     */
    private void onLocalGalleryActivityResult(Intent data) {
        if (data == null) {
            return;
        }
        try {
            Uri uri = data.getData();
            String path = GalleryUtils.getGalleryPicturePath(this, uri);
            LogUtils.e("print", "--> onLocalGalleryActivityResult 图片路径：" + path);
            HtmlUtils.appendImage(this, path, richEdit);
        } catch (Exception e) {
            LogUtils.e("print", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_select_local_pic:
                Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                albumIntent.addCategory(Intent.CATEGORY_OPENABLE);
                albumIntent.setType("image/*");
                startActivityForResult(albumIntent, SHOW_LOCAL_GALLERY);
                break;
            case R.id.bt_camera_pic:
                if (!FileUtils.isExternalMemoryAvailable()) {
                    Toast.makeText(this, "无SD存储卡或空间不足，无法完成该操作", Toast.LENGTH_SHORT).show();
                    return;
                }
                startSystemCamera(MainActivity.this, PHOTO_REQUEST_TAKEPHOTO);
                break;
            case R.id.bt_insert_link_text:
                if (!TextUtils.isEmpty(linkTextEdit.getText())) {
                    HtmlUtils.appendText(this, richEdit, linkTextEdit.getText().toString());
                } else {
                    Toast.makeText(this, "请输入要添加的内容", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bt_generate_html:

                // 此处用于发表自己的文章，如果添加了本地图片则先将图片一次性上传到服务器，
                // 服务器会返回一个UploadFileResultBean对象，包含了每个图片的上传时的原始名称以及生成的url链接，
                // 然后根据上传的文件名和服务器返回的文件名对比，目的是检查是否有上传失败的文件。
                // 最后将服务端返回img的url替换掉对应位置的ImageSpan并转换成<img>标签和文本一起提交到服务端，完成文章的发布

                String content = "";
                if (!HtmlUtils.hasCsImageSpan(richEdit.getText())) {//如果没有添加本地图片则直接提交文字
                    if (HtmlUtils.hasImageSpan(richEdit.getText())) {
                        content = HtmlUtils.buildHtmlStr(HtmlUtils.buildNetworkImagTag(richEdit.getText()));
                        Toast.makeText(this, "已将文章中包含的图片转换成<img>标签", Toast.LENGTH_SHORT).show();
                    } else {
                        content = TextUtils.isEmpty(richEdit.getText()) ? "" : richEdit.getText().toString();
                        Toast.makeText(this, "没有图片，只需将文本提交至服务器", Toast.LENGTH_SHORT).show();
                    }


                    //...
                    //....
                    //发表文章到服务器
                } else {
                    //上传文件至服务器，需要自己实现，并返回一个UploadFileResultBean对象result，
                    // 然后调用HtmlUtils.buildHtmlStr(HtmlUtils.buildLocalImagTag(result, richEdit.getText(),true));方法，将富文本转换成字符串

                    //...
                    // ...
                    //发表文章到服务器
                    Toast.makeText(this, "请先将图片上传到服务器", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        //处理简单的富文本，支持在文本中添加、删除、，插入图片，支持插入超链接
    }
}
