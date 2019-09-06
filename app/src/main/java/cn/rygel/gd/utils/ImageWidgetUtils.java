package cn.rygel.gd.utils;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.blankj.utilcode.util.ImageUtils;

import cn.rygel.gd.app.APP;
import cn.rygel.gd.bean.ImageWidgetInfo;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class ImageWidgetUtils {

    public static Bitmap getForegroundOf(ImageWidgetInfo info, int width, int height) {
        return info.getType().getDemoWidgetImage(
                new Rect(0, 0, width, height - AutoSizeUtils.dp2px(APP.getInstance(), 44)),
                info.getTextColor()
        );
    }

    public static Bitmap getForegroundOf(ImageWidgetInfo info) {
        return getForegroundOf(info, info.getBgWidth(), info.getBgHeight());
    }

    public static Bitmap getBackgroundOf(ImageWidgetInfo info) {
        return getBackgroundOf(info, info.getBgWidth(), info.getBgHeight());
    }

    public static Bitmap getBackgroundOf(ImageWidgetInfo info, int width, int height) {
        if (info == null) {
            throw new IllegalArgumentException("info should not be null");
        }
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("widget is in err size");
        }
        if (info.getBgWidth() <= 0 || info.getBgHeight() <= 0) {
            throw new IllegalArgumentException("info background is in err size");
        }
        Bitmap cache;
        if (info.isOnlyColorBg()) {
            cache = createColorBmp(width, height, info.getBgColor());
        } else {
            cache = ImageUtils.getBitmap(info.getBgPath());
            final float widgetRatio = width / (float) height;
            final float bmpRatio = info.getBgWidth() / (float) info.getBgHeight();
            if (widgetRatio > bmpRatio) {
                width = info.getBgWidth();
                height = (int) (width / widgetRatio);
            } else {
                height = info.getBgHeight();
                width = (int) (height * widgetRatio);
            }
            // 对图片进行压缩，提高运行效率
            cache = ImageUtils.compressByScale(cache, info.getBgWidth(), info.getBgHeight());
            // 裁切图片
            cache = ImageUtils.clip(cache, 0, 0, width, height);
            if (cache == null) throw new IllegalArgumentException("img not found");
            if (info.isShouldBlur()) {
                cache = ImageUtils.fastBlur(cache, 1, 25);
            }
        }
        cache = getTransparentBitmap(cache, info.getBgAlpha());
        cache = RoundCornerTransformer.transform(cache, info.getCornerRadius());
        return cache;
    }

    /**
     * 修改图片透明度
     * @param sourceImg
     * @param alpha
     * @return
     */
    private static Bitmap getTransparentBitmap(Bitmap sourceImg, int alpha){
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg
                .getWidth(), sourceImg.getHeight());// 获得图片的ARGB值
        alpha = alpha * 255 / 100;
        for (int i = 0; i < argb.length; i++) {
            argb[i] = (alpha << 24) | (argb[i] & 0x00FFFFFF);
        }
        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg
                .getHeight(), Bitmap.Config.ARGB_8888);
        return sourceImg;
    }

    /**
     * 创建纯色背景图
     * @return
     */
    private static Bitmap createColorBmp(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        bitmap.eraseColor(color);
        return bitmap;
    }

}
