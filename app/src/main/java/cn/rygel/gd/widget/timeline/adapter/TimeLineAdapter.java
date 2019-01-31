package cn.rygel.gd.widget.timeline.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.rygel.gd.R;
import cn.rygel.gd.ui.event.impl.AddEventActivity;
import cn.rygel.gd.widget.timeline.bean.TimeLineItem;
import cn.rygel.gd.utils.calendar.LunarUtils;
import rygel.cn.uilibrary.utils.UIUtils;

public class TimeLineAdapter extends BaseQuickAdapter<TimeLineItem,BaseViewHolder> {

    public TimeLineAdapter(@Nullable List<TimeLineItem> data) {
        super(R.layout.item_time_line,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final TimeLineItem events) {
        RecyclerView rvEvents = helper.getView(R.id.rv_date_detail);
        // 减少滑动卡顿
        rvEvents.setNestedScrollingEnabled(false);
        Button btnEmpty = helper.getView(R.id.btn_event_empty);
        btnEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewEventIn(events.getDate());
            }
        });
        if(events.getEvents().size() > 0){
            EventAdapter adapter = new EventAdapter(events);
            btnEmpty.setVisibility(View.GONE);
            rvEvents.setVisibility(View.VISIBLE);
            rvEvents.setAdapter(adapter);
        } else {
            btnEmpty.setVisibility(View.VISIBLE);
            rvEvents.setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_date,
                UIUtils.getString(helper.itemView.getContext(),
                        R.string.dd,
                        events.getDate().solarDay
                )
        );
    }

    private void createNewEventIn(LunarUtils.Solar solar) {
        AddEventActivity.start(mContext, solar, null, null);
    }
}
