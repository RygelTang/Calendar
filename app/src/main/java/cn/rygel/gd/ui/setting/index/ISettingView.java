package cn.rygel.gd.ui.setting.index;

import rygel.cn.uilibrary.mvp.IView;

public interface ISettingView extends IView {

    void onBackupSuccess();
    void onBackupFail();

    void onRestoreSuccess();
    void onRestoreFail(String err);
}
