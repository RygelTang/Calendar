package cn.rygel.gd.ui.index.fragment.calendar.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.rygel.gd.bean.TimeLineItem;
import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.db.model.EventModel;
import cn.rygel.gd.ui.index.fragment.calendar.ICalendarView;
import cn.rygel.gd.utils.calendar.CalendarUtils;
import cn.rygel.gd.utils.observer.AsyncTransformer;
import cn.rygel.gd.utils.observer.BaseObserver;
import cn.rygel.gd.utils.calendar.LunarUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import rygel.cn.uilibrary.mvp.BasePresenter;

public class CalendarPresenter extends BasePresenter<ICalendarView> {

    private static final String TAG = "CalendarPresenter";

    private EventModel mEventModel = EventModel.getInstance();

    private Set<String> mSubscribeTags = new HashSet<>();

    public void loadEventItemsInRange(LunarUtils.Solar start, int interval, final boolean isStart) {
        final String method = "loadEventItemsInRange";
        final String subscribeTag = TAG + "#" + method;
        mSubscribeTags.add(subscribeTag);
        BaseObserver.cancel(subscribeTag);
        final LunarUtils.Solar solarStart = new LunarUtils.Solar(start.solarYear,start.solarMonth,start.solarDay);
        final LunarUtils.Solar solarEnd = CalendarUtils.getDayByInterval(start,interval);
        Observable.create(new ObservableOnSubscribe<List<BaseEvent>>() {

            @Override
            public void subscribe(ObservableEmitter<List<BaseEvent>> emitter) throws Exception {
                try {
                    emitter.onNext(mEventModel.queryInRange(solarStart, solarEnd));
                } catch (Exception e){
                    emitter.onError(e);
                }
                emitter.onComplete();
            }
        }).compose(getView().getLifecycleProvider().bindToLifecycle())
                .compose(new AsyncTransformer<List<BaseEvent>>())
                .map(new Function<List<BaseEvent>,List<TimeLineItem>>() {
                    @Override
                    public List<TimeLineItem> apply(List<BaseEvent> events) throws Exception {
                        return formatEvents(solarStart, solarEnd, events);
                    }
                })
                .subscribe(new BaseObserver<List<TimeLineItem>>(){
                    @Override
                    public Object getTag() {
                        return subscribeTag;
                    }

                    @Override
                    public void onSuccess(List<TimeLineItem> events) {
                        getView().showEvents(events,isStart);
                    }

                });
    }

    private static List<TimeLineItem> formatEvents(LunarUtils.Solar start, LunarUtils.Solar end, List<BaseEvent> events){
        final List<TimeLineItem> items = new ArrayList<>();
        final int interval = CalendarUtils.getIntervalDays(start, end);
        int index = 0;
        while(index <= interval){
            List<BaseEvent> eventList = new ArrayList<>();
            for(BaseEvent event : events){
                if(needShow(start,event)){
                    eventList.add(event);
                }
            }
            TimeLineItem item = new TimeLineItem();
            item.setDate(new LunarUtils.Solar(start.solarYear,start.solarMonth,start.solarDay));
            item.setEvents(eventList);
            items.add(item);
            start = CalendarUtils.tomorrow(start);
            index++;
        }
        return items;
    }

    private static boolean needShow(LunarUtils.Solar date,BaseEvent event){
        switch (event.getEventType().mRepeatType) {
            case EVERY_DAY:
                return true;
            case EVERY_YEAR:
                if(event.isLunarEvent()){
                    final LunarUtils.Lunar lunar = LunarUtils.solarToLunar(date);
                    final LunarUtils.Lunar eventLeapLunar = new LunarUtils.Lunar(true,event.getEventLunarDate().lunarYear,event.getEventLunarDate().lunarMonth,event.getEventLunarDate().lunarDay);
                    final LunarUtils.Lunar eventCommonLunar = new LunarUtils.Lunar(false,event.getEventLunarDate().lunarYear,event.getEventLunarDate().lunarMonth,event.getEventLunarDate().lunarDay);
                    return eventLeapLunar.equals(lunar) || eventCommonLunar.equals(lunar);
                } else {
                    final LunarUtils.Solar solar = event.getEventSolarDate();
                    return date.equals(solar);
                }
            case EVERY_WEEK:
                return CalendarUtils.getIntervalDays(date,event.getEventSolarDate()) % 7 == 0;
            case NO_REPEAT:
                return date.equals(event.getEventSolarDate());
            case EVERY_MONTH:
                return date.solarDay == event.getEventSolarDate().solarDay;
            default:
                return false;
        }
    }

    @Override
    public void dropView() {
        super.dropView();
        for(String tag : mSubscribeTags){
            BaseObserver.cancel(tag);
        }
    }
}
