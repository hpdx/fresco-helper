package com.anbetter.album.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anbetter.album.R;
import com.anbetter.album.constant.Type;
import com.anbetter.album.models.album.entity.PhotoInfo;
import com.anbetter.album.setting.Setting;
import com.anbetter.album.ui.widget.subscaleview.ImageSource;
import com.anbetter.album.ui.widget.subscaleview.SubsamplingScaleImageView;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.fresco.helper.photoview.PhotoDraweeView;
import com.facebook.fresco.helper.photoview.photodraweeview.OnPhotoTapListener;
import com.facebook.fresco.helper.photoview.photodraweeview.OnScaleChangeListener;
import com.facebook.fresco.helper.photoview.photodraweeview.OnViewTapListener;
import com.facebook.fresco.helper.utils.MLog;
import com.facebook.imagepipeline.image.ImageInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;


/**
 * 大图预览界面图片集合的适配器
 * Created by huan on 2017/10/26.
 */
public class PreviewPhotosAdapter extends RecyclerView.Adapter<PreviewPhotosAdapter.PreviewPhotosViewHolder> {
    private ArrayList<PhotoInfo> photoInfos;
    private OnClickListener listener;
    private LayoutInflater inflater;
    private long mMaxValue = 10000;

    public interface OnClickListener {
        void onPhotoClick();

        void onPhotoScaleChanged();
    }

    public PreviewPhotosAdapter(Context cxt, ArrayList<PhotoInfo> photoInfos, OnClickListener listener) {
        this.photoInfos = photoInfos;
        this.inflater = LayoutInflater.from(cxt);
        this.listener = listener;
    }

    @NonNull
    @Override
    public PreviewPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PreviewPhotosViewHolder(inflater.inflate(R.layout.item_preview_photo_easy_photos, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PreviewPhotosViewHolder holder, int position) {
        final String path = photoInfos.get(position).path;
        final String type = photoInfos.get(position).type;
        final double ratio =
                (double) photoInfos.get(position).height / (double) photoInfos.get(position).width;

        holder.ivPlay.setVisibility(View.GONE);
        holder.ivPhoto.setVisibility(View.GONE);
        holder.ivBigPhoto.setVisibility(View.GONE);

        if (type.contains(Type.VIDEO)) {
            holder.ivPhoto.setVisibility(View.VISIBLE);
//            Setting.imageEngine.loadPhoto(holder.ivPhoto.getContext(), path,
//                    holder.ivPhoto);

            holder.ivPlay.setVisibility(View.VISIBLE);
            holder.ivPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toPlayVideo(v, path, type);
                }
            });
        } else if (path.endsWith(Type.GIF) || type.endsWith(Type.GIF)) {
            holder.ivPhoto.setVisibility(View.VISIBLE);
//            Setting.imageEngine.loadGif(holder.ivPhoto.getContext(), path, holder.ivPhoto);
        } else {
            if (ratio > 3 || ratio < 0.34) {
                holder.ivBigPhoto.setVisibility(View.VISIBLE);
                holder.ivBigPhoto.setImage(ImageSource.uri(path));
            } else {
                holder.ivPhoto.setVisibility(View.VISIBLE);
//                Setting.imageEngine.loadPhoto(holder.ivPhoto.getContext(), path, holder.ivPhoto);
            }
        }

        holder.ivBigPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPhotoClick();
            }
        });
        holder.ivBigPhoto.setOnStateChangedListener(new SubsamplingScaleImageView.OnStateChangedListener() {
            @Override
            public void onScaleChanged(float newScale, int origin) {
                listener.onPhotoScaleChanged();
            }

            @Override
            public void onCenterChanged(PointF newCenter, int origin) {

            }
        });
        holder.ivPhoto.setScale(1f);

        final PhotoDraweeView photoDraweeView = holder.ivPhoto;
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();

        Uri uri = Uri.parse(path);
        if (!UriUtil.isNetworkUri(uri)) {
            uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_FILE_SCHEME)
                    .path(path)
                    .build();
        }
        controller.setUri(uri);

        GenericDraweeHierarchy hierarchy = photoDraweeView.getHierarchy();
        hierarchy.setProgressBarImage(new ProgressBarDrawable(){
            @Override
            protected boolean onLevelChange(int level) {
                int progress = (int) (((double)level/mMaxValue) * 100);
                MLog.i("progress = " + progress);
                return super.onLevelChange(progress);
            }
        });

        controller.setOldController(photoDraweeView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                    return;
                }
                photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }

            @Override
            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                super.onIntermediateImageSet(id, imageInfo);
            }
        });
        photoDraweeView.setController(controller.build());

        photoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (listener != null) {
                    listener.onPhotoClick();
                }
            }
        });

        photoDraweeView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (listener != null) {
                    listener.onPhotoClick();
                }
            }
        });

        photoDraweeView.setOnScaleChangeListener(new OnScaleChangeListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                if (listener != null) {
                    listener.onPhotoScaleChanged();
                }
            }
        });
    }

    private void toPlayVideo(View v, String path, String type) {
        if (Setting.videoPreviewCallback != null) {
            Setting.videoPreviewCallback.callback(v, path, type);
        } else {
            Context context = v.getContext();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = getUri(context, path, intent);
            intent.setDataAndType(uri, type);
            context.startActivity(intent);
        }
    }

    private Uri getUri(Context context, String path, Intent intent) {
        File file = new File(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            return FileProvider.getUriForFile(context, Setting.fileProviderAuthority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    @Override
    public int getItemCount() {
        return photoInfos.size();
    }

    public class PreviewPhotosViewHolder extends RecyclerView.ViewHolder {
        public PhotoDraweeView ivPhoto;
        public SubsamplingScaleImageView ivBigPhoto;
        ImageView ivPlay;

        PreviewPhotosViewHolder(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.pdv_photo);
            ivBigPhoto = itemView.findViewById(R.id.iv_big_photo);
            ivPlay = itemView.findViewById(R.id.iv_play);
            ivPhoto.setMaximumScale(5f);
            ivPhoto.setMediumScale(3f);
            ivPhoto.setMinimumScale(1f);

            ivBigPhoto.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
            ivBigPhoto.setMaxScale(5f);
            ivBigPhoto.setMinScale(0.8f);
        }
    }
}