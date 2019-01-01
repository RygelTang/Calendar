package rygel.cn.uilibrary.mvp;

import com.trello.rxlifecycle2.LifecycleProvider;

public interface IView {

    LifecycleProvider getLifecycleProvider();

    void refresh();
    void showToast(String str);
    void onLoading();
    void onLoadFinish();

}
