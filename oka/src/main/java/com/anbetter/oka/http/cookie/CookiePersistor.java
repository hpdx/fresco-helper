package com.anbetter.oka.http.cookie;

import java.util.Collection;
import java.util.List;

import okhttp3.Cookie;

/**
 * <p>
 * Created by android_ls on 2020-03-05 20:04.
 *
 * @author android_ls
 * @version 1.0
 */
public interface CookiePersistor {

    List<Cookie> loadAll();

    /**
     * Persist all cookies, existing cookies will be overwritten.
     *
     * @param cookies cookies persist
     */
    void saveAll(Collection<Cookie> cookies);

    /**
     * Removes indicated cookies from persistence.
     *
     * @param cookies cookies to remove from persistence
     */
    void removeAll(Collection<Cookie> cookies);

    /**
     * Clear all cookies from persistence.
     */
    void clear();

}
