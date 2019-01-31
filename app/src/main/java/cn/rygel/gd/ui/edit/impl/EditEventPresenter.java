package cn.rygel.gd.ui.edit.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.db.entity.User;
import cn.rygel.gd.db.model.EventModel;
import cn.rygel.gd.ui.edit.IEditEventView;
import cn.rygel.gd.utils.observer.AsyncTransformer;
import cn.rygel.gd.utils.observer.BaseObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import rygel.cn.uilibrary.mvp.BasePresenter;

public class EditEventPresenter extends BasePresenter<IEditEventView> {

    private static final String TAG = "EditEventPresenter";

    private EventModel mEventModel = EventModel.getInstance();

    private Set<String> mSubscribeTags = new HashSet<>();

    void update(BaseEvent event) {
        final String method = "update";
        final String subscribeTag = TAG + "#" + method;
        Observable.just(mEventModel.update(event))
                .compose(new AsyncTransformer<Boolean>())
                .compose(getView().getLifecycleProvider().bindToLifecycle())
                .subscribe(new BaseObserver<Boolean>(){
                    @Override
                    public Object getTag() {
                        return subscribeTag;
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if(aBoolean) {
                            getView().saveSuccess();
                        } else {
                            getView().saveFail();
                        }
                    }
                });
    }

    public void getUser(){
        final String method = "getUser";
        final String subscribeTag = TAG + "#" + method;
        mSubscribeTags.add(subscribeTag);
        BaseObserver.cancel(subscribeTag);
        Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> emitter) throws Exception {
                try {
                    emitter.onNext(mEventModel.getUser());
                } catch (Exception e){
                    emitter.onError(e);
                }
                emitter.onComplete();
            }
        })
                .compose(new AsyncTransformer<List<User>>())
                .compose(getView().getLifecycleProvider().bindToLifecycle())
                .map(new Function<List<User>,List<String>>() {
                    @Override
                    public List<String> apply(List<User> users) throws Exception {
                        List<String> result = new ArrayList<>();
                        for(User user : users){
                            result.add(user.getUserName());
                        }
                        if(result.size() == 0) {
                            result.add("默认用户");
                        }
                        return result;
                    }
                })
                .subscribe(new BaseObserver<List<String>>() {
                    @Override
                    public Object getTag() {
                        return subscribeTag;
                    }

                    @Override
                    public void onSuccess(List<String> list) {
                        getView().showUserList(list);
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
