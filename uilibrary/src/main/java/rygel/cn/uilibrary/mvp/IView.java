package rygel.cn.uilibrary.mvp;

import android.support.annotation.StringRes;

import com.trello.rxlifecycle2.LifecycleProvider;

public interface IView {

    LifecycleProvider getLifecycleProvider();

    void refresh();
    void showToast(String str);
    void showToast(@StringRes int strRes);
    void onLoading();
    void onLoadFinish();

}
