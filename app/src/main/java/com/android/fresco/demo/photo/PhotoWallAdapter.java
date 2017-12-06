package com.android.fresco.demo.photo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.fresco.demo.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.Phoenix;
import com.facebook.fresco.helper.photoview.entity.PhotoInfo;
import com.facebook.fresco.helper.utils.DensityUtil;

import java.util.ArrayList;

/**
 * Created by android_ls on 16/11/2.
 */

public class PhotoWallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PhotoInfo> mPhotos;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;

    public PhotoWallAdapter(ArrayList<PhotoInfo> photos, OnItemClickListener onItemClickListener) {
        this.mPhotos = photos;
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }

        final PhotoViewHolder photoViewHolder = new PhotoViewHolder(mLayoutInflater.inflate(R.layout.photo_wall_item, parent, false));
        photoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, mPhotos, photoViewHolder.getAdapterPosition());
                }
            }
        });

        return photoViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PhotoViewHolder)holder).bind(mPhotos.get(position));
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    private static class PhotoViewHolder extends RecyclerView.ViewHolder {

        private int itemDimensionSize;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            itemDimensionSize = (DensityUtil.getDisplayWidth(itemView.getContext())
                    - DensityUtil.dipToPixels(itemView.getContext(), 17f)) / 4;
            ViewGroup.LayoutParams vlp = itemView.getLayoutParams();
            vlp.width = itemDimensionSize;
            vlp.height = itemDimensionSize;
        }

        public void bind(final PhotoInfo photoInfo) {
            Phoenix.with((SimpleDraweeView)itemView)
                    .setWidth(itemDimensionSize)
                    .setHeight(itemDimensionSize)
//                    .setNeedBlur(true)
                    .load(photoInfo.thumbnailUrl);
        }
    }

}
