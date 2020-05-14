package com.anbetter.oka.http.utils;

import java.util.Map;

/**
 * <p>
 * Created by android_ls on 2020/3/19 5:13 PM.
 *
 * @author android_ls
 * @version 1.0
 */
public class UrlUtils {

    /**
     * get请求拼接参数
     * @param url
     * @param params
     * @return
     */
    public static String getUrlWithParams(String url, Map<String, Object> params) {
        StringBuilder b = new StringBuilder(url);
        for (String key : params.keySet()) {
            if (!url.contains("?")) {
                b.append("?");
                b.append(key).append("=").append(params.get(key));
                url = b.toString();
            } else if (url.contains("?")) {
                b.append("&");
                b.append(key).append("=").append(params.get(key));
                url = b.toString();
            }
        }
        return b.toString();
    }


}
