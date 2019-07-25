package cn.rygel.gd.widget.adapter;

import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.rygel.gd.R;
import cn.rygel.gd.bean.OnDateEventDeleteAllEvent;
import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.bean.event.base.DefaultEvent;
import cn.rygel.gd.bean.event.base.LocationEvent;
import cn.rygel.gd.bean.event.constants.EventType;
import cn.rygel.gd.db.model.EventModel;
import cn.rygel.gd.ui.edit.impl.EditEventActivity;

public class EventAdapter extends BaseMultiItemQuickAdapter<BaseEvent,BaseViewHolder> {

    public EventAdapter(List<BaseEvent> events)  {
        super(events);
        addItemType(EventType.TYPE_DEFAULT,R.layout.item_event_type_default);
        addItemType(EventType.TYPE_BIRTHDAY,R.layout.item_event_type_default);
        addItemType(EventType.TYPE_APPOINTMENT,R.layout.item_event_with_location);
        addItemType(EventType.TYPE_MEETING,R.layout.item_event_with_location);
        addItemType(EventType.TYPE_MEMORIAL,R.layout.item_event_type_default);
    }

    @Override
    protected void convert(BaseViewHolder helper, final BaseEvent item) {
        helper.getView(R.id.layout_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu_click_event,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_edit:
                                onEditItem(item);
                                break;
                            case R.id.action_delete:
                                onDeleteItem(item);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
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

    private void onEditItem(BaseEvent event) {
        EditEventActivity.start(mContext,event);
    }

    private void onDeleteItem(BaseEvent event) {
        EventModel.getInstance().delete(event);
        if(mData.contains(event)) {
            int index = mData.indexOf(event);
            mData.remove(event);
            notifyItemRemoved(index);
        }
        if(mData.size() == 0) {
            EventBus.getDefault().post(new OnDateEventDeleteAllEvent());
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
