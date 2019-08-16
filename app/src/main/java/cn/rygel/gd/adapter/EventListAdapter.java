package cn.rygel.gd.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.rygel.gd.R;
import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.utils.CalendarUtils;
import rygel.cn.calendar.bean.Lunar;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.SolarUtils;

public class EventListAdapter extends EventAdapter {

    private Solar start = null;

    public EventListAdapter(List<BaseEvent> events, Solar start) {
        super(events);
        if (start == null) {
            throw new IllegalArgumentException("start date should not be null!");
        }
        this.start = start;
        Collections.sort(events, new Comparator<BaseEvent>() {
            @Override
            public int compare(BaseEvent o1, BaseEvent o2) {
                return getEventDuration(o1, start) - getEventDuration(o2, start);
            }
        });
        openLoadAnimation();
    }

    @Override
    protected void convert(BaseViewHolder helper, final BaseEvent item) {
        super.convert(helper, item);
        int duration = getEventDuration(item, start);
        if(duration > 0) {
            helper.setText(R.id.tv_duration, duration + "天后");
        }
    }

    private static int getEventDuration(BaseEvent event, Solar start) {
        Logger.e("getEventDuration called by");
        int interval = -1;
        Solar tempSolar = null;
        Lunar tempLunar = null;
        switch (event.getRepeatType()) {
            case NO_REPEAT :
                return SolarUtils.getIntervalDays(start, event.getEventSolarDate());
            case EVERY_DAY:
                return 0;
            case EVERY_WEEK:
                interval = SolarUtils.getIntervalDays(start, event.getEventSolarDate()) % 7;
                if (interval < 0) {
                    interval += 7;
                }
                return interval;
            case EVERY_MONTH:
                tempSolar =  new Solar(start.solarYear, start.solarMonth, event.getEventSolarDate().solarDay);
                // 确保temp是合法的日期
                while (interval < 0) {
                    while (!CalendarUtils.checkSolar(tempSolar)) {
                        tempSolar.solarMonth += 1;
                        if (tempSolar.solarMonth > 12) {
                            tempSolar.solarYear += 1;
                            tempSolar.solarMonth = 1;
                        }
                    }
                    interval = SolarUtils.getIntervalDays(start, tempSolar);
                    tempSolar.solarMonth += 1;
                    if (tempSolar.solarMonth > 12) {
                        tempSolar.solarYear += 1;
                        tempSolar.solarMonth = 1;
                    }
                }
                return interval;
            case EVERY_YEAR:
                if (event.isLunarEvent()) {
                    Lunar date = event.getEventLunarDate();
                    tempLunar = new Lunar(false, start.toLunar().lunarYear, date.lunarMonth, date.lunarDay);
                    while (interval < 0) {
                        while (!CalendarUtils.checkLunar(tempLunar)) {
                            if (!tempLunar.isLeap) {
                                tempLunar.isLeap = true;
                                continue;
                            }
                            tempLunar.lunarYear += 1;
                        }
                        interval = SolarUtils.getIntervalDays(start, tempLunar.toSolar());
                        tempLunar.lunarYear += 1;
                    }
                    return interval;
                } else {
                    Solar date = event.getEventSolarDate();
                    tempSolar =  new Solar(start.solarYear, date.solarMonth, date.solarDay);
                    // 确保temp是合法的日期
                    while (interval < 0) {
                        while (!CalendarUtils.checkSolar(tempSolar)) {
                            tempSolar.solarYear += 1;
                        }
                        interval = SolarUtils.getIntervalDays(start, tempSolar);
                        tempSolar.solarYear += 1;
                    }
                    return interval;
                }
            default:
                return -1;
        }
    }

}
