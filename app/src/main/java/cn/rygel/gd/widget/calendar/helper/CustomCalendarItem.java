package cn.rygel.gd.widget.calendar.helper;

import android.graphics.Canvas;
import android.graphics.Rect;

import cn.rygel.gd.widget.calendar.bean.CalendarData;

public interface CustomCalendarItem {

    void drawDateItem(Canvas canvas, Rect bound, CalendarData data, int index);

    void drawSelectItem(Canvas canvas, Rect bound, CalendarData data, int index);

    void drawTodayItem(Canvas canvas, Rect bound, CalendarData data);

    void setSelectTextColor(int selectTextColor);

    void setDateTextSize(int dateTextSize);

    void setLunarTextSize(int lunarTextSize);

    void setHolidayTextSize(int holidayTextSize);

    void setPrimaryColor(int primaryColor);

    void setTextColor(int textColor);

    void setHolidayTextColor(int holidayTextColor);

    void setHolidayBreakTextColor(int holidayBreakTextColor);

    void setSolarTermsTextColor(int solarTermsTextColor) ;


}