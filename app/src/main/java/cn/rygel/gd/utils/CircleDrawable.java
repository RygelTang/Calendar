package cn.rygel.gd.utils;

import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class CircleDrawable extends ShapeDrawable {

    public CircleDrawable(int color) {
        super();
        setShape(new OvalShape());
        setColorFilter(color, PorterDuff.Mode.ADD);
    }

}
