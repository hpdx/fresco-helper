package com.anbetter.oka.http.parser;

import androidx.annotation.NonNull;

import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * <p>
 * Created by android_ls on 2020/3/16 12:26 PM.
 *
 * @author android_ls
 * @version 1.0
 */
public class ObjectParser<T> extends Parser<T> {

    public ObjectParser(@NonNull Type targetClass) {
        super(targetClass);
    }

    @NonNull
    @Override
    public T parser(@NonNull String data) throws JsonParseException {
        return GsonConvert.convert(data, mType);
    }

}
