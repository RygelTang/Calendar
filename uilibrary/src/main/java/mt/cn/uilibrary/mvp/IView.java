package mt.cn.uilibrary.mvp;

public interface IView {

    void refresh();
    void showToast(String str);
    void onLoading();
    void onLoadFinish();

}
