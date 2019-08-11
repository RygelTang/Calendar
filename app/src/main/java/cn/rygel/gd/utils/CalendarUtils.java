package cn.rygel.gd.utils;

import rygel.cn.calendar.bean.Lunar;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.LunarUtils;
import rygel.cn.calendar.utils.SolarUtils;

public class CalendarUtils {

    /**
     * 检查公历日期是否合法
     * @param solar
     * @return
     */
    public static boolean checkSolar(Solar solar) {
        if (solar.solarMonth < 1 || solar.solarMonth > 12) {
            return false;
        }
        final int daysInMonth = SolarUtils.getMonthDay(solar.solarYear, solar.solarMonth);
        if (solar.solarDay < 1 || solar.solarDay > daysInMonth) {
            return false;
        }
        return true;
    }

    /**
     * 检查农历日期是否合法
     * @param lunar
     * @return
     */
    public static boolean checkLunar(Lunar lunar) {
        if (lunar.lunarMonth < 1 || lunar.lunarMonth > 12) {
            return false;
        }
        if(lunar.isLeap) {
            if(LunarUtils.getLeapMonth(lunar.lunarYear) != lunar.lunarMonth) {
                return false;
            }
        }
        final int daysInMonth = LunarUtils.daysInMonth(lunar.lunarYear, lunar.lunarMonth, lunar.isLeap);
        if (lunar.lunarDay < 1 || lunar.lunarDay > daysInMonth) {
            return false;
        }
        return true;
    }

    public static String format(Solar solar) {
        return solar.solarYear + "年" + solar.solarMonth + "月" + solar.solarDay + "日";
    }

    public static String format(Lunar lunar) {
        if (lunar.lunarDay <= 0 || lunar.lunarMonth <= 0) throw new IllegalArgumentException("lunar illegal : " + lunar);
        return lunar.lunarYear + (lunar.isLeap?"闰":"") + LunarUtils.LUNAR_MONTHS[lunar.lunarMonth - 1] + LunarUtils.LUNAR_DAYS[lunar.lunarDay - 1];
    }

}
