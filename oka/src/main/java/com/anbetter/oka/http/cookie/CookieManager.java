package com.anbetter.oka.http.cookie;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * <p>
 * Created by android_ls on 2020-03-09 19:25.
 *
 * @author android_ls
 * @version 1.0
 */
public class CookieManager {

    private Context mContext;
    private List<Cookie> mCookies;
    private CookieJar mCookieJar;
    private CookiePersistor mCookiePersistor;

    private static CookieManager sInstance;

    private CookieManager() {
    }

    public static CookieManager getInstance() {
        if (sInstance == null) {
            synchronized (CookieManager.class) {
                if (sInstance == null) {
                    sInstance = new CookieManager();
                }
            }
        }
        return sInstance;
    }

    public static void init(Context context) {
        getInstance().mContext = context;
    }

    /**
     * 获取全局的CookieStore
     *
     * @return
     */
    public List<Cookie> getCookies(HttpUrl httpUrl) {
        if (mCookieJar != null) {
            return mCookieJar.loadForRequest(httpUrl);
        }
        return null;
    }

    public void setCookies(List<Cookie> mCookies) {
        this.mCookies = mCookies;
    }

    public CookieJar getCookieJar(String url) {
        if (mCookieJar == null) {
            // cookie持久化存储，如果cookie不过期，则一直有效
            mCookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mContext));
        }

        // 同步cookies
        if (mCookies != null && mCookies.size() > 0) {
            HttpUrl httpUrl = HttpUrl.parse(url + "method");
            if (httpUrl != null) {
                mCookieJar.saveFromResponse(httpUrl, mCookies);
            }
        }

        return mCookieJar;
    }

}
