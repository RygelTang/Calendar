package cn.rygel.gd.setting;

import com.blankj.utilcode.util.ColorUtils;
import com.tencent.mmkv.MMKV;

import cn.rygel.gd.R;

public class Settings {

    private MMKV mKV = MMKV.defaultMMKV();

    private static final String KEY_KEEP_ALIVE = "KEY_KEEP_ALIVE";
    private static final String KEY_WEEKDAY_OFFSET = "KEY_WEEKDAY_OFFSET";
    private static final String KEY_HIDE_STATUS = "KEY_HIDE_STATUS";
    private static final String KEY_THEME_COLOR = "KEY_THEME_COLOR";
    private static final String KEY_THEME_COLOR_DARK = "KEY_THEME_COLOR_DARK";
    private static final String KEY_THEME_COLOR_LIGHT = "KEY_THEME_COLOR_LIGHT";
    private static final String KEY_ACCENT_COLOR = "KEY_ACCENT_COLOR";
    private static final String KEY_THEME_INDEX = "KEY_THEME_INDEX";

    public boolean putKeepAlive(boolean state) {
        return mKV.encode(KEY_KEEP_ALIVE, state);
    }

    public boolean putHideStatus(boolean state) {
        return mKV.encode(KEY_HIDE_STATUS, state);
    }

    public boolean putWeekDayOffset(int offset) {
        return mKV.encode(KEY_WEEKDAY_OFFSET, offset);
    }

    public boolean isKeepAlive() {
        return mKV.decodeBool(KEY_KEEP_ALIVE,false);
    }

    public int getWeekdayOffset() {
        return mKV.decodeInt(KEY_WEEKDAY_OFFSET,0);
    }

    public boolean isHideStatus() {
        return mKV.decodeBool(KEY_HIDE_STATUS, false);
    }

    public boolean putThemeIndex(int index) {
        return mKV.encode(KEY_THEME_INDEX, index);
    }

    public int getThemeIndex() {
        return mKV.decodeInt(KEY_THEME_INDEX, 0);
    }

    public boolean putCustomThemeColorDark(int color) {
        return mKV.encode(KEY_THEME_COLOR_DARK, color);
    }

    public int getCustomThemeColorDark() {
        return mKV.decodeInt(KEY_THEME_COLOR_DARK, ColorUtils.getColor(R.color.colorDefaultThemeDark));
    }

    public boolean putCustomThemeColorLight(int color) {
        return mKV.encode(KEY_THEME_COLOR_LIGHT, color);
    }

    public int getCustomThemeColorLight() {
        return mKV.decodeInt(KEY_THEME_COLOR_LIGHT, ColorUtils.getColor(R.color.colorDefaultThemeLight));
    }

    public boolean putCustomThemeColor(int color) {
        return mKV.encode(KEY_THEME_COLOR, color);
    }

    public int getCustomThemeColor() {
        return mKV.decodeInt(KEY_THEME_COLOR, ColorUtils.getColor(R.color.colorDefaultTheme));
    }

    public boolean putCustomAccentColor(int color) {
        return mKV.encode(KEY_ACCENT_COLOR, color);
    }

    public int getCustomAccentColor() {
        return mKV.decodeInt(KEY_ACCENT_COLOR, ColorUtils.getColor(R.color.colorDefaultAccent));
    }

    public static Settings getInstance(){
        return Instance.sInstance;
    }

    private static class Instance{
        private static Settings sInstance = new Settings();
    }
}
