package com.anbetter.album.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anbetter.album.R;
import com.anbetter.album.constant.Type;
import com.anbetter.album.models.album.entity.PhotoInfo;
import com.anbetter.album.setting.Setting;
import com.anbetter.album.utils.media.MediaUtils;

import java.util.ArrayList;

/**
 * 拼图相册适配器
 * Created by huan on 2017/10/23.
 */

public class PuzzleSelectorPreviewAdapter extends RecyclerView.Adapter {

    private ArrayList<PhotoInfo> dataList;
    private LayoutInflater mInflater;
    private OnClickListener listener;

    public PuzzleSelectorPreviewAdapter(Context cxt, ArrayList<PhotoInfo> dataList, OnClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
        this.mInflater = LayoutInflater.from(cxt);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new PhotoViewHolder(mInflater.inflate(R.layout.item_puzzle_selector_preview_easy_photos, parent, false));

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final int p = position;
        PhotoInfo photoInfo = dataList.get(position);
        String path = photoInfo.path;
        String type = photoInfo.type;
        long duration = photoInfo.duration;
        final boolean isGif = path.endsWith(Type.GIF) || type.endsWith(Type.GIF);
        if (Setting.showGif && isGif) {
            Setting.imageEngine.loadGifAsBitmap(((PhotoViewHolder) holder).ivPhoto.getContext(), path, ((PhotoViewHolder) holder).ivPhoto);
            ((PhotoViewHolder) holder).tvType.setText(R.string.gif_easy_photos);
            ((PhotoViewHolder) holder).tvType.setVisibility(View.VISIBLE);
        } else if (Setting.showVideo() && type.contains(Type.VIDEO)) {
            Setting.imageEngine.loadPhoto(((PhotoViewHolder) holder).ivPhoto.getContext(), path, ((PhotoViewHolder) holder).ivPhoto);
            ((PhotoViewHolder) holder).tvType.setText(MediaUtils.format(duration));
            ((PhotoViewHolder) holder).tvType.setVisibility(View.VISIBLE);
        } else {
            Setting.imageEngine.loadPhoto(((PhotoViewHolder) holder).ivPhoto.getContext(), path, ((PhotoViewHolder) holder).ivPhoto);
            ((PhotoViewHolder) holder).tvType.setVisibility(View.GONE);
        }

        ((PhotoViewHolder) holder).ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(p);
            }
        });
    }


    @Override
    public int getItemCount() {
        return null == dataList ? 0 : dataList.size();
    }


    public interface OnClickListener {
        void onDeleteClick(int position);
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        ImageView ivDelete;
        TextView tvType;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            this.ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            this.ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            this.tvType = (TextView) itemView.findViewById(R.id.tv_type);
        }
    }
}
