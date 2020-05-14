package com.anbetter.oka.http;

import android.text.TextUtils;

import com.anbetter.oka.http.callback.IRequest;
import com.anbetter.oka.http.callback.OnProgressRespResult;
import com.anbetter.oka.http.callback.OnResponseListener;
import com.anbetter.oka.http.filebody.FileRequestBody;
import com.anbetter.oka.http.interceptor.ResponseInterceptor;
import com.anbetter.oka.http.interceptor.RetryInterceptor;
import com.anbetter.oka.http.parser.DownloadParser;
import com.anbetter.oka.http.parser.ObjectParser;
import com.anbetter.oka.http.parser.ParameterizedTypeImpl;
import com.anbetter.oka.http.parser.Parser;
import com.anbetter.oka.http.parser.StringParser;
import com.anbetter.oka.http.utils.MLog;
import com.anbetter.oka.http.utils.UrlUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * <p>
 * Created by android_ls on 2020-03-05 14:13.
 *
 * @author android_ls
 * @version 1.0
 */
public class OkHttp {

    private OkHttp() {
    }

    private static Map<String, String> mHeaders;
    private static OkHttpClient mOkHttpClient;

    public static void setHttpClient(OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
    }

    public static void addHeaders(Map<String, String> headers) {
        OkHttp.mHeaders = headers;
    }

    public static void addHeader(String key, String value) {
        if (mHeaders == null) {
            mHeaders = new HashMap<>();
        }
        mHeaders.put(key, value);
    }

    public static Map<String, String> getHeaders() {
        return mHeaders;
    }

    public static OkHttp.Builder post() {
        OkHttp.Builder builder = new OkHttp.Builder();
        builder.post();
        return builder;
    }

    public static OkHttp.Builder get() {
        OkHttp.Builder builder = new OkHttp.Builder();
        builder.get();
        return builder;
    }

    public static OkHttp.Builder post(String url) {
        OkHttp.Builder builder = new OkHttp.Builder(url);
        builder.post();
        return builder;
    }

    public static OkHttp.Builder get(String url) {
        OkHttp.Builder builder = new OkHttp.Builder(url);
        builder.get();
        return builder;
    }

    public static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = getDefaultOkHttpClient();
        }
        return mOkHttpClient;
    }

    public static final class Builder {

        private static final int REQUEST_TYPE_GET = 1;
        private static final int REQUEST_TYPE_POST = 2;

        private String url;
        private String filePath;
        private Map<String, String> headers;
        private Map<String, Object> params;
        private Map<String, String> files;
        private RequestBody requestBody;

        private long connectTimeout;
        private long readTimeout;
        private long writeTimeout;

        private int type;
        private Parser<?> parser;
        private OnResponseListener<?> response;

        private Request request;
        private OkHttpClient okHttpClient;

        public Builder() {
            params = new HashMap<>();
            connectTimeout = 10;
            readTimeout = 10;
            writeTimeout = 10;
        }

        public Builder(String url) {
            params = new HashMap<>();
            connectTimeout = 10;
            readTimeout = 10;
            writeTimeout = 10;
            this.url = url;
        }

        public Builder get() {
            type = REQUEST_TYPE_GET;
            return this;
        }

        public Builder post() {
            type = REQUEST_TYPE_POST;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder path(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder params(Map<String, String> params) {
            for (String key : params.keySet()) {
                this.params.put(key, params.get(key));
            }
            return this;
        }

        public Builder params(Map<String, Object> params, boolean withObj) {
            this.params.putAll(params);
            return this;
        }

        public Builder param(String key, int value) {
            this.params.put(key, value);
            return this;
        }

        public Builder param(String key, long value) {
            this.params.put(key, value);
            return this;
        }

        public Builder param(String key, float value) {
            this.params.put(key, value);
            return this;
        }

        public Builder param(String key, double value) {
            this.params.put(key, value);
            return this;
        }

        public Builder param(String key, boolean value) {
            this.params.put(key, value);
            return this;
        }

        public Builder file(String key, String value) {
            if (files == null) {
                files = new HashMap<>();
            }
            this.files.put(key, value);
            return this;
        }

        public Builder param(String key, String value) {
            this.params.put(key, value);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder connectTimeout(long connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(long readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder writeTimeout(long writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public Builder requestBody(RequestBody requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public void request(Request request) {
            this.request = request;
        }

        public Builder client(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public <T> Builder asString() {
            this.parser = new StringParser<T>(String.class);
            return this;
        }

        public <T> Builder asObject(Type targetClass) {
            this.parser = new ObjectParser<T>(targetClass);
            return this;
        }

        public <T> Builder asList(Type targetClass) {
            Type type = ParameterizedTypeImpl.get(List.class, targetClass);
            this.parser = new ObjectParser<T>(type);
            return this;
        }

        public <T> Builder asMap(Type kType, Type vType) {
            Type type = ParameterizedTypeImpl.getParameterized(Map.class, kType, vType);
            this.parser = new ObjectParser<T>(type);
            return this;
        }

        public void callback(OnResponseListener<?> response) {
            this.response = response;
        }

        public <T> IRequest result(OnResponseListener<T> response) {
            this.response = response;
            return new OkRequest<>(call(), (Parser<T>) parser, response).enqueue();
        }

        /**
         * 下载文件、上传文件
         *
         * @param response
         * @param <T>
         */
        public <T> void result(OnProgressRespResult<T> response) {
            this.response = response;
            if (filePath != null && filePath.length() > 0) {
                // 下载文件
                this.parser = new DownloadParser(String.class, filePath);
            }
            new OkRequest<>(call2(), (Parser<T>) parser, response).enqueue();
        }

        public Response result() throws IOException {
            return call().execute();
        }

        private Call call() {
            if (request == null) {
                request = getRequest();
            }

            if (okHttpClient == null) {
                okHttpClient = OkHttp.getOkHttpClient();
            }
            return okHttpClient.newCall(request);
        }

        private Call call2() {
            if (request == null) {
                request = getRequest();
            }

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.writeTimeout(30, TimeUnit.SECONDS);

            builder.retryOnConnectionFailure(true);
            builder.followRedirects(true); // 请求支持重定向
            if (filePath != null && filePath.length() > 0) {
                // 下载文件，进度监听
                if (response instanceof OnProgressRespResult) {
                    ResponseInterceptor interceptor = new ResponseInterceptor((OnProgressRespResult) response);
                    builder.networkInterceptors().add(interceptor);
                }
            }
            builder.addInterceptor(new RetryInterceptor(2)); // 弱网环境，网络请求失败了，重试2次

            okHttpClient = builder.build();
            return okHttpClient.newCall(request);
        }

        private Request getRequest() {
            StringBuilder sbLog = new StringBuilder();
            Request.Builder requestBuilder = new Request.Builder();
            Map<String, String> allHeaders = new HashMap<>();
            Map<String, String> globalHeaders = OkHttp.getHeaders();
            if (globalHeaders != null && globalHeaders.size() > 0) {
                allHeaders.putAll(globalHeaders);
            }

            if (headers != null && headers.size() > 0) {
                allHeaders.putAll(headers);
            }

            if (allHeaders.size() > 0) {
                requestBuilder.headers(Headers.of(allHeaders));
            }
            sbLog.append("headers:").append(allHeaders);

            if (type == REQUEST_TYPE_GET) {
                requestBuilder.get();
                if (params != null && params.size() > 0) {
                    url = UrlUtils.getUrlWithParams(url, params);
                }

                sbLog.append("\nGET");
                sbLog.append("\nparams:").append(params);
                if (filePath != null && filePath.length() > 0) {
                    // 下载文件
                    sbLog.append("\nfilePath:").append(filePath);
                }
            } else if (type == REQUEST_TYPE_POST) {
                sbLog.append("\nPOST");
                sbLog.append("\nparams:").append(params);

                if (files != null && files.size() > 0) {
                    // 上传文件
                    if (requestBody == null) {
                        requestBody = getRequestFileBody();
                    }
                    sbLog.append("\nfiles:").append(files);
                } else {
                    if (requestBody == null) {
                        requestBody = getRequestBody();
                    }
                }
                requestBuilder.post(requestBody);
            }

            requestBuilder.url(url);
            requestBuilder.tag(url);
            sbLog.append("\nurl:").append(url);
            MLog.i(sbLog.toString());

            return requestBuilder.build();
        }

        private RequestBody getRequestBody() {
            FormBody.Builder builder = new FormBody.Builder();
            try {
                for (String key : params.keySet()) {
                    String value = String.valueOf(params.get(key));
                    if (!"null".equals(value)) {
                        builder.addEncoded(key, URLEncoder.encode(value, "UTF-8"));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return builder.build();
        }

        private RequestBody getRequestFileBody() {
            if (params == null) {
                params = new HashMap<>();
            }

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            if (params != null && !params.isEmpty()) {
                for (HashMap.Entry<String, Object> entry : params.entrySet()) {
                    String value = entry.getValue() == null ? "" : String.valueOf(entry.getValue());
                    builder.addFormDataPart(entry.getKey(), value);
                }
            }

            if (files != null && !files.isEmpty()) {
                for (HashMap.Entry<String, String> entry : files.entrySet()) {
                    String filePath = entry.getValue();
                    File file = new File(filePath);
                    if (!file.exists() || !file.isFile()) {
                        MLog.e("============!file.exists() || !file.isFile()====================");
                        continue;
                    }

                    String fileName = file.getName();
                    try {
                        fileName = URLEncoder.encode(file.getName(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    MLog.i("fileName = " + fileName);

                    builder.addFormDataPart(entry.getKey(), fileName,
                            RequestBody.create(file, MediaType.parse("application/octet-stream;charset=utf-8")));
                }
            }

            if (response instanceof OnProgressRespResult) {
                OnProgressRespResult onProgressRespResult = (OnProgressRespResult) response;
                return new FileRequestBody(builder.build(),onProgressRespResult);
            }
            return builder.build();
        }

    }

    private static OkHttpClient getDefaultOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            // builder.cookieJar(CookieManager.getInstance().getCookieJar());
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true; // 忽略host验证
                }
            });

            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(10, TimeUnit.SECONDS);
            builder.writeTimeout(10, TimeUnit.SECONDS);

            builder.retryOnConnectionFailure(true);
            builder.followRedirects(true); // 请求支持重定向
            builder.addInterceptor(new RetryInterceptor(2)); // 弱网环境，网络请求失败了，重试2次

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 当前将要执行的请求是否已经在执行
     *
     * @param url
     * @return
     */
    public static boolean exists(String url) {
        if (!TextUtils.isEmpty(url)) {
            OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
            Dispatcher dispatcher = okHttpClient.dispatcher();
            for (Call call : dispatcher.queuedCalls()) {
                if (url.equals(call.request().tag())) {
                    return true;
                }
            }

            for (Call call : dispatcher.runningCalls()) {
                if (url.equals(call.request().tag())) {
                    return true;
                }
            }
        }
        return false;
    }

}
