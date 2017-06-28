# android-richText
处理简单的富文本，支持在文本中添加、删除、，插入图片，支持插入超链接

# Usage
1. 将 ```lib/richtext-lib.jar``` 此jar包复制到主项目的libs下

2. 在主项目的 ```build.gradle``` 的dependencies下添加如下：
```
    dependencies {
    
        ...
        ...
        //添加
        compile 'com.github.bumptech.glide:glide:3.7.0'
        compile 'com.android.support:support-v4:22.2.1'
    }
    
```
3. 在主项目中的 `AndroidMainfest.xml` 中添加如下权限：
```
    //添加
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

4. 创建一个 TestApplication(自定义)  继承 `RichApplication`,然后在主项目的 `AndroidMainfest.xml` 配置此TestApplication

```
    <application
        //添加
        android:name=".TestApplication"
        ...

    </application>
```
或者不创建TestApplication，直接使用 

```
    <application
        //添加
        android:name="com.zsc.richtext.RichApplication
        ...
    </application>
```