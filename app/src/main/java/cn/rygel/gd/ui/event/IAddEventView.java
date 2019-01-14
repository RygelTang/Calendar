package cn.rygel.gd.ui.event;

import java.util.List;

import rygel.cn.uilibrary.mvp.IView;

public interface IAddEventView extends IView {

    void saveSuccess();

    void saveFail();

    void showUserList(List<String> users);

}
