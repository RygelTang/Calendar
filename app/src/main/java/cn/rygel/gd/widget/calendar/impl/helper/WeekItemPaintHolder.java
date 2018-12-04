package cn.rygel.gd.widget.calendar.impl.helper;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

public class WeekItemPaintHolder {

    private TextPaint mWeekDayPaint = new TextPaint();

    private int mWeedDayTextColor = Color.BLACK;

    public void setWeekDayTextSize(int weekDayTextSize){
        mWeekDayPaint.setTextSize(weekDayTextSize);
    }

    public void setWeekDayTextColor(int weekDayTextColor){
        mWeedDayTextColor = weekDayTextColor;
    }

    public TextPaint getWeekDayPaint(){
        mWeekDayPaint.setColor(mWeedDayTextColor);
        mWeekDayPaint.setTextAlign(Paint.Align.CENTER);
        return mWeekDayPaint;
    }

}
