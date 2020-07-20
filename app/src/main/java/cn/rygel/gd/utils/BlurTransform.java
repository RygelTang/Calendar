package cn.rygel.gd.utils;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class BlurTransform extends BitmapTransformation {

    private static final String ID = "cn.rygel.gd.utils.Blur";

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return ImageUtils.fastBlur(toTransform, 1, 25);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID.getBytes());
    }

}
