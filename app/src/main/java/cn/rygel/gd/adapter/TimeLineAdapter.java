package cn.rygel.gd.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.rygel.gd.R;
import cn.rygel.gd.bean.event.base.BaseEvent;

public class TimeLineAdapter extends BaseQuickAdapter<List<BaseEvent>,BaseViewHolder> {

    public TimeLineAdapter(@Nullable List<List<BaseEvent>> data) {
        super(R.layout.item_time_line,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, List<BaseEvent> item) {

    }

}
