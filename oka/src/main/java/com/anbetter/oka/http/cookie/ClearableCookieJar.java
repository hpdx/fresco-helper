package com.anbetter.oka.http.cookie;

import okhttp3.CookieJar;

/**
 * <p>
 * Created by android_ls on 2020-03-05 19:58.
 *
 * @author android_ls
 * @version 1.0
 */
public interface ClearableCookieJar extends CookieJar {

    /**
     * Clear all the session cookies while maintaining the persisted ones.
     */
    void clearSession();

    /**
     * Clear all the cookies from persistence and from the cache.
     */
    void clear();

}
