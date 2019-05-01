package cn.rygel.gd.db.filter;

import cn.rygel.gd.db.entity.Time;
import io.objectbox.query.QueryFilter;
import rygel.cn.calendarview.bean.Lunar;
import rygel.cn.calendarview.bean.Solar;
import rygel.cn.calendarview.utils.SolarUtils;

public class TimeFilter implements QueryFilter<Time>{

    private Solar mDate;

    public TimeFilter(Solar date) {
        mDate = date;
    }

    @Override
    public boolean keep(Time time) {
        boolean flag = false;
        switch (time.getRepeatType()){
            case EVERY_DAY:
                flag = true;
                break;
            case NO_REPEAT:
                flag = time.getSolar().equals(mDate);
                break;
            case EVERY_WEEK:
                flag = SolarUtils.getIntervalDays(mDate,time.getSolar()) % 7 == 0;
                break;
            case EVERY_MONTH:
                flag = time.getSolar().solarDay == mDate.solarDay;
                break;
            case EVERY_YEAR:
                if(time.isLunar()){
                    Lunar lunar = mDate.toLunar();
                    flag = time.getLunar().lunarMonth == lunar.lunarMonth && time.getLunar().lunarDay == lunar.lunarDay;
                } else {
                    flag = time.getSolar().solarMonth == mDate.solarMonth && time.getSolar().solarDay == mDate.solarDay;
                }
                break;
            default:
                break;
        }
        return flag;
    }

}
