package com.anbetter.album.result;

import android.text.TextUtils;

import com.anbetter.album.constant.Type;
import com.anbetter.album.models.album.entity.PhotoInfo;
import com.anbetter.album.setting.Setting;
import com.anbetter.album.utils.system.SystemUtils;
import java.io.File;
import java.util.ArrayList;

/**
 * 存储的返回图片集
 * Created by huan on 2017/10/24.
 */

public class Result {
    public static ArrayList<PhotoInfo> photoInfos = new ArrayList<>();

    /**
     * @return 0：添加成功 -4：选择结果互斥 -3：文件不存在 -2：超过视频选择数 -1：超过图片选择数
     */
    public static int addPhoto(PhotoInfo photoInfo) {
        final String path = photoInfo.path;
        if (TextUtils.isEmpty(path)) {
            return -3;
        }
        if (SystemUtils.beforeAndroidTen()) {
            final File file = new File(path);
            if (!file.exists() || !file.isFile()) {
                return -3;
            }
        } else {
            //TODO 判断文件是否存在
            //if(AndroidQUtils.isAndroidQFileExists())
        }
        if (Setting.selectMutualExclusion && photoInfos.size() > 0) {
            if (photoInfo.type.contains(Type.VIDEO) != photoInfos.get(0).type.contains(Type.VIDEO)) {
                return -4;
            }
        }
        if (Setting.videoCount != -1 || Setting.pictureCount != -1) {
            int number = getVideoNumber();
            if (photoInfo.type.contains(Type.VIDEO) && number >= Setting.videoCount) {
                return -2;
            }
            number = photoInfos.size() - number;
            if ((!photoInfo.type.contains(Type.VIDEO)) && number >= Setting.pictureCount) {
                return -1;
            }
        }
        photoInfos.add(photoInfo);
        return 0;
    }

    public static void removePhoto(PhotoInfo photoInfo) {
        photoInfos.remove(photoInfo);
    }

    public static void removePhoto(int photoIndex) {
        removePhoto(photoInfos.get(photoIndex));
    }

    public static void removeAll() {
        int size = photoInfos.size();
        for (int i = 0; i < size; i++) {
            removePhoto(0);
        }
    }

    private static int getVideoNumber() {
        int count = 0;
        for (PhotoInfo p : photoInfos) {
            if (p.type.contains(Type.VIDEO)) {
                count += 1;
            }
        }
        return count;
    }

    public static void processOriginal() {
        if (Setting.showOriginalMenu) {
            if (Setting.originalMenuUsable) {
                for (PhotoInfo photoInfo : photoInfos) {
                    photoInfo.selectedOriginal = Setting.selectedOriginal;
                }
            }
        }
    }

    public static void clear() {
        photoInfos.clear();
    }

    public static boolean isEmpty() {
        return photoInfos.isEmpty();
    }

    public static int count() {
        return photoInfos.size();
    }

    /**
     * 获取选择器应该显示的数字
     *
     * @param photoInfo 当前图片
     * @return 选择器应该显示的数字
     */
    public static String getSelectorNumber(PhotoInfo photoInfo) {
        return String.valueOf(photoInfos.indexOf(photoInfo) + 1);
    }

    public static String getPhotoPath(int position) {
        return photoInfos.get(position).path;
    }

    public static String getPhotoType(int position) {
        return photoInfos.get(position).type;
    }

    public static long getPhotoDuration(int position) {
        return photoInfos.get(position).duration;
    }

    public static boolean isSelected(PhotoInfo photoInfo) {
        for (PhotoInfo p : Result.photoInfos) {
            if (p.path != null && p.path.equals(photoInfo.path)) return true;
        }
        return false;
    }
}
