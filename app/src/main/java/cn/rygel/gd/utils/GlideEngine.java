package cn.rygel.gd.utils;

//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.zhihu.matisse.engine.ImageEngine;
//
//public class GlideEngine implements ImageEngine {
//
//    @Override
//    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
//        Glide.with(context)
//                .load(uri)
//                .apply(new RequestOptions()
//                        .placeholder(placeholder)
//                        .centerCrop()
//                )
//                .into(imageView);
//    }
//
//    @Override
//    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
//        Glide.with(context)
//                .load(uri)
//                .apply(new RequestOptions()
//                        .placeholder(placeholder)
//                        .centerCrop()
//                )
//                .into(imageView);
//    }
//
//    @Override
//    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
//        Glide.with(context)
//                .load(uri)
//                .into(imageView);
//    }
//
//    @Override
//    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
//        Glide.with(context)
//                .load(uri)
//                .into(imageView);
//    }
//
//    @Override
//    public boolean supportAnimatedGif() {
//        return true;
//    }
//
//}
