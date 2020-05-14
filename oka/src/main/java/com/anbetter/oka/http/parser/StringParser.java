package com.anbetter.oka.http.parser;

import androidx.annotation.NonNull;

import com.anbetter.oka.http.utils.MLog;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * <p>
 * Created by android_ls on 2020/3/17 3:13 PM.
 *
 * @author android_ls
 * @version 1.0
 */
public class StringParser<T> extends Parser<T> {

    public StringParser(@NonNull Type targetClass) {
        super(targetClass);
    }

    @NonNull
    @Override
    public T parser(@NonNull String data) throws JsonParseException {
        MLog.i("result: " + data);
        return (T) data;
    }

}
