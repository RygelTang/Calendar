package cn.rygel.gd.utils;

import com.orhanobut.logger.Logger;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Subscriber<T>,Observer<T> {

    private static final String TAG = "BaseObserver";

    /**
     * 对异步任务进行管理
     */
    private static Map<Object, Subscription> sSubscriptions = new HashMap<>();
    private static Map<Object, Disposable> sDisposables = new HashMap<>();

    /**
     * 为Observer设置TAG，这个TAG是用于方便取消异步任务
     * @return 不要返回null值
     */
    public abstract Object getTag();

    /**
     * 请求成功的回调
     */
    public abstract void onSuccess(T t);

    /**
     * 请求失败的回调
     */
    public void onFail(Throwable t){
        Logger.e(TAG,getTag(),t,t.getMessage());
    }

    /**
     * 取消任务
     */
    private void cancel(){
        cancel(getTag());
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onSubscribe(Subscription s) {
        sSubscriptions.put(getTag(),s);
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable t) {
        onFail(t);
        cancel();
    }

    @Override
    public void onComplete() {
       cancel();
    }

    /**
     * 根据TAG取消指定的任务
     * @param tag
     */
    public static void cancel(Object tag){
        Subscription subscription = sSubscriptions.get(tag);
        if(subscription != null){
            subscription.cancel();
            sSubscriptions.remove(tag);
        } else {
            Disposable disposable = sDisposables.get(tag);
            if(disposable != null){
                if(!disposable.isDisposed()) {
                    disposable.dispose();
                }
                sDisposables.remove(tag);
            }
        }
    }

}
