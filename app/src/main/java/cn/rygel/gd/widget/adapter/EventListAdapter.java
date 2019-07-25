package cn.rygel.gd.widget.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.rygel.gd.R;
import cn.rygel.gd.bean.event.base.BaseEvent;
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
        int interval = 0;
        switch (event.getEventType().mRepeatType) {
            case NO_REPEAT :
                return SolarUtils.getIntervalDays(start, event.getEventSolarDate());
            case EVERY_DAY:
                return 0;
            case EVERY_WEEK:
                return SolarUtils.getIntervalDays(start, event.getEventSolarDate()) % 7;
            case EVERY_MONTH:
                interval = SolarUtils.getIntervalDays(start, new Solar(start.solarYear, start.solarMonth, event.getEventSolarDate().solarDay));
                return interval < 0 ? interval + SolarUtils.getMonthDay(start.solarYear, start.solarMonth) : interval;
            case EVERY_YEAR:
                if (event.isLunarEvent()) {
                    Lunar date = event.getEventLunarDate();
                    interval = SolarUtils.getIntervalDays(start, new Lunar(false, start.toLunar().lunarYear, date.lunarMonth, date.lunarDay).toSolar());
                    return interval < 0 ? SolarUtils.getIntervalDays(start, new Lunar(false, start.toLunar().lunarYear + 1, date.lunarMonth, date.lunarDay).toSolar()) : interval;
                } else {
                    Solar date = event.getEventSolarDate();
                    interval = SolarUtils.getIntervalDays(start, new Solar(start.solarYear, date.solarMonth, date.solarDay));
                    return interval < 0 ? SolarUtils.getIntervalDays(start, new Solar(start.solarYear + 1, date.solarMonth, date.solarDay)) : interval;
                }
            default:
                return -1;
        }
    }

}
