package cn.rygel.gd.ui.index.fragment.calendar.impl;

import com.orhanobut.logger.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.db.model.EventModel;
import cn.rygel.gd.ui.index.fragment.calendar.ICalendarView;
import cn.rygel.gd.utils.observer.AsyncTransformer;
import cn.rygel.gd.utils.observer.BaseObserver;
import io.reactivex.Observable;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.uilibrary.mvp.BasePresenter;

public class CalendarPresenter extends BasePresenter<ICalendarView> {

    private static final String TAG = "CalendarPresenter";

    private Set<String> mSubscribeTags = new HashSet<>();

    public void loadEventsOf(final Solar date) {
        final String method = "loadEventsOf";
        final String subscribeTag = TAG + "#" + method;
        mSubscribeTags.add(subscribeTag);
        BaseObserver.cancel(subscribeTag);
        Observable.just(EventModel.getInstance().queryInDay(new Solar(date.solarYear,date.solarMonth,date.solarDay)))
                .compose(new AsyncTransformer<List<BaseEvent>>())
                .compose(getView().getLifecycleProvider().bindToLifecycle())
                .subscribe(new BaseObserver<List<BaseEvent>>() {
                    @Override
                    public Object getTag() {
                        return subscribeTag;
                    }

                    @Override
                    public void onSuccess(List<BaseEvent> events) {
                        Logger.i("query success, size : " + events.size());
                        // 对数据进行排序
                        Collections.sort(events, new Comparator<BaseEvent>() {
                            @Override
                            public int compare(BaseEvent o1, BaseEvent o2) {
                                return (int) (o1.getStart() - o2.getStart());
                            }
                        });
                        getView().showEvents(events);
                    }

                    @Override
                    public void onFail(Throwable t) {
                        super.onFail(t);
                        t.printStackTrace(System.err);
                    }
                });
    }

    @Override
    public void dropView() {
        super.dropView();
        for(String tag : mSubscribeTags){
            BaseObserver.cancel(tag);
        }
    }
}
