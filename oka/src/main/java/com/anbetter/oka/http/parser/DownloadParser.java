package com.anbetter.oka.http.parser;

import androidx.annotation.NonNull;

import com.anbetter.oka.http.utils.MLog;
import com.google.gson.JsonParseException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * <p>
 * Created by android_ls on 2020/3/17 11:20 AM.
 *
 * @author android_ls
 * @version 1.0
 */
public class DownloadParser<T> extends Parser<T> {

    private String mFilePath;

    public DownloadParser(@NonNull Type targetClass, String filePath) {
        super(targetClass);
        this.mFilePath = filePath;
    }

    @NonNull
    public T parser(@NonNull ResponseBody responseBody) {
        FileOutputStream fileOutputStream = null;
        try {
            MLog.i("filePath: " + mFilePath);
            if (mFilePath != null) {
                fileOutputStream = new FileOutputStream(mFilePath);
                fileOutputStream.write(responseBody.bytes());
                fileOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (T) mFilePath;
    }

    @NonNull
    @Override
    public T parser(@NonNull String data) throws JsonParseException {
        return null;
    }

}
