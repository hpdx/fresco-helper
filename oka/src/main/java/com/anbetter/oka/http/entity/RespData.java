package com.anbetter.oka.http.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>
 * Created by android_ls on 2020/3/17 4:02 PM.
 *
 * @author android_ls
 * @version 1.0
 */
public class RespData implements Parcelable {

    public int code;
    public String data;
    public String msg;

    public RespData() {

    }

    public RespData(String msg) {
        this.msg = msg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.data);
        dest.writeString(this.msg);
    }

    protected RespData(Parcel in) {
        this.code = in.readInt();
        this.data = in.readString();
        this.msg = in.readString();
    }

    public static final Parcelable.Creator<RespData> CREATOR = new Parcelable.Creator<RespData>() {
        @Override
        public RespData createFromParcel(Parcel source) {
            return new RespData(source);
        }

        @Override
        public RespData[] newArray(int size) {
            return new RespData[size];
        }
    };

    @Override
    public String toString() {
        return "RespData{" +
                "code=" + code +
                ", data='" + data + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
