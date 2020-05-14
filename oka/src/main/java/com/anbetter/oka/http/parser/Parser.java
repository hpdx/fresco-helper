package com.anbetter.oka.http.parser;

import androidx.annotation.NonNull;

import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * <p>
 * Created by android_ls on 2020/3/16 12:25 PM.
 *
 * @author android_ls
 * @version 1.0
 */
public abstract class Parser<T> {

    public Type mType;

    public Parser(@NonNull Type targetClass) {
       this.mType = targetClass;
    }

    @NonNull
    public abstract T parser(@NonNull String data) throws JsonParseException;

}
