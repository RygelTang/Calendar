package rygel.cn.uilibrary.mvp;

import androidx.annotation.StringRes;

import com.trello.rxlifecycle2.LifecycleProvider;

public interface IView {

    <T> LifecycleProvider<T> getLifecycleProvider();

    void refresh();
    void showToast(String str);
    void showToast(@StringRes int strRes);
    void onLoading();
    void onLoadFinish();

}
