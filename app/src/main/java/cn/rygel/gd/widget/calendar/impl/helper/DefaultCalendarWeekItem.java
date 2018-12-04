package cn.rygel.gd.widget.calendar.impl.helper;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextPaint;

import rygel.cn.calendardemo.calendar.helper.CustomCalendarWeekItem;

public class DefaultCalendarWeekItem implements CustomCalendarWeekItem {

    private WeekItemPaintHolder mWeekItemPaintHolder = new WeekItemPaintHolder();

    @Override
    public void drawWeekDays(Canvas canvas, Rect bound, String[] weekdays) {
        final TextPaint paint = mWeekItemPaintHolder.getWeekDayPaint();
        final int itemWidth = bound.width() / weekdays.length;
        for(int i = 0;i < weekdays.length;i++){
            Rect textBound = new Rect();
            paint.getTextBounds(weekdays[i], 0, weekdays[i].length(), textBound);
            final int x = bound.left + i * itemWidth + itemWidth / 2;
            final int y = bound.centerY() + textBound.height() / 2;
            canvas.drawText(weekdays[i], x, y, paint);
        }
    }

    @Override
    public void setWeekDayTextSize(int textSize) {
        mWeekItemPaintHolder.setWeekDayTextSize(textSize);
    }

    @Override
    public void setWeekDayTextColor(int textColor) {
        mWeekItemPaintHolder.setWeekDayTextColor(textColor);
    }
}
