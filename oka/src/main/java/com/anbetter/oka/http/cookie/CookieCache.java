package com.anbetter.oka.http.cookie;

import java.util.Collection;

import okhttp3.Cookie;

/**
 * <p>
 * Created by android_ls on 2020-03-05 20:00.
 *
 * @author android_ls
 * @version 1.0
 */
public interface CookieCache extends Iterable<Cookie> {

    /**
     * Add all the new cookies to the session, existing cookies will be overwritten.
     *
     * @param cookies
     */
    void addAll(Collection<Cookie> cookies);

    /**
     * Clear all the cookies from the session.
     */
    void clear();

}
