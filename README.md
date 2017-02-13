# fresco-helper
Android上图片加载库Fresco的使用帮助类

Fresco在GitHub上的项目地址：https://github.com/facebook/fresco

目前是基于Fresco `0.14.1`这个版本。

在使用到fresco-helper库的Module中的build.gradle文件里，添加以下依赖：
```
 allprojects {
    repositories {
        jcenter()

        maven {
            url 'https://dl.bintray.com/hpdx/maven/'
        }
    }
 }

 compile 'com.facebook.fresco.helper:fresco-helper:1.2.2'
```

## 目前对以下需求进行了封装
* ImagePipeline对象的初始化配置，默认配置了两个磁盘缓存
* 从网络加载图片并显示
* 从本地文件加载图片并显示
* 从本地res中加载图片并显示
* **加载并显示gif格式的图片**
* **加载并显示webp格式的图片**

* 只知道要显示图片的高或者宽的值，另一个值可以从设置的比例得出
* 解码指定尺寸大小的图片，图片数据来源支持网络、本地文件和本地res中的，并显示
* **支持不知道图片原始尺寸，也不知道要显示的图片尺寸，加载并显示**
* **根据url下载图片（未解码），并保存到本地指定的路径**
* **根据url获取已解码的对象（bitmap）**
* **图片的高斯模糊处理**
* **根据url下载图片，并对其进行高斯模糊处理，之后显示为任意View的背景**

* 查找一张图片在已解码的缓存中是否存在
* 查找一张图片在磁盘缓存中是否存在，若配有两个磁盘缓存，则只要其中一个存在，就会返回true
* 查找一张图片在磁盘缓存中是否存在，可以指定是哪个磁盘缓存
* 从内存缓存中移除指定图片的缓存
* 从磁盘缓存中移除指定图片的缓存
* 移除指定图片的所有缓存（包括内存+磁盘）

* 清空所有内存缓存
* 清空所有磁盘缓存，若你配置有两个磁盘缓存，则两个都会清除
* 清除所有缓存（包括内存+磁盘）

* 当前网络请求是否处于暂停状态
* 暂停所有图片的网络请求
* 恢复所有图片的网络请求


## 示例效果如下：

<img src="http://img.blog.csdn.net/20161114234420713" width="320px" />

常见的各种效果

<img src="http://img.blog.csdn.net/20161114234525933" width="320px" />

在进行高斯模糊前的照片

<img src="http://img.blog.csdn.net/20161112180841944" width="320px" />

对照片进行高斯模糊后的效果

<img src="http://img.blog.csdn.net/20161112180917335" width="320px" />

[下载示例Apk](https://github.com/hpdx/fresco-helper/blob/master/app-debug.apk)

## 使用：

初始化
```
 Phoenix.init(this); // Context
```

从网络加载一张图片
```
String url = "http://ww3.sinaimg.cn/large/610dc034jw1f6m4aj83g9j20zk1hcww3.jpg";
SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
Phoenix.with(simpleDraweeView).load(url);
```

从本地加载一张图片
```
String filePath = "/sdcard/image/test.jpg";
SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
Phoenix.with(simpleDraweeView).load(filePath);
```

从res下面加载一张图片
```
SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
Phoenix.with(simpleDraweeView).load(R.drawable.meizi);
```

在写布局文件xml时，我不知道要显示的图片尺寸，需要根据业务逻辑动态的设置要显示的图片的大小，可以像下面这样写：
```
String url = "http://ww3.sinaimg.cn/large/610dc034jw1f6m4aj83g9j20zk1hcww3.jpg";
SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
Phoenix.with(simpleDraweeView)
       .setWidth(100)
       .setHeight(100)
       .load(url);
```

只知道要显示图片的高或者宽的值，另一个值可以从设置的比例得出
```
String url = "http://ww3.sinaimg.cn/large/610dc034jw1f6m4aj83g9j20zk1hcww3.jpg";
SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
Phoenix.with(simpleDraweeView)
       .setWidth(100)
       .setAspectRatio(0.6f) // w/h = 6/10
       .load(url);
```

图片的高斯模糊处理
```
String url = "http://ww3.sinaimg.cn/large/610dc034jw1f6m4aj83g9j20zk1hcww3.jpg";
SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
Phoenix.with(simpleDraweeView)
       .setNeedBlur(true)
       .load(url);
```

加载并显示gif格式的图片
```
String url = "http://img4.178.com/acg1/201506/227753817857/227754566617.gif";
SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
Phoenix.with(simpleDraweeView)
       .load(url);
```

加载并显示webp格式的图片
```
SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
Phoenix.with(simpleDraweeView)
        .load(R.drawable.meizi_webp);
```

将图片资源预加载到磁盘缓存
```
String url = "http://ww1.sinaimg.cn/large/610dc034jw1fahy9m7xw0j20u00u042l.jpg";
Phoenix.prefetchToDiskCache(url);
```

从网络下载一张图片
```
String url = "http://feed.chujianapp.com/20161108/452ab5752287a99a1b5387e2cd849006.jpg@1080w";
String filePath = "";
Phoenix.with(url)
       .setResult(new IDownloadResult(filePath) {
           @Override
           public void onResult(String filePath) {
               MLog.i("filePath = " + filePath);

           }
       })
       .download();
```
......


更详细的讲解，请查阅我的这篇博客：[Android图片加载神器之Fresco，基于各种使用场景的讲解。](http://blog.csdn.net/android_ls/article/details/53137867)

在使用过程中有满足不了的使用场景或遇到bug，欢迎提issuse ! 若你觉得还不错，请点Star, 谢谢！
