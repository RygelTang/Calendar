package cn.rygel.gd.ui.edit;

import java.util.List;

import rygel.cn.uilibrary.mvp.IView;

public interface IEditEventView extends IView {

    void saveSuccess();

    void saveFail();

    void showUserList(List<String> users);


}
