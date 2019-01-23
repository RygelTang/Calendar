package cn.rygel.gd.widget.calendar.impl;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import cn.rygel.gd.R;
import cn.rygel.gd.utils.calendar.LunarUtils;
import cn.rygel.gd.widget.calendar.ICalendar;
import cn.rygel.gd.widget.calendar.bean.CalendarOptions;
import cn.rygel.gd.widget.calendar.helper.CalendarDataHelper;
import cn.rygel.gd.widget.calendar.listener.OnDateSelectedListener;
import cn.rygel.gd.widget.calendar.listener.OnMonthChangedListener;

// TODO: 2018/11/4 对外暴露的属性设计
public class CalendarView extends ViewPager implements ICalendar {

    private CalendarPageAdapter mCalendarPageAdapter = new CalendarPageAdapter(this);

    private CalendarOptions mOptions = new CalendarOptions();

    /**
     * 构造方法
     * @param context
     */
    public CalendarView(Context context){
        this(context,null);
    }

    /**
     * 构造方法
     * @param context
     * @param attrs
     */
    public CalendarView(Context context, AttributeSet attrs){
        super(context,attrs);
        setAdapter(mCalendarPageAdapter);
        obtainAttrs(attrs);
        // 硬件加速
        // setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private void obtainAttrs(AttributeSet attrs){
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);
        mOptions.setDateOffset(ta.getInteger(R.styleable.CalendarView_date_offset,mOptions.getDateOffset()));
        mOptions.setAccentColor(ta.getColor(R.styleable.CalendarView_accent_color,mOptions.getAccentColor()));
        mOptions.setPrimaryColor(ta.getColor(R.styleable.CalendarView_primary_color,mOptions.getPrimaryColor()));
        mOptions.setTextColor(ta.getColor(R.styleable.CalendarView_text_color,mOptions.getTextColor()));
        mOptions.setSelectTextColor(ta.getColor(R.styleable.CalendarView_select_text_color,mOptions.getSelectTextColor()));
        mOptions.setWeekDayTextColor(ta.getColor(R.styleable.CalendarView_week_day_text_color,mOptions.getWeekDayTextColor()));
        mOptions.setWeekBarHeight(ta.getDimensionPixelSize(R.styleable.CalendarView_week_bar_height,mOptions.getWeekBarHeight()));
        mOptions.setWeekDayTextSize(ta.getDimensionPixelSize(R.styleable.CalendarView_week_day_text_size,mOptions.getWeekDayTextSize()));
        mOptions.setDateTextSize(ta.getDimensionPixelSize(R.styleable.CalendarView_date_text_size,mOptions.getDateTextSize()));
        mOptions.setLunarTextSize(ta.getDimensionPixelSize(R.styleable.CalendarView_lunar_text_size,mOptions.getLunarTextSize()));
        mOptions.setHolidayTextSize(ta.getDimensionPixelSize(R.styleable.CalendarView_holiday_text_size,mOptions.getHolidayTextSize()));
        mOptions.setChildPaddingLeft(ta.getDimensionPixelSize(R.styleable.CalendarView_child_padding_left,mOptions.getChildPaddingLeft()));
        mOptions.setChildPaddingRight(ta.getDimensionPixelSize(R.styleable.CalendarView_child_padding_right,mOptions.getChildPaddingRight()));
        mOptions.setChildPaddingTop(ta.getDimensionPixelSize(R.styleable.CalendarView_child_padding_top,mOptions.getChildPaddingTop()));
        mOptions.setChildPaddingBottom(ta.getDimensionPixelSize(R.styleable.CalendarView_child_padding_bottom,mOptions.getChildPaddingBottom()));
        ta.recycle();
        setCalendarOptions(mOptions);
    }

    /******************************************* 对外暴露方法 *********************************************/
    public CalendarOptions getCalendarOptions(){
        return mOptions;
    }

    public void setCalendarOptions(CalendarOptions options) {
        mOptions = options;
        mCalendarPageAdapter.setCalendarOptions(options);
    }

    @Override
    public void setSelect(LunarUtils.Solar solar) {
        mCalendarPageAdapter.select(solar);
    }

    @Override
    public void setCalendarDataHelper(CalendarDataHelper calendarDataHelper) {
        mCalendarPageAdapter.setCalendarDataHelper(calendarDataHelper);
    }

    @Override
    public void setOnDateSelectListener(OnDateSelectedListener onDateSelectedListener) {
        mCalendarPageAdapter.setOnDateSelectedListener(onDateSelectedListener);
    }

    @Override
    public void setOnMonthChangedListener(OnMonthChangedListener onMonthChangedListener) {
        mCalendarPageAdapter.setOnMonthChangedListener(onMonthChangedListener);
    }

    @Override
    public LunarUtils.Solar getSelectDate() {
        return mCalendarPageAdapter.getSelectDate();
    }

}
