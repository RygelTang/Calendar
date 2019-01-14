package cn.rygel.gd.widget.timeline.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.rygel.gd.R;
import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.bean.event.base.DefaultEvent;
import cn.rygel.gd.bean.event.base.LocationEvent;
import cn.rygel.gd.bean.event.constants.EventType;
import cn.rygel.gd.utils.calendar.LunarUtils;

public class EventAdapter extends BaseMultiItemQuickAdapter<BaseEvent,BaseViewHolder> {

    public EventAdapter(List<BaseEvent> data) {
        super(data);
        addItemType(EventType.TYPE_DEFAULT,R.layout.item_event_type_default);
        addItemType(EventType.TYPE_BIRTHDAY,R.layout.item_event_type_birthday);
        addItemType(EventType.TYPE_APPOINTMENT,R.layout.item_event_type_appointment);
        addItemType(EventType.TYPE_MEETING,R.layout.item_event_type_meeting);
        addItemType(EventType.TYPE_MEMORIAL,R.layout.item_event_type_memorial);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseEvent item) {
        helper.setText(R.id.tv_event_name,item.getName());
        switch (helper.getItemViewType()) {
            case EventType.TYPE_MEMORIAL:
            case EventType.TYPE_BIRTHDAY:
                helper.setText(R.id.tv_event_time,formatTime(item.getStart()));
                break;
            case EventType.TYPE_APPOINTMENT:
            case EventType.TYPE_MEETING:
                if(item instanceof LocationEvent) {
                    helper.setText(R.id.tv_event_location,((LocationEvent) item).getLocation());
                }
            case EventType.TYPE_DEFAULT:
                if(item instanceof DefaultEvent) {
                    final long duration = ((DefaultEvent) item).getDuration();
                    final long start = item.getStart();
                    helper.setText(R.id.tv_event_time,formatTimeString(start, start + duration));
                }
                break;
        }
    }

    private static String formatTimeString(long start,long end){
        return formatTime(start) + " - " + formatTime(end);
    }

    private static String formatTime(long time){
        final int hour = (int) (time / 60);
        final int minute = (int) (time % 60);
        return (hour < 10 ? "0" + hour : String.valueOf(hour)) + ":" + (minute < 10 ? "0" + minute : String.valueOf(minute));
    }

}
