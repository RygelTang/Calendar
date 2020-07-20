package cn.rygel.gd.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bilibili.boxing.loader.IBoxingCrop;
import com.bilibili.boxing.model.config.BoxingCropOption;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.model.AspectRatio;

public class CustomBoxingCrop implements IBoxingCrop {

    private AspectRatio[] mAspectRatios;

    public CustomBoxingCrop(AspectRatio[] aspectRatios) {
        mAspectRatios = aspectRatios;
    }

    public CustomBoxingCrop() { }

    @Override
    public void onStartCrop(Context context, Fragment fragment, @NonNull BoxingCropOption cropConfig, @NonNull String path, int requestCode) {
        Uri uri = new Uri.Builder()
                .scheme("file")
                .appendPath(path)
                .build();
        UCrop.Options crop = new UCrop.Options();
        // do not copy exif information to crop pictures
        // because png do not have exif and png is not Distinguishable
        crop.setCompressionFormat(Bitmap.CompressFormat.PNG);
        crop.withMaxResultSize(cropConfig.getMaxWidth(), cropConfig.getMaxHeight());
        if (mAspectRatios == null) {
            crop.withAspectRatio(cropConfig.getAspectRatioX(), cropConfig.getAspectRatioY());
        } else {
            crop.setAspectRatioOptions(0, mAspectRatios);
        }
        UCrop.of(uri, cropConfig.getDestination())
                .withOptions(crop)
                .start(context, fragment, requestCode);
    }

    @Override
    public Uri onCropFinish(int resultCode, Intent data) {
        if (data == null) {
            return null;
        }
        Throwable throwable = UCrop.getError(data);
        if (throwable != null) {
            return null;
        }
        return UCrop.getOutput(data);
    }

}
