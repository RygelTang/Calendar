package cn.mt.gd.widget.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

// TODO: 2018/11/4 切换选中日期的动画
// TODO: 2018/11/4 各种状态下的各个日期子项的绘制
public class RealCalendarView extends View {

    private Rect mBound = new Rect();
    private Rect mChildBound = new Rect();
    private int mChildPaddingLeft = 0;
    private int mChildPaddingRight = 0;
    private int mChildPaddingTop = 0;
    private int mChildPaddingBottom = 0;

    private CalendarData mCalendarData = null;
    private Paint mTextPaint = null;

    private int mSelectItemLeftOffset = 0;
    private int mSelectItemTopOffset = 0;

    private int mSelectIndex = -1;

    /**
     * 构造函数
     * @param context
     */
    public RealCalendarView(Context context){
        this(context,null);
    }

    /**
     * 构造函数
     * @param context
     * @param attrs
     */
    public RealCalendarView(Context context, AttributeSet attrs){
        super(context, attrs);
    }


    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        mBound.left = paddingLeft;
        mBound.top = paddingTop;
        mBound.right = getMeasuredWidth() - paddingRight;
        mBound.bottom = getMeasuredHeight() - paddingBottom;
    }

    /**
     * 获取默认的宽高值
     */
    public static int getDefaultSize (int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec. getMode(measureSpec);
        int specSize = MeasureSpec. getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec. UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec. AT_MOST:
            case MeasureSpec. EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mCalendarData != null){
            int maxWeek = (mCalendarData.mDays + mCalendarData.mStartIndex) / 7;
            for(int i = 0;i < mCalendarData.mDays;i++){
                if(mSelectIndex != i){
                    drawDateItem(canvas,getChildBound(i + mCalendarData.mStartIndex,maxWeek));
                }else {
                    Rect select = getChildBound(i + mCalendarData.mStartIndex,maxWeek);
                    select.offset(-mSelectItemLeftOffset,-mSelectItemTopOffset);
                    drawSelectItem(canvas,select);
                }
            }
            if(mCalendarData.mTodayIndex > 0){
                drawTodayItem(canvas, getChildBound(mCalendarData.mTodayIndex + mCalendarData.mStartIndex,maxWeek));
            }
        }
    }

    private Rect getChildBound(int index,int maxWeek){
        final int column = index % 7;
        final int row = index / 7;
        final int itemWidth = mBound.width() / 7;
        final int itemHeight = mBound.height() / maxWeek;
        mChildBound.left = mBound.left + column * itemWidth + mChildPaddingLeft;
        mChildBound.right = mBound.left + (column + 1) * itemWidth - mChildPaddingRight;
        mChildBound.top = mBound.top + row * itemHeight + mChildPaddingTop;
        mChildBound.bottom = mBound.top + (row + 1) * itemHeight - mChildPaddingBottom;
        return mChildBound;
    }

    private void drawDateItem(Canvas canvas, Rect bound){

    }

    private void drawSelectItem(Canvas canvas, Rect bound){

    }

    private void drawTodayItem(Canvas canvas, Rect bound){

    }

    private void drawDay(Canvas canvas, Rect bound ,String day, int color){

    }

    private void drawLunar(Canvas canvas, Rect bound, String lunar, int color){

    }

    private void drawHolidayInfo(Canvas canvas, Rect bound, String info, int color){

    }

    private void drawIndicator(Canvas canvas, Rect bound, int color){

    }

    public int getChildPaddingLeft() {
        return mChildPaddingLeft;
    }

    public void setChildPaddingLeft(int childPaddingLeft) {
        mChildPaddingLeft = childPaddingLeft;
    }

    public int getChildPaddingRight() {
        return mChildPaddingRight;
    }

    public void setChildPaddingRight(int childPaddingRight) {
        mChildPaddingRight = childPaddingRight;
    }

    public int getChildPaddingTop() {
        return mChildPaddingTop;
    }

    public void setChildPaddingTop(int childPaddingTop) {
        mChildPaddingTop = childPaddingTop;
    }

    public int getChildPaddingBottom() {
        return mChildPaddingBottom;
    }

    public void setChildPaddingBottom(int childPaddingBottom) {
        mChildPaddingBottom = childPaddingBottom;
    }

    public CalendarData getCalendarData() {
        return mCalendarData;
    }

    public void setCalendarData(CalendarData calendarData) {
        mCalendarData = calendarData;
    }

    public static class CalendarData {
        public int mDays = 0;
        public int mStartIndex = 0;
        public String[] mWeekDayInfo = null;
        public String[] mDayInfos = null;
        public String[] mLunars = null;
        public String[] mHolidays = null;
        public int mIndicators = 0;
        public int[] mIndicatorColors = null;
        public int mTodayIndex = -1;

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
    }

}
