package rygel.cn.uilibrary.mvp;

import java.lang.ref.WeakReference;

public class BasePresenter<V extends IView> implements IPresenter<V> {

    /**
     * 使用虚引用，避免内存泄漏
     */
    private WeakReference<V> mVWeakReference = null;

    /**
     * 释放子View
     */
    @Override
    public void dropView(){
        if (mVWeakReference != null) {
            mVWeakReference.clear();
            mVWeakReference = null;
        }
    }

    /**
     * 绑定子View
     * @param view
     */
    @Override
    public void bindView(V view){
        mVWeakReference = new WeakReference<V>(view);
    }

    /**
     * 获取取View
     * @return
     */
    protected V getView(){
        return mVWeakReference == null ? null : mVWeakReference.get();
    }
}
