package cn.rygel.gd.bean;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import cn.rygel.gd.app.APP;
import rygel.cn.calendarview.MonthView;

public enum WidgetType {

    MONTH_WIGET() {
        @Override
        Bitmap getDemoWidgetImage(int width, int height) {
            MonthView monthView = new MonthView(APP.getInstance());
            Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            monthView.draw(canvas);
            return result;
        }
    };

    abstract Bitmap getDemoWidgetImage(int width, int height);

}
