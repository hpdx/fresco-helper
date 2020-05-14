package com.anbetter.oka.http.exception;

import androidx.annotation.Nullable;

import java.io.EOFException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Http 状态码 小于200或者大于等于300时,或者ResponseBody等于null，抛出此异常
 * 可通过{@link #getLocalizedMessage()}方法获取code
 * <p>
 * Created by android_ls on 2020/3/17 7:16 PM.
 *
 * @author android_ls
 * @version 1.0
 */
public final class HttpStatusCodeException extends IOException {

    private String statusCode; //Http响应状态吗
    private String result;    //返回结果
    private String requestMethod; //请求方法，Get/Post等
    private String requestUrl; //请求Url及参数
    private Headers responseHeaders; //响应头

    public HttpStatusCodeException(Response response) {
        this(response, null);
    }

    public HttpStatusCodeException(Response response, String result) {
        super(response.message());
        statusCode = String.valueOf(response.code());
        Request request = response.request();
        requestMethod = request.method();
        requestUrl = getEncodedUrlAndParams(request);
        responseHeaders = response.headers();
        this.result = result;
    }

    @Nullable
    @Override
    public String getLocalizedMessage() {
        return statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public Headers getResponseHeaders() {
        return responseHeaders;
    }

    public String getResult() {
        return result;
    }

    public String getEncodedUrlAndParams(Request request) {
        String result;
        try {
            result = getRequestParams(request);
        } catch (IOException e) {
            e.printStackTrace();
            result = request.url().toString();
        }
        try {
            return URLDecoder.decode(result);
        } catch (Exception e) {
            return result;
        }
    }

    private String getRequestParams(Request request) throws IOException {
        RequestBody body = request.body();
        HttpUrl.Builder urlBuilder = request.url().newBuilder();

        if (body instanceof MultipartBody) {
            MultipartBody multipartBody = (MultipartBody) body;
            List<MultipartBody.Part> parts = multipartBody.parts();
            StringBuilder fileBuilder = new StringBuilder();
            for (int i = 0, size = parts.size(); i < size; i++) {
                MultipartBody.Part part = parts.get(i);
                RequestBody requestBody = part.body();
                Headers headers = part.headers();
                if (headers == null || headers.size() == 0) continue;
                String[] split = headers.value(0).split(";");
                String name = null, fileName = null;
                for (String s : split) {
                    if (s.equals("form-data")) continue;
                    String[] keyValue = s.split("=");
                    if (keyValue.length < 2) continue;
                    String value = keyValue[1].substring(1, keyValue[1].length() - 1);
                    if (name == null) {
                        name = value;
                    } else {
                        fileName = value;
                        break;
                    }
                }
                if (name == null) continue;
                if (requestBody.contentLength() < 1024) {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);
                    String value = buffer.readUtf8();
                    urlBuilder.addQueryParameter(name, value);
                } else {
                    if (fileBuilder.length() > 0) {
                        fileBuilder.append("&");
                    }
                    fileBuilder.append(name).append("=").append(fileName);
                }
            }
            return urlBuilder.toString() + "\n\nfiles = " + fileBuilder.toString();
        }

        if (body != null) {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            if (!isPlaintext(buffer)) {
                return urlBuilder.toString() + "\n\n(binary "
                        + body.contentLength() + "-byte body omitted)";
            } else {
                return urlBuilder.toString() + "\n\n" + buffer.readUtf8();
            }
        }
        return urlBuilder.toString();
    }

    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + ":" +
                " Method=" + requestMethod +
                " Code=" + statusCode +
                "\nmessage = " + getMessage() +
                "\n\n" + requestUrl +
                "\n\n" + responseHeaders +
                "\n" + result;
    }

}
