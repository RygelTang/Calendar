package cn.rygel.gd.ui.index.fragment.calendar.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.db.model.EventModel;
import cn.rygel.gd.ui.index.fragment.calendar.ICalendarView;
import cn.rygel.gd.utils.observer.AsyncTransformer;
import cn.rygel.gd.utils.observer.BaseObserver;
import cn.rygel.gd.utils.calendar.LunarUtils;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import rygel.cn.uilibrary.mvp.BasePresenter;

public class CalendarPresenter extends BasePresenter<ICalendarView> {

    public void loadEventItemsInRange(LunarUtils.Solar start, LunarUtils.Solar end){
        
    }

}
