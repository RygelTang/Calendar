package cn.rygel.gd.ui.setting;

import cn.rygel.gd.setting.Settings;
import rygel.cn.uilibrary.mvp.BasePresenter;

public class SettingPresenter extends BasePresenter<ISettingView> {

    boolean putKeepAlive(boolean state) {
        return Settings.getInstance().putKeepAlive(state);
    }

    boolean putWeekDayOffset(int offset) {
        return Settings.getInstance().putWeekDayOffset(offset);
    }

    boolean isKeepAlive() {
        return Settings.getInstance().isKeepAlive();
    }

    int getWeekdayOffset() {
        return Settings.getInstance().getWeekdayOffset();
    }

}
