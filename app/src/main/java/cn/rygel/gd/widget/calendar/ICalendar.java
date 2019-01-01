package cn.rygel.gd.widget.calendar;

import cn.rygel.gd.utils.calendar.LunarUtils;
import cn.rygel.gd.widget.calendar.helper.CalendarDataHelper;
import cn.rygel.gd.widget.calendar.listener.OnDateSelectedListener;
import cn.rygel.gd.widget.calendar.listener.OnMonthChangedListener;

public interface ICalendar {

    /**
     * 这里设置了选中日期以后会导致{@link OnDateSelectedListener#onDateSelect(LunarUtils.Solar)}和
     * {@link OnMonthChangedListener#onMonthChanged(int, int)}被调用
     * @param solar
     */
    void setSelect(LunarUtils.Solar solar);

    /**
     * 设置日历计算工具
     * @param calendarDataHelper
     */
    void setCalendarDataHelper(CalendarDataHelper calendarDataHelper);

    /**
     * 设置日期选择监听
     * @param onDateSelectedListener
     */
    void setOnDateSelectListener(OnDateSelectedListener onDateSelectedListener);

    /**
     * 设置月份改变监听
     * @param onMonthChangedListener
     */
    void setOnMonthChangedListener(OnMonthChangedListener onMonthChangedListener);

    /**
     * 获取当前选中的日期，若没有选中，返回值为null
     * @return
     */
    LunarUtils.Solar getSelectDate();

}
