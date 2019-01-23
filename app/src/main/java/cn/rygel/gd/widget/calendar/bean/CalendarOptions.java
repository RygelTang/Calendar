package cn.rygel.gd.widget.calendar.bean;

import android.graphics.Color;

public class CalendarOptions {

    private int mDateOffset = 0;
    private int mWeekBarHeight = 120;
    private int mWeekDayTextSize = 32;
    private int mDateTextSize = 48;
    private int mLunarTextSize = 32;
    private int mHolidayTextSize = 28;
    private int mChildPaddingLeft = 10;
    private int mChildPaddingRight = 10;
    private int mChildPaddingTop = 10;
    private int mChildPaddingBottom = 10;
    private int mWeekDayTextColor = Color.BLACK;
    private int mSelectTextColor = Color.WHITE;
    private int mPrimaryColor = Color.BLUE;
    private int mAccentColor = Color.GREEN;
    private int mTextColor = Color.BLACK;

    public int getDateOffset() {
        return mDateOffset;
    }

    public CalendarOptions setDateOffset(int dateOffset) {
        mDateOffset = dateOffset;
        return this;
    }

    public int getWeekBarHeight() {
        return mWeekBarHeight;
    }

    public CalendarOptions setWeekBarHeight(int weekBarHeight) {
        mWeekBarHeight = weekBarHeight;
        return this;
    }

    public int getWeekDayTextSize() {
        return mWeekDayTextSize;
    }

    public CalendarOptions setWeekDayTextSize(int weekDayTextSize) {
        mWeekDayTextSize = weekDayTextSize;
        return this;
    }

    public int getWeekDayTextColor() {
        return mWeekDayTextColor;
    }

    public CalendarOptions setWeekDayTextColor(int weekDayTextColor) {
        mWeekDayTextColor = weekDayTextColor;
        return this;
    }

    public int getSelectTextColor() {
        return mSelectTextColor;
    }

    public CalendarOptions setSelectTextColor(int selectTextColor) {
        mSelectTextColor = selectTextColor;
        return this;
    }

    public int getDateTextSize() {
        return mDateTextSize;
    }

    public CalendarOptions setDateTextSize(int dateTextSize) {
        mDateTextSize = dateTextSize;
        return this;
    }

    public int getLunarTextSize() {
        return mLunarTextSize;
    }

    public CalendarOptions setLunarTextSize(int lunarTextSize) {
        mLunarTextSize = lunarTextSize;
        return this;
    }

    public int getHolidayTextSize() {
        return mHolidayTextSize;
    }

    public CalendarOptions setHolidayTextSize(int holidayTextSize) {
        mHolidayTextSize = holidayTextSize;
        return this;
    }

    public int getPrimaryColor() {
        return mPrimaryColor;
    }

    public CalendarOptions setPrimaryColor(int primaryColor) {
        mPrimaryColor = primaryColor;
        return this;
    }

    public int getAccentColor() {
        return mAccentColor;
    }

    public CalendarOptions setAccentColor(int accentColor) {
        mAccentColor = accentColor;
        return this;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public CalendarOptions setTextColor(int textColor) {
        mTextColor = textColor;
        return this;
    }

    public int getChildPaddingLeft() {
        return mChildPaddingLeft;
    }

    public CalendarOptions setChildPaddingLeft(int childPaddingLeft) {
        mChildPaddingLeft = childPaddingLeft;
        return this;
    }

    public int getChildPaddingRight() {
        return mChildPaddingRight;
    }

    public CalendarOptions setChildPaddingRight(int childPaddingRight) {
        mChildPaddingRight = childPaddingRight;
        return this;
    }

    public int getChildPaddingTop() {
        return mChildPaddingTop;
    }

    public CalendarOptions setChildPaddingTop(int childPaddingTop) {
        mChildPaddingTop = childPaddingTop;
        return this;
    }

    public int getChildPaddingBottom() {
        return mChildPaddingBottom;
    }

    public CalendarOptions setChildPaddingBottom(int childPaddingBottom) {
        mChildPaddingBottom = childPaddingBottom;
        return this;
    }
}
