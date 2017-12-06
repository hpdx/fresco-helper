package com.facebook.fresco.helper.photoview.anim;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by android_ls on 16/9/18.
 */
public class ViewOptions implements Parcelable {

    public String thumbnail;

    public int startX;
    public int startY;
    public int width;
    public int height;

    // 当前是否是全屏
    public boolean isFullScreen;
    // 当前是否是竖屏
    public boolean isVerticalScreen;
    // 当前view是否在屏幕上
    public boolean isInTheScreen;

    public ViewOptions() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.thumbnail);
        dest.writeInt(this.startX);
        dest.writeInt(this.startY);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeByte(this.isFullScreen ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isVerticalScreen ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isInTheScreen ? (byte) 1 : (byte) 0);
    }

    protected ViewOptions(Parcel in) {
        this.thumbnail = in.readString();
        this.startX = in.readInt();
        this.startY = in.readInt();
        this.width = in.readInt();
        this.height = in.readInt();
        this.isFullScreen = in.readByte() != 0;
        this.isVerticalScreen = in.readByte() != 0;
        this.isInTheScreen = in.readByte() != 0;
    }

    public static final Creator<ViewOptions> CREATOR = new Creator<ViewOptions>() {
        @Override
        public ViewOptions createFromParcel(Parcel source) {
            return new ViewOptions(source);
        }

        @Override
        public ViewOptions[] newArray(int size) {
            return new ViewOptions[size];
        }
    };
}
