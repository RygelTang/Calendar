package cn.rygel.gd.widget.calendar.helper;

import cn.rygel.gd.widget.calendar.bean.CalendarData;

public interface CalendarDataHelper {

    void setDateOffset(int offset);

    /**
     * 根据年月返回该月的有多少天
     * @param year
     * @param month
     * @return
     */
    int getDaysOfMonth(int year, int month);

    /**
     * 根据年月返回当月的第一天的起始位置
     * 无需根据星期的信息
     * 返回值需要在0-6之间
     * @param year
     * @param month
     * @return
     */
     int getStartIndex(int year, int month);

    /**
     * 返回星期的信息，用于显示在顶部
     * 需要自行跟{@link CalendarDataHelper#getStartIndex}返回的index进行对应
     * @return
     */
    String[] getWeekDayInfo();

    /**
     * 根据年月，返回当月的日期的信息
     * 一般返回1到当月的天数即可
     * @param year
     * @param month
     * @return
     */
    String[] getDaysInfo(int year, int month);

    /**
     * 根据年月，返回当月的农历信息
     * 需要跟{@link CalendarDataHelper#getDaysInfo}的日期进行对应
     * @param year
     * @param month
     * @return
     */
    String[] getLunarsInfo(int year, int month);

    /**
     * 根据年月，返回当月的节假日信息
     * 需要跟{@link CalendarDataHelper#getDaysInfo}的日期进行对应
     * 若没有节假日，需要用null占位
     * @param year
     * @param month
     * @return
     */
    String[] getHolidaysInfo(int year, int month);

    /**
     * 根据年月返回当月的需要有下标的日期
     * 由于一个月最多只有31天，所以采用int值来储存下标的情况
     * 例如13号需要下标提醒，则将1左移13位，如果还有需要下标提醒的日期
     * 则将1左移相应位之后的结果和前面计算的结果进行或运算
     * 重复这个过程知道不再有需要有下标的日期
     * @param year
     * @param month
     * @return
     */
    int getIndicatorsInfo(int year, int month);

    /**
     * 返回补班信息，格式和下标指示一致
     * {@link CalendarDataHelper#getIndicatorsInfo(int, int)}
     * @param year
     * @param month
     * @return
     */
    int getLegalBreakInfo(int year, int month);

    /**
     * 返回法定节假日信息，格式和下标指示一致
     * {@link CalendarDataHelper#getIndicatorsInfo(int, int)}
     * @param year
     * @param month
     * @return
     */
    int getLegalHolidayInfo(int year, int month);

    /**
     * 返回相应的下标的颜色
     * @param year
     * @param month
     * @return
     */
    int[] getIndicatorColors(int year, int month);

    /**
     * 获取当天的index，如果当天不是这个月，则返回-1
     * 从0开始计数
     * @return
     */
    int getTodayIndex(int year, int month);

    /**
     * 获取封装好的CalendarData数据
     * @param year
     * @param month
     * @return
     */
    CalendarData getCalendarData(int year, int month);

}
