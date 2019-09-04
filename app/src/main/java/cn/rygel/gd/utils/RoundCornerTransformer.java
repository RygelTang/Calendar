package cn.rygel.gd.utils;

import android.graphics.*;
import android.os.Build;

public class RoundCornerTransformer {

    public static Bitmap transform(Bitmap inBitmap, float radius) {
        // Alpha is required for this transformation.
        final Bitmap toTransform = getAlphaSafeBitmap(inBitmap);
        Bitmap result = getAlphaSafeBitmap(inBitmap);

        result.setHasAlpha(true);

        final BitmapShader shader = new BitmapShader(
                toTransform,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP
        );
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        final RectF rect = new RectF(0f, 0f, result.getWidth(), result.getHeight());
        if (!result.isMutable()) result = result.copy(result.getConfig(), true);
        final Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawRoundRect(rect, radius, radius, paint);
        canvas.setBitmap(null);

        return result;
    }

    private static Bitmap.Config getAlphaSafeConfig(Bitmap inBitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Avoid short circuiting the sdk check.
            if (Bitmap.Config.RGBA_F16 == inBitmap.getConfig()) { // NOPMD
                return Bitmap.Config.RGBA_F16;
            }
        }
        return Bitmap.Config.ARGB_8888;
    }


    private static Bitmap getAlphaSafeBitmap(Bitmap maybeAlphaSafe) {
        final Bitmap.Config safeConfig = getAlphaSafeConfig(maybeAlphaSafe);
        if (safeConfig == maybeAlphaSafe.getConfig()) {
            return maybeAlphaSafe;
        }
        final Bitmap argbBitmap = Bitmap.createBitmap(maybeAlphaSafe.getWidth(), maybeAlphaSafe.getHeight(), safeConfig);
        new Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0f /*left*/, 0f /*top*/, null /*paint*/);

        // We now own this Bitmap. It's our responsibility to replace it in the pool outside this method
        // when we're finished with it.
        if (!argbBitmap.isMutable()) return argbBitmap.copy(safeConfig, true);
        return argbBitmap;
    }
}