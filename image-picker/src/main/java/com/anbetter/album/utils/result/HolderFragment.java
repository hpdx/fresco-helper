package com.anbetter.album.utils.result;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anbetter.album.EasyPhotos;
import com.anbetter.album.callback.PuzzleCallback;
import com.anbetter.album.callback.SelectCallback;
import com.anbetter.album.engine.ImageEngine;
import com.anbetter.album.models.album.entity.PhotoInfo;
import com.anbetter.album.ui.EasyPhotosActivity;
import com.anbetter.album.ui.PuzzleActivity;

import java.util.ArrayList;

/**
 * HolderFragment
 *
 * @author joker
 * @date 2019/4/9.
 */
public class HolderFragment extends Fragment {

    private static final int HOLDER_SELECT_REQUEST_CODE = 0x44;
    private static final int HOLDER_PUZZLE_REQUEST_CODE = 0x55;
    private SelectCallback mSelectCallback;
    private PuzzleCallback mPuzzleCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startEasyPhoto(SelectCallback callback) {
        mSelectCallback = callback;
        EasyPhotosActivity.start(this, HOLDER_SELECT_REQUEST_CODE);
    }

    public void startPuzzleWithPhotos(ArrayList<PhotoInfo> photoInfos, boolean replaceCustom, @NonNull ImageEngine imageEngine, PuzzleCallback callback) {
        mPuzzleCallback = callback;
        PuzzleActivity.startWithPhotos(this, photoInfos, HOLDER_PUZZLE_REQUEST_CODE, replaceCustom, imageEngine);
    }

    public void startPuzzleWithPaths(ArrayList<String> paths, boolean replaceCustom, @NonNull ImageEngine imageEngine, PuzzleCallback callback) {
        mPuzzleCallback = callback;
        PuzzleActivity.startWithPaths(this, paths, HOLDER_PUZZLE_REQUEST_CODE, replaceCustom, imageEngine);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case HOLDER_SELECT_REQUEST_CODE:
                    if (mSelectCallback != null) {
                        Log.i("MLog", "=============onActivityResult=================");

                        ArrayList<PhotoInfo> resultPhotoInfos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                        ArrayList<String> resultPaths = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS);
                        boolean selectedOriginal = data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);

                        Log.i("MLog", "=======>selectedOriginal = " + selectedOriginal);
                        if (resultPhotoInfos != null) {
                            Log.i("MLog", "=======>resultPhotoInfos = " + resultPhotoInfos.toString());
                        }

                        if (resultPaths != null) {
                            Log.i("MLog", "=======>resultPaths = " + resultPaths.toString());
                        }

                        mSelectCallback.onResult(resultPhotoInfos, resultPaths, selectedOriginal);
                    }
                    break;
                case HOLDER_PUZZLE_REQUEST_CODE:
                    if (mPuzzleCallback != null) {
                        PhotoInfo puzzlePhotoInfo = data.getParcelableExtra(EasyPhotos.RESULT_PHOTOS);
                        String puzzlePath = data.getStringExtra(EasyPhotos.RESULT_PATHS);
                        mPuzzleCallback.onResult(puzzlePhotoInfo, puzzlePath);
                    }
                    break;
                default:
                    Log.e("EasyPhotos", "requestCode error : " + requestCode);
                    break;
            }
        } else {
            Log.e("EasyPhotos", "resultCode is not RESULT_OK: " + resultCode);
        }
    }
}
