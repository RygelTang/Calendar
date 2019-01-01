package cn.rygel.gd.utils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseObserver<T> implements Subscriber<T> {

    /**
     * 对异步任务进行管理
     */
    private static Map<Object, Subscription> sSubscriptions = new HashMap<>();

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
    public abstract void onFail(Throwable t);

    /**
     * 取消任务
     */
    private void cancel(){
        cancel(getTag());
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
    }

    @Override
    public void onComplete() {
       sSubscriptions.remove(getTag());
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
        }
    }

}
