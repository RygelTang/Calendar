package cn.rygel.gd.widget.calendar.impl.helper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;

import cn.rygel.gd.widget.calendar.bean.CalendarData;
import cn.rygel.gd.widget.calendar.helper.CustomCalendarItem;

public class DefaultCalendarItem implements CustomCalendarItem {

    private int mOfficialHolidayPadding = 20;

    private int mRoundCornerRadius = 10;

    private CalendarItemPaintHolder mCalendarItemPaintHolder = new CalendarItemPaintHolder();

    @Override
    public void drawDateItem(Canvas canvas, Rect bound, CalendarData data, int index) {
        drawDate(canvas,bound,data,index,false);
        drawLunarOrHoliday(canvas,bound,data,index,false);
        // TODO: 2018/12/4 法定节假日显示,下标绘制
    }

    @Override
    public void drawSelectItem(Canvas canvas, Rect bound, CalendarData data, int index) {
        drawBackground(canvas,bound,true);
        drawDate(canvas,bound,data,index,true);
        drawLunarOrHoliday(canvas,bound,data,index,true);
        // TODO: 2018/12/4 法定节假日显示,下标绘制
    }

    @Override
    public void drawTodayItem(Canvas canvas, Rect bound, CalendarData data) {
        drawBackground(canvas,bound,false);
        drawDate(canvas,bound,data,data.mTodayIndex,false);
        drawLunarOrHoliday(canvas,bound,data,data.mTodayIndex,false);
        // TODO: 2018/12/4 法定节假日显示,下标绘制
    }

    private void drawLunarOrHoliday(Canvas canvas, Rect bound, CalendarData data, int index, boolean mode){
        Rect textBound = new Rect();
        if(data.mHolidays[index] != null){
            final TextPaint holidayPaint = mCalendarItemPaintHolder.getHolidayPaint(mode);
            final String holiday = data.mHolidays[index];
            holidayPaint.getTextBounds(holiday,0,holiday.length(),textBound);
            canvas.drawText(holiday,
                    bound.centerX(),
                    bound.top + bound.height() * 2 / 3 + textBound.height() / 2,
                    holidayPaint
            );
        } else {
            final TextPaint lunarPaint = mCalendarItemPaintHolder.getLunarPaint(mode);
            final String lunar = data.mLunars[index];
            lunarPaint.getTextBounds(lunar,0,lunar.length(),textBound);
            canvas.drawText(lunar,
                    bound.centerX(),
                    bound.top + bound.height() * 2 / 3 + textBound.height() / 2,
                    lunarPaint
            );
        }
    }

    private void drawDate(Canvas canvas, Rect bound, CalendarData data, int index, boolean mode){
        final TextPaint datePaint = mCalendarItemPaintHolder.getDatePaint(mode);
        final String date = String.valueOf(index + 1);
        Rect textBound = new Rect();
        datePaint.getTextBounds(date,0,date.length(),textBound);
        canvas.drawText(date,
                bound.centerX(),
                bound.top + bound.height() / 3 + textBound.height() / 2,
                datePaint
        );
    }

    private void drawOfficialHoliday(Canvas canvas, Rect bound, boolean mode){
        final TextPaint officialHolidayPaint = mCalendarItemPaintHolder.getOfficialHolidayPaint(mode);
        final String officialHoliday = "休";
        Rect textBound = new Rect();
        officialHolidayPaint.getTextBounds(officialHoliday,0,officialHoliday.length(),textBound);
        canvas.drawText(officialHoliday,
                bound.right - mOfficialHolidayPadding,
                bound.top + textBound.height() + mOfficialHolidayPadding,
                officialHolidayPaint
        );
    }

    private void drawBackground(Canvas canvas, Rect bound, boolean mode){
        final Paint backgroundPaint = mCalendarItemPaintHolder.getBackgroundPaint(mode);
        final float strokeWidth = backgroundPaint.getStrokeWidth();
        canvas.drawRoundRect(
                new RectF(bound.left + strokeWidth / 2,
                        bound.top + strokeWidth / 2,
                        bound.right - strokeWidth / 2,
                        bound.bottom - strokeWidth / 2),
                mRoundCornerRadius,
                mRoundCornerRadius,
                backgroundPaint);
    }

    @Override
    public void setSelectTextColor(int selectTextColor) {
        mCalendarItemPaintHolder.setSelectTextColor(selectTextColor);
    }

    @Override
    public void setDateTextSize(int dateTextSize) {
        mCalendarItemPaintHolder.setDateTextSize(dateTextSize);
    }

    @Override
    public void setLunarTextSize(int lunarTextSize) {
        mCalendarItemPaintHolder.setLunarTextSize(lunarTextSize);
    }

    @Override
    public void setHolidayTextSize(int holidayTextSize) {
        mCalendarItemPaintHolder.setHolidayTextSize(holidayTextSize);
    }

    @Override
    public void setPrimaryColor(int primaryColor) {
        mCalendarItemPaintHolder.setPrimaryColor(primaryColor);
    }

    @Override
    public void setAccentColor(int accentColor) {
        mCalendarItemPaintHolder.setAccentColor(accentColor);
    }

    @Override
    public void setTextColor(int textColor) {
        mCalendarItemPaintHolder.setTextColor(textColor);
    }

    public void setOfficialHolidayPadding(int officialHolidayPadding) {
        mOfficialHolidayPadding = officialHolidayPadding;
    }

}
