package cn.rygel.gd.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.rygel.gd.R;
import cn.rygel.gd.bean.TimeLineItem;
import rygel.cn.uilibrary.utils.UIUtils;

public class TimeLineAdapter extends BaseQuickAdapter<TimeLineItem,BaseViewHolder> {

    private TimeLineAdapter(@Nullable List<TimeLineItem> data) {
        super(R.layout.item_time_line,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TimeLineItem events) {
        helper.setText(R.id.tv_date,
                UIUtils.getString(helper.itemView.getContext(),
                        R.string.dd,
                        events.getDate().solarDay
                )
        );
    }
}
