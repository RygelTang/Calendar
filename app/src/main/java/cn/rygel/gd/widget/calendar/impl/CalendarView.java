package cn.rygel.gd.widget.calendar.impl;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import cn.rygel.gd.R;
import cn.rygel.gd.utils.calendar.LunarUtils;
import cn.rygel.gd.widget.calendar.ICalendar;
import cn.rygel.gd.widget.calendar.bean.ParamBean;
import cn.rygel.gd.widget.calendar.helper.CalendarDataHelper;
import cn.rygel.gd.widget.calendar.listener.OnDateSelectedListener;
import cn.rygel.gd.widget.calendar.listener.OnMonthChangedListener;

// TODO: 2018/11/4 对外暴露的属性设计
public class CalendarView extends ViewPager implements ICalendar {

    private CalendarPageAdapter mCalendarPageAdapter = new CalendarPageAdapter(this);

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
        ParamBean param = new ParamBean();
        param.setDateOffset(ta.getInteger(R.styleable.CalendarView_date_offset,param.getDateOffset()));
        param.setAccentColor(ta.getColor(R.styleable.CalendarView_accent_color,param.getAccentColor()));
        param.setPrimaryColor(ta.getColor(R.styleable.CalendarView_primary_color,param.getPrimaryColor()));
        param.setTextColor(ta.getColor(R.styleable.CalendarView_text_color,param.getTextColor()));
        param.setSelectTextColor(ta.getColor(R.styleable.CalendarView_select_text_color,param.getSelectTextColor()));
        param.setWeekDayTextColor(ta.getColor(R.styleable.CalendarView_week_day_text_color,param.getWeekDayTextColor()));
        param.setWeekBarHeight(ta.getDimensionPixelSize(R.styleable.CalendarView_week_bar_height,param.getWeekBarHeight()));
        param.setWeekDayTextSize(ta.getDimensionPixelSize(R.styleable.CalendarView_week_day_text_size,param.getWeekDayTextSize()));
        param.setDateTextSize(ta.getDimensionPixelSize(R.styleable.CalendarView_date_text_size,param.getDateTextSize()));
        param.setLunarTextSize(ta.getDimensionPixelSize(R.styleable.CalendarView_lunar_text_size,param.getLunarTextSize()));
        param.setHolidayTextSize(ta.getDimensionPixelSize(R.styleable.CalendarView_holiday_text_size,param.getHolidayTextSize()));
        param.setChildPaddingLeft(ta.getDimensionPixelSize(R.styleable.CalendarView_child_padding_left,param.getChildPaddingLeft()));
        param.setChildPaddingRight(ta.getDimensionPixelSize(R.styleable.CalendarView_child_padding_right,param.getChildPaddingRight()));
        param.setChildPaddingTop(ta.getDimensionPixelSize(R.styleable.CalendarView_child_padding_top,param.getChildPaddingTop()));
        param.setChildPaddingBottom(ta.getDimensionPixelSize(R.styleable.CalendarView_child_padding_bottom,param.getChildPaddingBottom()));
        ta.recycle();
        mCalendarPageAdapter.setParam(param);
    }

    /******************************************* 对外暴露方法 *********************************************/
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
