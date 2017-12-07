package com.android.fresco.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.anbetter.log.MLog;
import com.android.fresco.demo.photo.PhotoAlbumActivity;
import com.android.fresco.demo.photo.PhotoWallActivity;
import com.facebook.fresco.helper.Phoenix;
import com.facebook.fresco.helper.listener.IDownloadResult;
import com.facebook.fresco.helper.listener.IResult;
import com.facebook.fresco.helper.utils.ImageFileUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_load_local_bitmap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoadDiskCacheImageActivity.class));
            }
        });

        findViewById(R.id.btn_open_photo_wall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PhotoWallActivity.class));
            }
        });

        findViewById(R.id.btn_open_photo_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PhotoAlbumActivity.class));
            }
        });

        findViewById(R.id.btn_base_use).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FrescoBaseUseActivity.class));
            }
        });

        findViewById(R.id.btn_clear_memory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Phoenix.clearCaches();
                ((Button)findViewById(R.id.btn_get_cache_size)).setText("获取已使用的缓存大小");
            }
        });

        findViewById(R.id.btn_big).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BigImageActivity.class));
            }
        });

        findViewById(R.id.btn_big_big).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BigBigImageActivity.class));
            }
        });

        findViewById(R.id.btn_asset_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AssetsActivity.class));
            }
        });

        findViewById(R.id.btn_get_cache_size).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long cacheSize = Phoenix.getMainDiskStorageCacheSize();
                MLog.i("cacheSize = " + cacheSize);
                ((Button)findViewById(R.id.btn_get_cache_size)).setText("缓存:" + (cacheSize/1024) + "kb");
            }
        });

        findViewById(R.id.btn_small).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ImageSizeActivity.class));
            }
        });

        findViewById(R.id.btn_gif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GifActivity.class));
            }
        });

        findViewById(R.id.btn_meizi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MeiziActivity.class));
            }
        });

        findViewById(R.id.btn_meizi_webp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WebpActivity.class));
            }
        });

        findViewById(R.id.btn_use_databinding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UseDataBindingActivity.class));
            }
        });

        findViewById(R.id.btn_blur).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BlurActivity.class));
            }
        });

        findViewById(R.id.btn_blur2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Blur2Activity.class));
            }
        });

        findViewById(R.id.btn_load_bitmap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BitmapActivity.class));
            }
        });

        findViewById(R.id.btn_gif_bitmap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GIFFirstFrameActivity.class));
            }
        });

    }

    public void preLoad() {
        String url = "http://ww1.sinaimg.cn/large/610dc034jw1fahy9m7xw0j20u00u042l.jpg";
        // 将图片资源预加载到磁盘缓存
        Phoenix.prefetchToDiskCache(url);

        // 检查磁盘缓存中是否存在
        if (Phoenix.isInDiskCacheSync(Uri.parse(url))) {
            MLog.i("---->isInDiskCacheSync");
        }
    }

    public void downloadImage(Context context) {
        String url = "http://ww1.sinaimg.cn/large/610dc034jw1fahy9m7xw0j20u00u042l.jpg";
        String filePath = "";
        Phoenix.with(context)
                .setUrl(url)
                .setResult(new IDownloadResult(filePath) {
                    @Override
                    public void onResult(String filePath) {
                        MLog.i("filePath = " + filePath);
                        // 在子线程，要显示到View上，需要切换到主线程
                    }
                })
                .download();
    }

    public void downloadBitmap(Context context) {
        String url = "http://ww1.sinaimg.cn/large/610dc034jw1fahy9m7xw0j20u00u042l.jpg";
        Phoenix.with(context)
                .setUrl(url)
                .setResult(new IResult<Bitmap>() {
            @Override
            public void onResult(Bitmap result) {
                // 在主线程

            }
        }).load();
    }

    public void downloadImageGif(Context context) {
        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496383829892&di=ac88e62a1424eaddcc03502e99e8168f&imgtype=0&src=http%3A%2F%2Fi3.17173cdn.com%2F2fhnvk%2FYWxqaGBf%2Fcms3%2FVqLgYAbkkbmrjhy.gif";
        String filePath = ImageFileUtils.getImageDownloadPath(context, url);
        Phoenix.with(context)
                .setUrl(url)
                .setResult(new IDownloadResult(filePath) {
                    @Override
                    public void onResult(String filePath) {
                        MLog.i("filePath = " + filePath);
                        // 在子线程，要显示到View上，需要切换到主线程
                    }
                })
                .download();
    }

}
