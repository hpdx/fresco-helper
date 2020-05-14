package com.anbetter.oka.http;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.anbetter.oka.http.callback.IRequest;
import com.anbetter.oka.http.callback.OnResponseListener;
import com.anbetter.oka.http.dispatcher.Dispatcher;
import com.anbetter.oka.http.entity.RespData;
import com.anbetter.oka.http.exception.HttpStatusCodeException;
import com.anbetter.oka.http.parser.DownloadParser;
import com.anbetter.oka.http.parser.Parser;
import com.anbetter.oka.http.parser.ResponseParser;
import com.anbetter.oka.http.parser.StringParser;
import com.anbetter.oka.http.utils.MLog;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 基于OkHttp3的异步网络请求封装类
 * <p>
 * Created by android_ls on 2020-03-05 18:29.
 *
 * @author android_ls
 * @version 1.0
 */
public class OkRequest<T> implements IRequest, Callback {

    private Call mCall;
    private Parser<T> mParser;
    private OnResponseListener<T> mOnResponse;

    public OkRequest(Call call, Parser<T> parser, OnResponseListener<T> onResponse) {
        this.mCall = call;
        this.mParser = parser;
        this.mOnResponse = onResponse;
    }

    public IRequest enqueue() {
        mCall.enqueue(this);
        return this;
    }

    @Override
    public void onFailure(@NonNull Call call, IOException e) {
        MLog.e("IOException: " + e);
        Dispatcher.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (mOnResponse != null) {
                    mOnResponse.onError(getErrorMessage(e));
                }
            }
        });
        e.printStackTrace();
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        try {
            ResponseBody responseBody = throwIfFatal(response);
            if (mParser == null) {
                mParser = new StringParser<>(String.class);
            }

            MLog.i("type: " + mParser.mType);
            if (mParser instanceof DownloadParser) {
                // 下载文件
                DownloadParser<T> downloadParser = (DownloadParser<T>) mParser;
                final T resultData = downloadParser.parser(responseBody);
                if (mOnResponse != null) {
                    mOnResponse.onResponse(resultData);
                }
            } else if (mParser instanceof StringParser) {
                String responseData = responseBody.string();
                // 返回原始数据
                final T resultData = mParser.parser(responseData);
                Dispatcher.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mOnResponse != null) {
                            mOnResponse.onResponse(resultData);
                        }
                    }
                });
            } else {
                final String responseData = responseBody.string();
                MLog.i("responseData: " + responseData);
                if (mParser.mType == String.class) {
                    Dispatcher.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mOnResponse != null) {
                                mOnResponse.onResponse((T) responseData);
                            }
                        }
                    });
                } else {
                    ResponseParser<RespData> commonParser = new ResponseParser<>(RespData.class);
                    final RespData data = commonParser.parser(responseData);
                    if (mParser.mType == RespData.class) {
                        Dispatcher.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mOnResponse != null) {
                                    mOnResponse.onResponse((T) data);
                                }
                            }
                        });
                    } else {
                        if (data.code == 1 && !TextUtils.isEmpty(data.data)) {
                            final T resultData = mParser.parser(data.data);
                            Dispatcher.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mOnResponse != null) {
                                        mOnResponse.onResponse(resultData);
                                    }
                                }
                            });
                        } else {
                            MLog.e("data: " + data.toString());
                            Dispatcher.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    String errorMsg = data.msg;
                                    if (TextUtils.isEmpty(errorMsg)) {
                                        errorMsg = "[" + data.code + "] 解析数据出错";
                                    }
                                    if (mOnResponse != null) {
                                        mOnResponse.onError(errorMsg);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        } catch (HttpStatusCodeException e) {
            MLog.i(e.toString());
            Dispatcher.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    String errorMsg = "[" + e.getLocalizedMessage() + "]" + e.getMessage();
                    if (mOnResponse != null) {
                        mOnResponse.onError(errorMsg);
                    }
                }
            });
        } catch (IOException e) {
            Dispatcher.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    String errorMsg = "[" + response.code() + "]" + response.message();
                    if (mOnResponse != null) {
                        mOnResponse.onError(errorMsg);
                    }
                }
            });
            e.printStackTrace();
        } finally {
            response.close();
        }
    }

    private String getErrorMessage(IOException error) {
        String errorMsg = error.getMessage();
        if (error instanceof UnknownHostException
                || error instanceof EOFException
                || error instanceof SocketException) {
            errorMsg = "网络连接失败，请检查你的网络";
        } else if (error instanceof SocketTimeoutException) {
            errorMsg = "网络连接超时，请检查你的网络";
        } else if (error instanceof SSLHandshakeException) {
            errorMsg = "网络连接失败，SSL证书验证出错";
        }
        return errorMsg;
    }

    /**
     * 根据Http执行结果过滤异常
     *
     * @param response Http响应体
     * @return ResponseBody
     * @throws IOException 请求失败异常、网络不可用异常
     */
    private ResponseBody throwIfFatal(@NonNull Response response) throws IOException {
        ResponseBody body = response.body();
        if (body == null) {
            throw new HttpStatusCodeException(response);
        }

        if (!response.isSuccessful()) {
            throw new HttpStatusCodeException(response, body.string());
        }
        return body;
    }

    public String getUrl() {
        if (mCall != null) {
            return mCall.request().url().toString();
        }
        return null;
    }

    @Override
    public void cancel() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

}
