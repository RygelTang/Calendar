package cn.rygel.gd.utils.provider.impl;

import cn.rygel.gd.db.model.EventModel;
import cn.rygel.gd.utils.provider.EventInfoProvider;
import rygel.cn.calendar.bean.Solar;

public class DefaultEventInfoProvider implements EventInfoProvider {

    @Override
    public boolean hasEvent(Solar solar) {
        return EventModel.getInstance().hasEventIn(solar);
    }

}
