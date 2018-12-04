package cn.rygel.gd.widget.calendar.helper;

import android.graphics.Canvas;
import android.graphics.Rect;

public interface CustomCalendarWeekItem {

    void drawWeekDays(Canvas canvas, Rect bound, String[] weekdays);
    void setWeekDayTextSize(int textSize);
    void setWeekDayTextColor(int textColor);

}
