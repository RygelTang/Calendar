package rygel.cn.uilibrary.mvp;

public interface IView {

    void refresh();
    void showToast(String str);
    void onLoading();
    void onLoadFinish();

}
