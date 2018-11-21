package cn.rygel.gd.widget.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import cn.rygel.gd.utils.LunarUtils;

// TODO: 2018/11/4 对外暴露的属性设计
public class CalendarView extends ViewPager implements ICalendar {

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
    }


    /******************************************* 对外暴露方法 *********************************************/
    // TODO: 2018/11/21 对外暴露方法的实现
    @Override
    public void setSelect(LunarUtils.Solar solar) {

    }

    @Override
    public void setCalendarDataHelper(CalendarDataHelper calendarDataHelper) {

    }

    @Override
    public void setOnDateSelectListener(OnDateSelectedListener onDateSelectedListener) {

    }

    @Override
    public void setOnMonthChangedListener(OnMonthChangedListener onMonthChangedListener) {

    }

    @Override
    public LunarUtils.Solar getSelectDate() {
        return null;
    }
}
