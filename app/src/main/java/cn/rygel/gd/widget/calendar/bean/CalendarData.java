package cn.rygel.gd.widget.calendar.bean;

public class CalendarData {

    public int year = 0;
    public int month = 0;
    public int mDays = 0;
    public int mStartIndex = 0;
    public String[] mWeekDayInfo = null;
    public String[] mDayInfos = null;
    public String[] mLunars = null;
    public String[] mHolidays = null;
    public int mIndicators = 0;
    public int[] mIndicatorColors = null;
    public int mTodayIndex = -1;
    public int mLegalHolidayInfo = 0;
    public int mLegalBreakInfo = 0;

    public int getDays() {
        return mDays;
    }

    public void setDays(int days) {
        mDays = days;
    }

    public int getStartIndex() {
        return mStartIndex;
    }

    public void setStartIndex(int startIndex) {
        mStartIndex = startIndex;
    }

    public String[] getDayInfos() {
        return mDayInfos;
    }

    public void setDayInfos(String[] dayInfos) {
        mDayInfos = dayInfos;
    }

    public String[] getLunars() {
        return mLunars;
    }

    public void setLunars(String[] lunars) {
        mLunars = lunars;
    }

    public String[] getHolidays() {
        return mHolidays;
    }

    public void setHolidays(String[] holidays) {
        mHolidays = holidays;
    }

    public int getIndicators() {
        return mIndicators;
    }

    public void setIndicators(int indicators) {
        mIndicators = indicators;
    }

    public int[] getIndicatorColors() {
        return mIndicatorColors;
    }

    public void setIndicatorColors(int[] indicatorColors) {
        mIndicatorColors = indicatorColors;
    }

    public String[] getWeekDayInfo() {
        return mWeekDayInfo;
    }

    public void setWeekDayInfo(String[] weekDayInfo) {
        mWeekDayInfo = weekDayInfo;
    }

    public int getTodayIndex() {
        return mTodayIndex;
    }

    public void setTodayIndex(int todayIndex) {
        mTodayIndex = todayIndex;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getLegalHolidayInfo() {
        return mLegalHolidayInfo;
    }

    public void setLegalHolidayInfo(int legalHolidayInfo) {
        mLegalHolidayInfo = legalHolidayInfo;
    }

    public int getLegalBreakInfo() {
        return mLegalBreakInfo;
    }

    public void setLegalBreakInfo(int legalBreakInfo) {
        mLegalBreakInfo = legalBreakInfo;
    }
}