package cn.rygel.gd.utils;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bilibili.boxing.loader.IBoxingCallback;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class GlideLoader implements IBoxingMediaLoader {

    @Override
    public void displayThumbnail(@NonNull ImageView img, @NonNull String absPath, int width, int height) {
        Glide.with(img).load(absPath).apply(new RequestOptions().override(width, height)).into(img);
    }

    @Override
    public void displayRaw(@NonNull ImageView img, @NonNull String absPath, int width, int height, IBoxingCallback callback) {
        Glide.with(img)
                .load(absPath)
                .apply(new RequestOptions().override(width, height))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (callback != null) {
                            callback.onFail(e);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (callback != null) {
                            callback.onSuccess();
                        }
                        return false;
                    }
                })
                .into(img);
    }
}