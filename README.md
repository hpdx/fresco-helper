# fresco-helper
Android上图片加载库Fresco的封装库，只需几行代码，就可以快速集成到项目中。告别繁琐的配置，快速上手，满足项目中遇到的各种应用场景。

## 依赖的开源库：
fresco：https://github.com/facebook/fresco（`v1.3.0`）

subsampling-scale-image-view：https://github.com/davemorrissey/subsampling-scale-image-view（`v3.6.0`）

## Demo运行后的效果图：
<img src="http://img.blog.csdn.net/20161114234420713" width="320px" />

[下载示例Apk](https://github.com/hpdx/fresco-helper/blob/master/app-debug.apk)

## 使用：
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

 compile 'com.facebook.fresco.helper:fresco-helper:1.3.1'
```

初始化
```
Phoenix.init(this); // Context
```

xml布局文件
```        
<com.facebook.drawee.view.SimpleDraweeView
        android:id="@+id/sdv_1"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:actualImageScaleType="centerCrop" />
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

从assets下面加载一张图片
```
SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
Phoenix.with(simpleDraweeView)
       .setAssets(true)
       .load("qingchun.jpg");
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

将图片资源预加载到内存缓存
```
String url = "http://ww1.sinaimg.cn/large/610dc034jw1fahy9m7xw0j20u00u042l.jpg";
Phoenix.prefetchToBitmapCache(url);
```

从网络下载一张图片，下载完成后把Bitmap给我（返回的结果是在主线程）
```
String url = "http://feed.chujianapp.com/20161108/452ab5752287a99a1b5387e2cd849006.jpg@1080w";
Phoenix.with(context)
       .setUrl(url)
       .setResult(new IResult<Bitmap>() {
            @Override
            public void onResult(Bitmap result) {

            }
        }).load();
```

从网络下载一张图片，下载完成后告诉我下载的图片存在哪里（返回的结果是在工作线程，非主线程）
```
String url = "http://feed.chujianapp.com/20161108/452ab5752287a99a1b5387e2cd849006.jpg@1080w";
String filePath = "";
Phoenix.with(context)
       .setUrl(url)
       .setResult(new IDownloadResult(filePath) {
           @Override
           public void onResult(String filePath) {
               MLog.i("filePath = " + filePath);

           }
       }).download(); 
```


## 从网络加载一张超大图
大小以M为单位，目标图片宽度大于手机屏幕宽的2倍以上或者高度大于手机屏幕高的2倍以上

xml布局文件
```
<com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/image"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

从网络加载并显示
```
String url = "http://img5q.duitang.com/uploads/item/201402/24/20140224212510_eQRG5.thumb.700_0.jpeg";
final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) findViewById(R.id.image);
Phoenix.with(imageView).load(url);
``` 

设置缩放参数
```
imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
imageView.setMinScale(1.0f);
imageView.setMaxScale(2.0f);
```

禁用缩放功能（你可能会有这个需求）
```
imageView.setZoomEnabled(false);
```

添加各种事件处理（单击、双击、长按）
```
    final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    MLog.i("单击: " + ((int) sCoord.x) + ", " + ((int) sCoord.y));
                } else {
                    MLog.i("onSingleTapConfirmed onError");
                }

                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    MLog.i("长按: " + ((int) sCoord.x) + ", " + ((int) sCoord.y));
                } else {
                    MLog.i("onLongPress onError");
                }
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    MLog.i("双击: " + ((int) sCoord.x) + ", " + ((int) sCoord.y));
                } else {
                    MLog.i("onDoubleTap onError");
                }
                return false;
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
```

从网络加载，存放到指定的缓存目录并显示

```
String fileCacheDir = getCacheDir().getAbsolutePath();
MLog.i("fileCacheDir = " + fileCacheDir);
Phoenix.with(imageView)
       .setDiskCacheDir(fileCacheDir)
       .load(url);     
```

加载本地资源并显示
```
imageView.setImage(ImageSource.asset("qmsht.jpg"));
imageView.setImage(ImageSource.resource(R.drawable.monkey));
imageView.setImage(ImageSource.uri("/storage/emulated/0/fresco-helper/Download/Images/20140224212510_eQRG5.thumb.700_0.jpeg"));
imageView.setImage(ImageSource.bitmap(bitmap));    

```

// 获取已占用磁盘缓存的大小，已B为单位
```
long cacheSize = Phoenix.getMainDiskStorageCacheSize();
MLog.i("cacheSize = " + cacheSize);
```

清空所有内存缓存
```
Phoenix.clearMemoryCaches();
```

清空所有磁盘缓存
```
Phoenix.clearDiskCaches();
```

清除所有缓存（包括内存+磁盘）
```
Phoenix.clearCaches();
```

## 与之配套的浏览大图，若需要可以试试我的这个开源库
[fresco-photoview](https://github.com/hpdx/fresco-photoview)


更详细的讲解，请查阅我的这篇博客：[Android图片加载神器之Fresco，基于各种使用场景的讲解。](http://blog.csdn.net/android_ls/article/details/53137867)

在使用过程中有满足不了的使用场景或遇到bug，欢迎提issuse ! 若你觉得还不错，请点Star, 谢谢！
