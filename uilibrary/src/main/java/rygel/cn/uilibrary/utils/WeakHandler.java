package rygel.cn.uilibrary.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

public abstract class WeakHandler<T> extends Handler {

    protected WeakReference<T> mWeakReference;

    public WeakHandler(Looper looper, T t) {
        super(looper);
        mWeakReference = new WeakReference<>(t);
    }

    public WeakHandler(T t) {
        mWeakReference = new WeakReference<>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        T t = mWeakReference.get();
        if (t == null)
            return;
        handleMessage(t, msg);
    }

    protected abstract void handleMessage(T t, Message message);

}
