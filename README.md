# fresco-helper

## Glide的使用示例
https://github.com/hpdx/glide-helper

## 依赖开源库
fresco v2.5.0：[https://github.com/facebook/fresco](https://github.com/facebook/fresco)

subsampling-scale-image-view v3.10.0：[https://github.com/davemorrissey/subsampling-scale-image-view](https://github.com/davemorrissey/subsampling-scale-image-view)

## Demo运行后效果图
<img src="https://github.com/hpdx/fresco-helper/blob/master/images/demo.jpg"  width="270px"/>  <img src="https://github.com/hpdx/fresco-helper/blob/master/images/demo1.jpg" width="270px"/>

<img src="https://github.com/hpdx/fresco-helper/blob/master/images/photo_wall.jpg" width="270px"/>  <img src="https://github.com/hpdx/fresco-helper/blob/master/images/big_image_browse.jpg" width="270px"/>

[下载示例Apk](https://github.com/hpdx/fresco-helper/blob/master/app-debug.apk)

## 使用
在使用到fresco-helper库的Module中的build.gradle文件里，添加以下依赖：
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

implementation 'com.github.hpdx:fresco-helper:2.5.0'
```

### 初始化
```
Phoenix.init(this); // Context
```

### 图片加载部分
从网络加载一张图片
```
String url = "http://ww3.sinaimg.cn/large/610dc034jw1f6m4aj83g9j20zk1hcww3.jpg";
SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
Phoenix.with(simpleDraweeView).load(url);
```

从网络加载一张图片，按原图等比缩放显示
```
String url = "http://ww3.sinaimg.cn/large/610dc034jw1f6m4aj83g9j20zk1hcww3.jpg";
SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
Phoenix.with(simpleDraweeView)
.setControllerListener(new SingleImageControllerListener(simpleDraweeView))
.load(url);
```

从网络加载一张图片，按原图等比缩放显示，添加限制最大宽高值（在指定的区域内等比缩放）
```
String url = "https://ws1.sinaimg.cn/large/0065oQSqly1fymj13tnjmj30r60zf79k.jpg";
SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
Phoenix.with(simpleDraweeView)
       .setControllerListener(new SingleImageControllerListener(simpleDraweeView,
                        DensityUtil.dipToPixels(this, 200), DensityUtil.dipToPixels(this, 200)))
       .load(url);
```

从网络加载一张图片，想加上自己的后处理
```
String url = "http://ww3.sinaimg.cn/large/610dc034jw1f6m4aj83g9j20zk1hcww3.jpg";
SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
Phoenix.with(simpleDraweeView)
       .setBasePostprocessor(new BasePostprocessor() {
            @Override
            public void process(Bitmap bitmap) {
                // 后处理，比如做高斯模糊处理
                
            }
        }).load(url);
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
                // 在主线程

            }
        }).load();
```

从网络下载gif图片，返回Bitmap对象
```
String url = "http://lh-avatar.liehuozhibo.com/20170911/d7682418510c7877fd4dd6afaa1be460.jpg?x-oss-process=image/resize,w_640,h_640/quality,q_90";
Phoenix.with(GIFFirstFrameActivity.this)
       .setUrl(url)
//     .setWidth(DensityUtil.dipToPixels(GIFFirstFrameActivity.this, 100))
//     .setHeight(DensityUtil.dipToPixels(GIFFirstFrameActivity.this, 100))
       .setResult(new IResult<Bitmap>() {
              @Override
              public void onResult(Bitmap result) {
                  mImageView.setImageBitmap(result);
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
               // 在子线程，要显示到View上，需要切换到主线程

           }
       }).download(); 
```


从本地磁盘缓存中获取Bitmap
```
String url = "https://ws1.sinaimg.cn/large/610dc034ly1fgi3vd6irmj20u011i439.jpg";
Phoenix.with(url)
       .setResult(new IResult<Bitmap>() {

           @Override
           public void onResult(Bitmap result) {
              // 在主线程

              }
           }).loadLocalDiskCache();
```

### 在列表滚动中暂停加载图片，等滚动停止后再继续加载
```
mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
       super.onScrollStateChanged(recyclerView, newState);
       if (newState == RecyclerView.SCROLL_STATE_IDLE) {
           Fresco.getImagePipeline().resume();
       } else {
           Fresco.getImagePipeline().pause();
       }
    }
});
```

### 从网络加载一张超大图
大小以M为单位，目标图片宽度大于手机屏幕宽的2倍以上或者高度大于手机屏幕高的2倍以上

xml布局文件
```
<com.facebook.fresco.helper.photoview.LargePhotoView
    android:id="@+id/image"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

从网络加载并显示
```
String url = "http://img5q.duitang.com/uploads/item/201402/24/20140224212510_eQRG5.thumb.700_0.jpeg";
final LargePhotoView imageView = (LargePhotoView) findViewById(R.id.image);
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


## 网络请求库使用OKHttp3

1、可以使用Fresco内置的OKHttp3 v3.6.0
```
compile 'com.facebook.fresco:imagepipeline-okhttp3:1.3.0'
```

2、在项目中已经使用OKHttp3作为网络请求库，想让图片加载库网络请求，使用与项目中一致的版本
```
compile 'com.squareup.okhttp3:okhttp:3.8.0'
```

3、调整配置参数
```
 ImagePipelineConfig imagePipelineConfig = new PhoenixConfig.Builder(this) // Context
                .setNetworkFetcher(null)
                .setRequestListeners(null)
                .setBitmapMemoryCacheParamsSupplier(null)
                .setMemoryTrimmableRegistry(null)
                .setMainDiskCacheConfig(null)
                .setSmallImageDiskCacheConfig(null)
                .build();
 Phoenix.init(this, imagePipelineConfig);
```

设置网络请求库使用OKHttp3的示例

```
// LOG过滤标签： RequestLoggingListener
Set<RequestListener> requestListeners = new HashSet<>();
requestListeners.add(new RequestLoggingListener());

HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build();

ImagePipelineConfig imagePipelineConfig = new PhoenixConfig.Builder(this)
        .setNetworkFetcher(new OkHttpNetworkFetcher(okHttpClient))
        .setRequestListeners(requestListeners)
        .build();

Phoenix.init(this, imagePipelineConfig); // this-->Context
```


## 其它

获取已占用磁盘缓存的大小，已B为单位
```
long cacheSize = Phoenix.getMainDiskStorageCacheSize();
MLog.i("cacheSize = " + cacheSize);
```

从内存缓存中移除指定图片的缓存
```
String url = "http://ww1.sinaimg.cn/large/610dc034jw1fahy9m7xw0j20u00u042l.jpg";
Phoenix.evictFromMemoryCache(url);
```

从磁盘缓存中移除指定图片的缓存
```
String url = "http://ww1.sinaimg.cn/large/610dc034jw1fahy9m7xw0j20u00u042l.jpg";
Phoenix.evictFromDiskCache(url);
```

移除指定图片的所有缓存（包括内存+磁盘）
```
String url = "http://ww1.sinaimg.cn/large/610dc034jw1fahy9m7xw0j20u00u042l.jpg";
Phoenix.evictFromCache(url);
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

## 照片墙、浏览大图
* 支持向下拖动关闭
* 支持双击放大效果
* 支持单击关闭大图浏览
* 支持手势缩放功能
* 支持屏蔽长按事件
* 支持扩展，可以自定义浏览大图的UI风格


### 带动画的效果打开，支持向下拖动关闭
```
ArrayList<PhotoInfo> photos = null;
PhotoX.with(PhotoWallActivity.this)
             .setLayoutManager(mLayoutManager)
             .setPhotoList(photos)
             .setCurrentPosition(position)
             .enabledAnimation(true)
             .enabledDragClose(true) // true->向下拖动关闭
             .start();
```

带动画的效果打开方式（多图）
```
ArrayList<PhotoInfo> photos = null;
PhotoX.with(PhotoWallActivity.this)
             .setLayoutManager(mLayoutManager)
             .setPhotoList(photos)
             .setCurrentPosition(position)
             .enabledAnimation(true)
             .start();
```

无动画效果的打开方式（多图）
```
 ArrayList<PhotoInfo> photos = null;
 PhotoX.with(PhotoWallActivity.this)
              .setPhotoList(photos)
              .setCurrentPosition(position)
              .start();
```

带动画效果的打开方式（只有一张图片）
```
String originalUrl = photos.get(position).originalUrl;
PhotoX.with(PhotoWallActivity.this)
             .setThumbnailView(view)
             .setOriginalUrl(originalUrl)
             .enabledAnimation(true)
             .start();
```

无动画效果的打开方式（只有一张图片）
```
String originalUrl = photos.get(position).originalUrl;
PhotoX.with(PhotoWallActivity.this)
             .setOriginalUrl(originalUrl)
             .start();
```

屏蔽长按事件
```
PhotoX.with(PhotoWallActivity.this, PhotoBrowseActivity.class)
             .setLayoutManager(mLayoutManager)
             .setPhotoList(photos)
             .setCurrentPosition(position)
             .enabledAnimation(true)
             .toggleLongClick(false) // 屏蔽长按事件
             .start();
```

支持扩展，可以自定义浏览大图的UI风格
```
public class PhotoBrowseActivity extends PictureBrowseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_photo_browse;
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        findViewById(R.id.rl_top_deleted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MLog.i("用户点击了删除按钮");
                MLog.i("mPhotoIndex = " + mPhotoIndex);

                PhotoInfo photoInfo = mItems.get(mPhotoIndex);
                MLog.i("originalUrl = " + photoInfo.originalUrl);

            }
        });
    }

    @Override
    public boolean onLongClick(View view) {
        MLog.i("currentPosition = " + getCurrentPosition());

        PhotoInfo photoInfo = getCurrentPhotoInfo();
        MLog.i("current originalUrl = " + photoInfo.originalUrl);

        return super.onLongClick(view);
    }

}

```

## 查看LOG
```
 FLog.setMinimumLoggingLevel(FLog.VERBOSE);
```

```
  adb logcat -v threadtime | grep -iE 'LoggingListener|AbstractDraweeController|BufferedDiskCache'
```


## 附录
在xml布局文件中使用
```        
<com.facebook.drawee.view.SimpleDraweeView
        android:id="@+id/sdv_1"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:actualImageScaleType="centerCrop" />
```

圆形
``` 
app:roundAsCircle="true"     
```     

圆形，添加边框
``` 
app:roundAsCircle="true"
app:roundingBorderColor="#fff3cf44"
app:roundingBorderWidth="2dp"   
```   

四个圆角
``` 
app:roundAsCircle="false"
app:roundedCornerRadius="10dp"   
```    

上面直角，底部圆角
``` 
app:roundAsCircle="false"
app:roundBottomLeft="true"
app:roundBottomRight="true"
app:roundTopLeft="false"
app:roundTopRight="false"
app:roundedCornerRadius="10dp" 
```   

占位图
``` 
app:placeholderImage="@mipmap/ic_launcher"
app:placeholderImageScaleType="centerCrop"
```  

加载失败时，显示的图，默认使用占位图
``` 
app:failureImage="@mipmap/ic_launcher"
app:failureImageScaleType="centerInside"
``` 

加载失败后，重试显示的图，默认使用占位图
``` 
app:retryImage="@mipmap/ic_launcher"
app:retryImageScaleType="centerCrop"
``` 

### 
更详细的讲解，请查阅我的这篇博客：[Android图片加载神器之Fresco，基于各种使用场景的讲解。](http://blog.csdn.net/android_ls/article/details/53137867)

在使用过程中有满足不了的使用场景或遇到bug，欢迎提issuse ! 若你觉得还不错，请点Star, 谢谢！


### License
```
Copyright (C) 2019 android_ls@163.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```
