package cn.rygel.gd.utils.provider;

import java.util.ArrayList;
import java.util.List;

import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendarview.provider.impl.DefaultHolidayInfoProvider;

public class HolidayInfoProvider extends DefaultHolidayInfoProvider {

    private final static List<Solar> STATUTORY_HOLIDAY = new ArrayList<>();
    private final static List<Solar> MAKE_UP_DAY = new ArrayList<>();

    static {
        initStatutoryHoliday();
        initMakeUpDay();
    }

    /**
     * 初始化法定节假日信息
     */
    private static void initStatutoryHoliday() {
        STATUTORY_HOLIDAY.add(new Solar(2020,1,1));

        STATUTORY_HOLIDAY.add(new Solar(2020,1,24));
        STATUTORY_HOLIDAY.add(new Solar(2020,1,25));
        STATUTORY_HOLIDAY.add(new Solar(2020,1,26));
        STATUTORY_HOLIDAY.add(new Solar(2020,1,27));
        STATUTORY_HOLIDAY.add(new Solar(2020,1,28));
        STATUTORY_HOLIDAY.add(new Solar(2020,1,29));
        STATUTORY_HOLIDAY.add(new Solar(2020,1,30));

        STATUTORY_HOLIDAY.add(new Solar(2020,4,4));
        STATUTORY_HOLIDAY.add(new Solar(2020,4,5));
        STATUTORY_HOLIDAY.add(new Solar(2020,4,6));

        STATUTORY_HOLIDAY.add(new Solar(2020,5,1));
        STATUTORY_HOLIDAY.add(new Solar(2020,5,2));
        STATUTORY_HOLIDAY.add(new Solar(2020,5,3));
        STATUTORY_HOLIDAY.add(new Solar(2020,5,4));
        STATUTORY_HOLIDAY.add(new Solar(2020,5,5));

        STATUTORY_HOLIDAY.add(new Solar(2020,6,25));
        STATUTORY_HOLIDAY.add(new Solar(2020,6,26));
        STATUTORY_HOLIDAY.add(new Solar(2020,6,27));

        STATUTORY_HOLIDAY.add(new Solar(2020,10,1));
        STATUTORY_HOLIDAY.add(new Solar(2020,10,2));
        STATUTORY_HOLIDAY.add(new Solar(2020,10,3));
        STATUTORY_HOLIDAY.add(new Solar(2020,10,4));
        STATUTORY_HOLIDAY.add(new Solar(2020,10,5));
        STATUTORY_HOLIDAY.add(new Solar(2020,10,6));
        STATUTORY_HOLIDAY.add(new Solar(2020,10,7));
        STATUTORY_HOLIDAY.add(new Solar(2020,10,8));
    }

    /**
     * 初始化法定节假日补班信息
     */
    private static void initMakeUpDay() {
        MAKE_UP_DAY.add(new Solar(2020,1,19));
        MAKE_UP_DAY.add(new Solar(2020,2,1));
        MAKE_UP_DAY.add(new Solar(2020,4,26));
        MAKE_UP_DAY.add(new Solar(2020,5,9));
        MAKE_UP_DAY.add(new Solar(2020,6,28));
        MAKE_UP_DAY.add(new Solar(2020,9,27));
        MAKE_UP_DAY.add(new Solar(2020,10,10));
    }

    @Override
    public boolean isStatutoryHoliday(Solar solar) {
        for(Solar s : STATUTORY_HOLIDAY) {
            if(s.equals(solar)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMakeUpDay(Solar solar) {
        for(Solar s : MAKE_UP_DAY) {
            if(s.equals(solar)) {
                return true;
            }
        }
        return false;
    }

}
