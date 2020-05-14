package com.anbetter.album.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anbetter.album.R;
import com.anbetter.album.constant.Type;
import com.anbetter.album.result.Result;
import com.anbetter.album.setting.Setting;
import com.anbetter.album.ui.widget.PressedImageView;
import com.anbetter.album.utils.media.MediaUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.Phoenix;
import com.facebook.fresco.helper.utils.DensityUtil;

/**
 * 预览所有选中图片集合的适配器
 * Created by huan on 2017/12/1.
 */

public class PreviewPhotosFragmentAdapter extends RecyclerView.Adapter<PreviewPhotosFragmentAdapter.PreviewPhotoVH> {
    private LayoutInflater inflater;
    private OnClickListener listener;
    private int checkedPosition = -1;
    private Context mContext;

    public PreviewPhotosFragmentAdapter(Context context, OnClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.mContext = context;
    }

    @Override
    public PreviewPhotoVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PreviewPhotoVH(inflater.inflate(R.layout.item_preview_selected_photos_easy_photos, parent, false));
    }

    @Override
    public void onBindViewHolder(PreviewPhotoVH holder, int position) {
        final int p = position;
        String path = Result.getPhotoPath(position);
        String type = Result.getPhotoType(position);
        long duration = Result.getPhotoDuration(position);

        final boolean isGif = path.endsWith(Type.GIF) || type.endsWith(Type.GIF);
        if (Setting.showGif && isGif) {
            // Setting.imageEngine.loadGifAsBitmap(holder.ivPhoto.getContext(), path, holder.ivPhoto);
            holder.tvType.setText(R.string.gif_easy_photos);
            holder.tvType.setVisibility(View.VISIBLE);
        } else if (Setting.showVideo() && type.contains(Type.VIDEO)) {
            // Setting.imageEngine.loadPhoto(holder.ivPhoto.getContext(), path, holder.ivPhoto);
            holder.tvType.setText(MediaUtils.format(duration));
            holder.tvType.setVisibility(View.VISIBLE);
        } else {
            // Setting.imageEngine.loadPhoto(holder.ivPhoto.getContext(), path, holder.ivPhoto);
            holder.tvType.setVisibility(View.GONE);
        }

        SimpleDraweeView simpleDraweeView = holder.ivPhoto;
        Phoenix.with(simpleDraweeView)
                .setWidth(DensityUtil.dip2px(mContext, 56))
                .setHeight(DensityUtil.dip2px(mContext, 56))
                .load(path);

        if (checkedPosition == p) {
            holder.frame.setVisibility(View.VISIBLE);
        } else {
            holder.frame.setVisibility(View.GONE);
        }
        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPhotoClick(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Result.count();
    }

    public void setChecked(int position) {
        if (checkedPosition == position) {
            return;
        }
        checkedPosition = position;
        notifyDataSetChanged();
    }

    class PreviewPhotoVH extends RecyclerView.ViewHolder {
        PressedImageView ivPhoto;
        View frame;
        TextView tvType;

        public PreviewPhotoVH(View itemView) {
            super(itemView);
            ivPhoto = (PressedImageView) itemView.findViewById(R.id.iv_photo);
            frame = itemView.findViewById(R.id.v_selector);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
        }
    }

    public interface OnClickListener {
        void onPhotoClick(int position);
    }
}
