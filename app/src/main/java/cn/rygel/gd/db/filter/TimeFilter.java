package cn.rygel.gd.db.filter;

import cn.rygel.gd.db.entity.Time;
import cn.rygel.gd.utils.calendar.CalendarUtils;
import cn.rygel.gd.utils.calendar.LunarUtils;
import io.objectbox.query.QueryFilter;

public class TimeFilter implements QueryFilter<Time>{

    private LunarUtils.Solar mStartSolar;
    private LunarUtils.Solar mEndSolar;

    private int mInterval = -1;

    private LunarUtils.Lunar mStartLunar;
    private LunarUtils.Lunar mEndLunar;

    public TimeFilter setStartSolar(LunarUtils.Solar startSolar) {
        mStartSolar = startSolar;
        return this;
    }

    public TimeFilter setEndSolar(LunarUtils.Solar endSolar) {
        mEndSolar = endSolar;
        return this;
    }

    public LunarUtils.Lunar getStartLunar() {
        if(mStartLunar == null){
            mStartLunar = LunarUtils.solarToLunar(mStartSolar);
        }
        return mStartLunar;
    }

    public LunarUtils.Lunar getEndLunar() {
        if (mEndLunar == null) {
            mEndLunar = LunarUtils.solarToLunar(mEndSolar);
        }
        return mEndLunar;
    }

    private int getInterval(){
        if(mInterval < 0){
            mInterval = CalendarUtils.getIntervalDays(mStartSolar,mEndSolar);
        }
        return mInterval;
    }

    private boolean checkLunar(LunarUtils.Lunar lunar){
        boolean leap = LunarUtils.leapMonth(lunar.lunarYear) == lunar.lunarMonth;
        return LunarUtils.daysInMonth(lunar.lunarYear,lunar.lunarMonth,leap) >= lunar.lunarDay ||
                LunarUtils.daysInMonth(lunar.lunarYear,lunar.lunarMonth,false) >= lunar.lunarDay;
    }

    private boolean compareLunarWithoutLeap(LunarUtils.Lunar lunar0, LunarUtils.Lunar lunar1){
        return ((lunar0.lunarYear << 9) | (lunar0.lunarMonth << 5) | lunar0.lunarDay) -
                ((lunar1.lunarYear << 9) | (lunar1.lunarMonth << 5) | lunar1.lunarDay) > 0;
    }

    @Override
    public boolean keep(Time time) {
        boolean flag = false;
        switch (time.getRepeatType()){
            case EVERY_DAY:
                flag = true;
                break;
            case NO_REPEAT:
                final LunarUtils.Solar eventSolar = time.getSolar();
                flag = CalendarUtils.compare(eventSolar,mStartSolar) && CalendarUtils.compare(mEndSolar, eventSolar);
                break;
            case EVERY_WEEK:
                flag = getInterval() > 7;
                break;
            case EVERY_MONTH:
                final int daysOfStart = CalendarUtils.getMonthDay(time.getSolar().solarYear,time.getSolar().solarMonth);
                final int startDay = mStartSolar.solarDay;
                final int endDay = mEndSolar.solarDay;
                final int eventDay = time.getSolar().solarDay;
                final int months = (mEndSolar.solarYear - mStartSolar.solarYear) * 12 + mEndSolar.solarMonth - mStartSolar.solarMonth;
                if(months > 3) {
                    flag = true;
                } else if(months == 0) {
                    flag = eventDay >= startDay && eventDay <= endDay;
                } else {
                    flag = eventDay <= endDay || (eventDay >= startDay && eventDay <= daysOfStart);
                    if(!flag){
                        for(int i = 0;i < months;i++){
                            final boolean nextYear = mStartSolar.solarMonth + i > 12;
                            final int curMonth = nextYear ? mStartSolar.solarMonth + i - 12 : mStartSolar.solarMonth + i;
                            final int curYear = nextYear ? mStartSolar.solarYear + 1 : mStartSolar.solarYear;
                            final int daysInMonth = CalendarUtils.getMonthDay(curYear,curMonth);
                            if(eventDay <= daysInMonth){
                                flag = true;
                                break;
                            }
                        }
                    }
                }
                break;
            case EVERY_YEAR:
                if(time.isLunar()){
                    final int startYear = getStartLunar().lunarYear;
                    final int endYear = getEndLunar().lunarYear;
                    final int years = endYear - startYear;
                    for(int i = 0;i <= years;i++) {
                        final int curYear = startYear + i;
                        final LunarUtils.Lunar eventDate = new LunarUtils.Lunar(false,curYear,time.getLunar().lunarMonth,time.getLunar().lunarDay);
                        flag = compareLunarWithoutLeap(eventDate,mStartLunar) && compareLunarWithoutLeap(mEndLunar,eventDate) && checkLunar(eventDate);
                        if(flag){
                            break;
                        }
                    }
                } else {
                    final int startYear = mStartSolar.solarYear;
                    final int endYear = mEndSolar.solarYear;
                    final int years = endYear - startYear;
                    if (years > 4){
                        flag = true;
                    } else {
                        final LunarUtils.Solar eventDate = new LunarUtils.Solar(startYear,mStartSolar.solarMonth,mStartSolar.solarDay);
                        final boolean needCheck = eventDate.solarMonth == 2 && eventDate.solarDay == 29;
                        flag = CalendarUtils.compare(eventDate,mStartSolar) && CalendarUtils.compare(mEndSolar,eventDate);
                        if(flag && needCheck){
                            for(int i = 0;i <= years;i++){
                                LunarUtils.Solar feb = CalendarUtils.yesterday(new LunarUtils.Solar(startYear + i,3,1));
                                flag = CalendarUtils.isLeapYear(startYear + i) &&
                                        CalendarUtils.compare(feb,mStartSolar) &&
                                        CalendarUtils.compare(mEndSolar,feb);
                            }
                        }
                    }
                }
                break;
        }
        return flag;
    }

}
