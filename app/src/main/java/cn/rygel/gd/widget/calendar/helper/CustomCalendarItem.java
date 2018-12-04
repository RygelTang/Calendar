package cn.rygel.gd.widget.calendar.helper;

import android.graphics.Canvas;
import android.graphics.Rect;

import rygel.cn.calendardemo.calendar.bean.CalendarData;

public interface CustomCalendarItem {

    void drawDateItem(Canvas canvas, Rect bound, CalendarData data, int index);

    void drawSelectItem(Canvas canvas, Rect bound, CalendarData data, int index);

    void drawTodayItem(Canvas canvas, Rect bound, CalendarData data);

    void setSelectTextColor(int selectTextColor);

    void setDateTextSize(int dateTextSize);

    void setLunarTextSize(int lunarTextSize);

    void setHolidayTextSize(int holidayTextSize);

    void setPrimaryColor(int primaryColor);

    void setAccentColor(int accentColor);

    void setTextColor(int textColor);

}