package cn.rygel.gd.ui.index.fragment.events.impl;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.rygel.gd.db.model.EventModel;
import cn.rygel.gd.ui.index.fragment.events.IEventsView;
import cn.rygel.gd.utils.observer.AsyncTransformer;
import cn.rygel.gd.utils.observer.BaseObserver;
import cn.rygel.gd.widget.adapter.EventListAdapter;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.SolarUtils;
import rygel.cn.uilibrary.mvp.BasePresenter;

public class EventsPresenter extends BasePresenter<IEventsView> {

    private static final String TAG = "EventsPresenter";

    private EventModel mEventModel = EventModel.getInstance();
    private Set<String> mSubscribeTags = new HashSet<>();

    public void generateEventListAdapter(String userName) {
        final String method = "generateEventListAdapter";
        final String subscribeTag = TAG + "#" + method;
        mSubscribeTags.add(subscribeTag);
        Observable.just(generateAdapter(userName))
                .compose(new AsyncTransformer())
                .compose(getView().getLifecycleProvider().bindToLifecycle())
                .subscribe(new BaseObserver<List<BaseQuickAdapter>>() {
                    @Override
                    public Object getTag() {
                        return subscribeTag;
                    }

                    @Override
                    public void onSuccess(List<BaseQuickAdapter> adapters) {
                        Logger.i("adapter generate success!");
                        getView().onAdapterGenerated(adapters);
                    }

                    @Override
                    public void onFail(Throwable t) {
                        super.onFail(t);
                        Logger.e("adapter generate fail, cause by : " + t.getMessage());
                    }
                });
    }

    private List<EventListAdapter> generateAdapter(String userName) {
        Logger.i("adapter generating!");
        Solar today = SolarUtils.today();
        mEventModel.init(userName);
        List<EventListAdapter> adapters = new ArrayList<>();
        Solar weekEnd = SolarUtils.getDayByInterval(today, 7);
        Solar monthEnd = SolarUtils.getDayByInterval(today, SolarUtils.getMonthDay(today.solarYear,today.solarMonth) - today.solarDay);
        adapters.add(new EventListAdapter(mEventModel.queryInRange(today, weekEnd), today, weekEnd));
        adapters.add(new EventListAdapter(mEventModel.queryInRange(today, monthEnd), today, monthEnd));
        adapters.add(new EventListAdapter(mEventModel.query(), today, null));
        Logger.i("adapter generated!");
        return adapters;
    }

    @Override
    public void dropView() {
        super.dropView();
        for(String tag : mSubscribeTags){
            BaseObserver.cancel(tag);
        }
    }

}
