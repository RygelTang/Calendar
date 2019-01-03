package cn.rygel.gd.ui.event.impl;

import java.util.HashSet;
import java.util.Set;

import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.db.model.EventModel;
import cn.rygel.gd.ui.event.IAddEventView;
import cn.rygel.gd.utils.observer.AsyncTransformer;
import cn.rygel.gd.utils.observer.BaseObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import rygel.cn.uilibrary.mvp.BasePresenter;

public class AddEventPresenter extends BasePresenter<IAddEventView> {

    private static final String TAG = "AddEventPresenter";

    private EventModel mEventModel = EventModel.getInstance();

    private Set<String> mSubscribeTags = new HashSet<>();

    public void saveEvent(final BaseEvent event){
        final String method = "loadEventItemsInRange";
        final String subscribeTag = TAG + "#" + method;
        mSubscribeTags.add(subscribeTag);
        BaseObserver.cancel(subscribeTag);
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                try {
                    emitter.onNext(mEventModel.putEvent(event));
                } catch (Exception e){
                    emitter.onError(e);
                }
                emitter.onComplete();
            }
        })
                .compose(new AsyncTransformer<Boolean>())
                .compose(getView().getLifecycleProvider().bindToLifecycle())
                .subscribe(new BaseObserver<Boolean>(){
                    @Override
                    public Object getTag() {
                        return null;
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if(aBoolean) {
                            getView().saveSuccess();
                        } else {
                            getView().saveFail();
                        }
                    }

                    @Override
                    public void onFail(Throwable t) {
                        super.onFail(t);
                        getView().saveFail();
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
