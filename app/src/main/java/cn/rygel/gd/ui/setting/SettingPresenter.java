package cn.rygel.gd.ui.setting;

import com.tencent.mmkv.MMKV;

import rygel.cn.uilibrary.mvp.BasePresenter;

public class SettingPresenter extends BasePresenter<ISettingView> {

    private MMKV mKV = MMKV.defaultMMKV();

    private static final String KEY_KEEP_ALIVE = "KEY_KEEP_ALIVE";
    private static final String KEY_WEEKDAY_OFFSET = "KEY_WEEKDAY_OFFSET";

    boolean putKeepAlive(boolean state) {
        return mKV.encode(KEY_KEEP_ALIVE, state);
    }

    boolean putWeekDayOffset(int offset) {
        return mKV.encode(KEY_WEEKDAY_OFFSET, offset);
    }

    boolean isKeepAlive() {
        return mKV.decodeBool(KEY_KEEP_ALIVE,true);
    }

    int getWeekdayOffset() {
        return mKV.decodeInt(KEY_WEEKDAY_OFFSET,0);
    }

}
