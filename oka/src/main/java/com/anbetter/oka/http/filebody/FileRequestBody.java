package com.anbetter.oka.http.filebody;

import androidx.annotation.NonNull;

import com.anbetter.oka.http.callback.OnProgressRespResult;
import com.anbetter.oka.http.dispatcher.Dispatcher;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 上传文件，可获取当前的上传进度
 *
 * Created by android_ls on 2017/10/31.
 */
public class FileRequestBody extends RequestBody {

    private static final int REFRESH_TIME = 50; // 回调刷新时间（单位ms）
    private RequestBody delegate;  // 实际的待包装请求体
    private OnProgressRespResult mOnProgressListener;
    private MyForwardingSink myForwardingSink;

    public FileRequestBody(RequestBody delegate, OnProgressRespResult progressListener) {
        this.delegate = delegate;
        this.mOnProgressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return delegate.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        myForwardingSink = new MyForwardingSink(sink, contentLength(), mOnProgressListener);
        BufferedSink bufferedSink = Okio.buffer(myForwardingSink);
        delegate.writeTo(bufferedSink);
        bufferedSink.flush();  // 必须调用flush，否则最后一部分数据可能不会被写入
    }

    static class MyForwardingSink extends ForwardingSink {

        long bytesWritten = 0L; // 当前写入字节数
        long contentLength = 0L; // 总字节长度，避免多次调用contentLength()方法

        int lastProgress; //上次回调进度
        long lastTime;//上次回调时间
        OnProgressRespResult mOnProgressListener;

        public MyForwardingSink(Sink delegate, long contentLength, OnProgressRespResult progressListener) {
            super(delegate);
            this.bytesWritten = 0L; // 当前写入字节数
            this.contentLength = contentLength; // 总字节长度，避免多次调用contentLength()方法
            this.lastProgress = 0; //上次回调进度
            this.lastTime = 0;//上次回调时间
            this.mOnProgressListener = progressListener;
        }

        @Override
        public void write(@NonNull Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);

            //增加当前写入的字节数
            bytesWritten += byteCount;

            int currentProgress = (int) ((bytesWritten * 100) / contentLength);
            if (currentProgress <= lastProgress) return; //进度较上次没有更新，直接返回

            // 当前进度小于100，需要判断两次回调时间间隔是否小于一定时间，是的话直接返回
            if (currentProgress < 100) {
                long currentTime = System.currentTimeMillis();
                // 两次回调时间间隔小于 REFRESH_TIME 毫秒,直接返回,避免更新太频繁
                if (currentTime - lastTime < REFRESH_TIME) {
                    return;
                }
                lastTime = currentTime;
            }
            lastProgress = currentProgress;

            // 回调, 更新进度
            updateProgress(currentProgress, bytesWritten, contentLength);
        }

        private void updateProgress(final int progress, final long currentSize, final long totalSize) {
            Dispatcher.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if(mOnProgressListener != null) {
                        mOnProgressListener.onProgress(progress, currentSize, totalSize);
                    }
                }
            });
        }
    }

}