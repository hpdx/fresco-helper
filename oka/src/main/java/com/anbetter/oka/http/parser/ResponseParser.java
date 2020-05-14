package com.anbetter.oka.http.parser;

import androidx.annotation.NonNull;

import com.anbetter.oka.http.entity.RespData;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * {"code":1,"data":null,"msg":"短信验证码已发送"}
 * <p>
 * Created by android_ls on 2020/3/17 3:34 PM.
 *
 * @author android_ls
 * @version 1.0
 */
public class ResponseParser<T> extends Parser<T> {

    public ResponseParser(@NonNull Type targetClass) {
        super(targetClass);
    }

    @NonNull
    @Override
    public T parser(@NonNull String responseData) throws JsonParseException {
        RespData data = new RespData();
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            data.code = jsonObject.optInt("code");
            JSONObject jsonData = jsonObject.optJSONObject("data");
            if (jsonData != null) {
                data.data = jsonData.toString();
            }
            data.msg = jsonObject.optString("msg");
            return (T) data;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (T) data;
    }

}
