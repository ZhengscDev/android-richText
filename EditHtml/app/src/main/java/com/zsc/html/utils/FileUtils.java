package com.zsc.html.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.zsc.html.MainApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    /**
     * 图片存储目录
     */
    public static String DIR_MESSAGE_IMAGE = "image";

    /**
     * SD卡存储空间是否可用，存储空间需大于5M
     *
     * @return
     */
    public static boolean isExternalMemoryAvailable() {
        if (getAvailableExternalMemorySize() > 1024 * 5) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 内部存储空间是否可用，存储空间需大于5M
     *
     * @return
     */
    public static boolean isInternalMemoryAvailable() {
        if (getAvailableInternalMemorySize() > 1024 * 5) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取SDCARD剩余存储空间
     *
     * @return KB
     */
    public static long getAvailableExternalMemorySize() {
        long availSize = 0;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                availSize = stat.getAvailableBytes() / 1024;
            } else {
                long blockSize = stat.getBlockSize();
                long availCount = stat.getAvailableBlocks();
                availSize = availCount * blockSize / 1024;
            }
            return availSize;
        } else {
            return availSize;
        }
    }


    /**
     * 获取手机内部剩余存储空间
     *
     * @return KB
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long availSize = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            availSize = stat.getAvailableBytes() / 1024;
        } else {
            long blockSize = stat.getBlockSize();
            long availCount = stat.getAvailableBlocks();
            availSize = availCount * blockSize / 1024;
        }
        return availSize;
    }


    public static String getPath() {
        File file;
        // 获取当前外部存储设备的目录
        if (isExternalMemoryAvailable()) {
            file = Environment.getExternalStorageDirectory();
            if (file == null) {
                file = MainApplication.getInstance().getDir("demo", Context.MODE_PRIVATE);
                if (file == null) {
                    return "";
                }
            }
            return file.getAbsolutePath() + "/demo/";
        } else {
            file = MainApplication.getInstance().getExternalFilesDir("demo");
            if (file == null) {
                file = MainApplication.getInstance().getDir("demo", Context.MODE_PRIVATE);
                if (file == null) {
                    return "";
                }
            }
            return file.getAbsolutePath() + "/";
        }
    }

    /**
     * 在sd卡的路径下创建文件
     */
    public static File createSDFile(String dirName, String fileName) throws IOException {
        String path = getPath();
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path + dirName + "/" + fileName);
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!TextUtils.isEmpty(createDir(parentFile))) {
                if (file.createNewFile()) {
                    return file;
                }
            }
        }
        return null;
    }

    /**
     * 创建目录
     *
     * @param parentFile
     * @return
     */
    public static String createDir(File parentFile) {
        if (parentFile.exists() || parentFile.mkdirs()) {
            return parentFile.getAbsolutePath();
        }
        return null;
    }

    // 在sd卡的路径下创建目录
    public static File createSDDir(String dirname) {
        String path = getPath();
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File dir = new File(path + dirname);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }


    /**
     * 判断文件是否已经存在
     *
     * @param dirName
     * @param fileName
     * @return
     */
    public static boolean isFileExist(String dirName, String fileName) {
        String path = getPath();
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path + dirName + "/" + fileName);
        return file.exists();
    }

    /**
     * 将一个InputStream里的数据写进SD卡
     *
     * @param dirName
     * @param fileName
     * @param inputstream
     * @return
     */
    public static String saveFile(String dirName, String fileName, InputStream inputstream) {
        File file = null;
        OutputStream outputstream = null;
        try {
            createSDDir(dirName);
            file = createSDFile(dirName, fileName);
            if (file != null) {
                outputstream = new FileOutputStream(file);
                byte[] buffer = new byte[1024 * 2];
                int readLength = -1;
                while ((readLength = inputstream.read(buffer)) != -1) {
                    outputstream.write(buffer, 0, readLength);
                }
                outputstream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputstream != null) {
                try {
                    outputstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return fileName;
    }

    public static String saveFileByFullPath(String path, String fileName, InputStream inputstream) {
        File file = null;
        OutputStream outputstream = null;
        try {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            file = createSDFile(path, fileName);
            if (file != null) {
                outputstream = new FileOutputStream(file);
                byte[] buffer = new byte[1024 * 2];
                int readLength = -1;
                while ((readLength = inputstream.read(buffer)) != -1) {
                    outputstream.write(buffer, 0, readLength);
                }
                outputstream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputstream != null) {
                try {
                    outputstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return fileName;
    }


    /**
     * get names of file in the folder
     *
     * @param folderName String folderName
     * @return List<String> fileNames
     */
    public static List<String> getAllFileNameOfFolder(String folderName) {
        List<String> fileNames = new ArrayList<>();
        String path = getPath();
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File folder = new File(path + folderName);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                fileNames.add(file.getName());
            }
        }
        return fileNames;
    }

    /**
     * 获取apk file
     * 返回的大小单位为：k
     *
     * @return 返回文件大小
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file != null && file.exists()) {
            FileInputStream stream = null;
            try {
                stream = new FileInputStream(file);
                size = stream.available();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                        stream = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }
}