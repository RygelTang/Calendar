package cn.rygel.gd.db.filter;

import cn.rygel.gd.db.entity.Time;
import cn.rygel.gd.utils.LunarComparator;
import cn.rygel.gd.utils.SolarComparator;
import io.objectbox.query.QueryFilter;
import rygel.cn.calendar.bean.Lunar;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.LunarUtils;
import rygel.cn.calendar.utils.SolarUtils;

public class RangeTimeFilter implements QueryFilter<Time> {

    private SolarComparator mComparator = new SolarComparator();
    private LunarComparator mLunarComparator = new LunarComparator();
    private Solar mStart;
    private Solar mEnd;

    public RangeTimeFilter(Solar start, Solar end) {
        if(mComparator.compare(start, end) < 0) {
            mStart = start;
            mEnd = end;
        } else {
            mStart = end;
            mEnd = end;
        }
    }

    @Override
    public boolean keep(Time time) {
        boolean flag = false;
        final int interval = SolarUtils.getIntervalDays(mStart,mEnd);
        final Solar solar = time.getSolar();
        switch (time.getRepeatType()){
            case EVERY_DAY:
                flag = true;
                break;
            case NO_REPEAT:
                flag = mComparator.compare(mStart,solar) < 0 && mComparator.compare(solar,mEnd) < 0;
                break;
            case EVERY_WEEK:
                if(interval >= 7) {
                    flag = true;
                    break;
                }
                int weekday = SolarUtils.getWeekDay(solar);
                int start = SolarUtils.getWeekDay(mStart);
                int end = SolarUtils.getWeekDay(mEnd);
                if(weekday < start) {
                    weekday += 7;
                }
                if(start > end) {
                    end += 7;
                }
                flag = start <= weekday && end >= weekday;
                break;
            case EVERY_MONTH:
                final int daysOfStart = SolarUtils.getMonthDay(solar.solarYear,solar.solarMonth);
                final int startDay = mStart.solarDay;
                final int endDay = mEnd.solarDay;
                final int eventDay = solar.solarDay;
                final int months = (mEnd.solarYear - mStart.solarYear) * 12 + mEnd.solarMonth - mStart.solarMonth;
                if(months > 3) {
                    flag = true;
                } else if(months == 0) {
                    flag = eventDay >= startDay && eventDay <= endDay;
                } else {
                    flag = eventDay <= endDay || (eventDay >= startDay && eventDay <= daysOfStart);
                    if(!flag){
                        for(int i = 0;i < months;i++){
                            final boolean nextYear = mStart.solarMonth + i > 12;
                            final int curMonth = nextYear ? mStart.solarMonth + i - 12 : mStart.solarMonth + i;
                            final int curYear = nextYear ? mStart.solarYear + 1 : mStart.solarYear;
                            final int daysInMonth = SolarUtils.getMonthDay(curYear,curMonth);
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
                    final Lunar lunar = solar.toLunar();
                    final Lunar mStartLunar = mStart.toLunar();
                    final Lunar mEndLunar = mEnd.toLunar();
                    final int startYear = mStartLunar.lunarYear;
                    final int endYear = mEndLunar.lunarYear;
                    final int years = endYear - startYear;
                    for(int i = 0;i <= years;i++) {
                        final int curYear = startYear + i;
                        final Lunar eventDate = new Lunar(false,curYear,lunar.lunarMonth,lunar.lunarDay);
                        flag = mLunarComparator.compare(eventDate,mStartLunar) < 0 &&
                                mLunarComparator.compare(mEndLunar,eventDate) < 0 &&
                                checkLunar(eventDate);
                    }
                } else {
                    final int startYear = mStart.solarYear;
                    final int endYear = mEnd.solarYear;
                    final int years = endYear - startYear;
                    if (years > 4){
                        flag = true;
                    } else {
                        final Solar eventDate = new Solar(startYear,mStart.solarMonth,mStart.solarDay);
                        final boolean needCheck = eventDate.solarMonth == 2 && eventDate.solarDay == 29;
                        flag = mComparator.compare(eventDate,mStart) < 0 && mComparator.compare(mEnd,eventDate) < 0;
                        if(flag && needCheck){
                            for(int i = 0;i <= years;i++){
                                Solar feb = new Solar(startYear + i,3,1).last();
                                flag = SolarUtils.isLeapYear(startYear + i) &&
                                        mComparator.compare(feb,mStart) < 0 &&
                                        mComparator.compare(mEnd,feb) < 0;
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
        return flag;
    }

    private boolean checkLunar(Lunar lunar){
        return LunarUtils.daysInMonth(lunar.lunarYear,
                lunar.lunarMonth,
                LunarUtils.getLeapMonth(lunar.lunarYear) == lunar.lunarMonth) >= lunar.lunarDay ||
                LunarUtils.daysInMonth(lunar.lunarYear,lunar.lunarMonth,false) >= lunar.lunarDay;
    }

}
