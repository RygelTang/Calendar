package cn.rygel.gd.ui.setting.index.impl;

import cn.rygel.gd.setting.Settings;
import cn.rygel.gd.ui.setting.index.ISettingView;
import rygel.cn.uilibrary.mvp.BasePresenter;

public class SettingPresenter extends BasePresenter<ISettingView> {

    public boolean putKeepAlive(boolean state) {
        return Settings.getInstance().putKeepAlive(state);
    }

    public boolean putWeekDayOffset(int offset) {
        return Settings.getInstance().putWeekDayOffset(offset);
    }

    public boolean putHideStatus(boolean isHideStatus) {
        return Settings.getInstance().putHideStatus(isHideStatus);
    }

    public boolean isKeepAlive() {
        return Settings.getInstance().isKeepAlive();
    }

    public int getWeekdayOffset() {
        return Settings.getInstance().getWeekdayOffset();
    }

    public boolean isHideStatus() {
        return Settings.getInstance().isHideStatus();
    }

}
