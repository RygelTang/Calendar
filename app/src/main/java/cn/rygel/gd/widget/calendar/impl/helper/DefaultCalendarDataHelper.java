package cn.rygel.gd.widget.calendar.impl.helper;

import android.text.TextUtils;

import cn.rygel.gd.utils.calendar.CalendarUtils;
import cn.rygel.gd.utils.calendar.LunarUtils;
import cn.rygel.gd.widget.calendar.bean.CalendarData;
import cn.rygel.gd.widget.calendar.helper.CalendarDataHelper;

// TODO: 2018/11/25 实现下标数据获取
public class DefaultCalendarDataHelper implements CalendarDataHelper {

    private static final String[] WEEKDAYS = {
            "日", "一", "二", "三", "四", "五", "六"
    };

    private int mStartOffset = 0;

    public DefaultCalendarDataHelper() {

    }

    @Override
    public void setDateOffset(int offset) {
        mStartOffset = offset;
    }

    @Override
    public int getDaysOfMonth(int year, int month) {
        return CalendarUtils.getMonthDay(year, month);
    }

    @Override
    public int getStartIndex(int year, int month) {
        int startIndex = CalendarUtils.getWeekDay(new LunarUtils.Solar(year,month,1))
                - mStartOffset;
        return startIndex < 0 ? startIndex + 7 : startIndex;
    }

    @Override
    public String[] getWeekDayInfo() {
        String[] result = new String[7];
        for(int i = 0;i < 7;i++){
            result[i] = WEEKDAYS[(i + mStartOffset) % 7];
        }
        return result;
    }

    @Override
    public String[] getDaysInfo(int year, int month) {
        String[] result = new String[getDaysOfMonth(year, month)];
        for(int i = 1;i < result.length + 1;i++){
            result[i - 1] = String.valueOf(i);
        }
        return result;
    }

    @Override
    public String[] getLunarsInfo(int year, int month) {
        String[] result = new String[getDaysOfMonth(year, month)];
        for(int i = 1;i < result.length + 1;i++){
            LunarUtils.Lunar lunar = LunarUtils.solarToLunar(new LunarUtils.Solar(year,month,i));
            result[i - 1] = LunarUtils.getLunarDayString(lunar.lunarMonth,lunar.lunarDay,lunar.isLeap);
        }
        return result;
    }

    @Override
    public String[] getHolidaysInfo(int year, int month) {
        String[] result = new String[getDaysOfMonth(year, month)];
        for(int i = 1;i < result.length + 1;i++){
            result[i - 1] = getHolidayString(new LunarUtils.Solar(year,month,i));
        }
        return result;
    }

    @Override
    public int getIndicatorsInfo(int year, int month) {
        return 0;
    }

    @Override
    public int[] getIndicatorColors(int year, int month) {
        return new int[0];
    }

    @Override
    public int getTodayIndex(int year, int month) {
        LunarUtils.Solar today = CalendarUtils.today();
        return (today.solarYear == year && today.solarMonth == month) ? today.solarDay - 1 : -1;
    }

    public String getHolidayString(LunarUtils.Solar solar){
        LunarUtils.Lunar lunar = LunarUtils.solarToLunar(solar);
        String lunarHoliday = LunarUtils.getLunarHoliday(lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay);
        if (!TextUtils.isEmpty(lunarHoliday)) {
            return lunarHoliday;
        }

        String solarHoliday = LunarUtils.getHolidayFromSolar(solar.solarYear, solar.solarMonth - 1, solar.solarDay);
        if (!TextUtils.isEmpty(solarHoliday)) {
            return solarHoliday;
        }

        return null;
    }

    @Override
    public CalendarData getCalendarData(int year, int month){
        CalendarData data = new CalendarData();
        data.setYear(year);
        data.setMonth(month);
        data.setDayInfos(getDaysInfo(year, month));
        data.setDays(getDaysOfMonth(year, month));
        data.setHolidays(getHolidaysInfo(year, month));
        data.setIndicatorColors(getIndicatorColors(year, month));
        data.setIndicators(getIndicatorsInfo(year, month));
        data.setLunars(getLunarsInfo(year, month));
        data.setStartIndex(getStartIndex(year, month));
        data.setWeekDayInfo(getWeekDayInfo());
        data.setTodayIndex(getTodayIndex(year, month));
        return data;
    }
}
