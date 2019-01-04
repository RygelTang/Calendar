package cn.rygel.gd.widget.timeline.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.rygel.gd.R;
import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.bean.event.constants.EventType;

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
        switch (helper.getItemViewType()) {
            case EventType.TYPE_DEFAULT:
                break;
            case EventType.TYPE_BIRTHDAY:
                break;
            case EventType.TYPE_APPOINTMENT:
                break;
            case EventType.TYPE_MEETING:
                break;
            case EventType.TYPE_MEMORIAL:
                break;
        }
    }
}
