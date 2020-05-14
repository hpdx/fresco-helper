package com.anbetter.album.models.album.entity;

import java.util.ArrayList;

/**
 * 专辑项目实体类
 * Created by huan on 2017/10/20.
 */

public class AlbumItem {
    public String name;
    public String coverImagePath;
    public ArrayList<PhotoInfo> photoInfos;

    AlbumItem(String name, String coverImagePath) {
        this.name = name;
        this.coverImagePath = coverImagePath;
        this.photoInfos = new ArrayList<>();
    }

    public void addImageItem(PhotoInfo imageItem) {
        this.photoInfos.add(imageItem);
    }

    public void addImageItem(int index, PhotoInfo imageItem) {
        this.coverImagePath = imageItem.path;
        this.photoInfos.add(index, imageItem);
    }
}
