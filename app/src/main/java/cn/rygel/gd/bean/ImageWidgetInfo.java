package cn.rygel.gd.bean;

import com.blankj.utilcode.util.GsonUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import cn.rygel.gd.app.APP;
import cn.rygel.gd.utils.mmkv.MMKVs;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class ImageWidgetInfo {

    private static final int DEFAULT_BITMAP_WIDTH = AutoSizeUtils.dp2px(APP.getInstance(), 240);

    private WidgetType mType;
    private int mWidgetId;
    private int mTextColor;
    private int mBgColor;
    private int mCornerRadius;
    private int mBgAlpha;
    private String mBgPath;

    private int mBgWidth;
    private int mBgHeight;

    private boolean mOnlyColorBg;
    private boolean mShouldBlur;

    private boolean mHideTitle;

    public ImageWidgetInfo(WidgetType mType, int mWidgetId, int mTextColor, int mBgColor, int mCornerRadius, int mBgAlpha, String mBgPath, int mBgWidth, int mBgHeight, boolean mOnlyColorBg, boolean mShouldBlur, boolean mHideTitle) {
        this.mType = mType;
        this.mWidgetId = mWidgetId;
        this.mTextColor = mTextColor;
        this.mBgColor = mBgColor;
        this.mCornerRadius = mCornerRadius;
        this.mBgAlpha = mBgAlpha;
        this.mBgPath = mBgPath;
        this.mBgWidth = mBgWidth;
        this.mBgHeight = mBgHeight;
        this.mOnlyColorBg = mOnlyColorBg;
        this.mShouldBlur = mShouldBlur;
        this.mHideTitle = mHideTitle;
    }

    public WidgetType getType() {
        return mType;
    }

    public void setType(WidgetType type) {
        mType = type;
    }

    public int getWidgetId() {
        return mWidgetId;
    }

    public void setWidgetId(int widgetId) {
        mWidgetId = widgetId;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public int getBgColor() {
        return mBgColor;
    }

    public void setBgColor(int bgColor) {
        mBgColor = bgColor;
    }

    public int getCornerRadius() {
        return mCornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        mCornerRadius = cornerRadius;
    }

    public int getBgAlpha() {
        return mBgAlpha;
    }

    public void setBgAlpha(int bgAlpha) {
        mBgAlpha = bgAlpha;
    }

    public String getBgPath() {
        return mBgPath;
    }

    public void setBgPath(String bgPath) {
        mBgPath = bgPath;
    }

    public boolean isOnlyColorBg() {
        return mOnlyColorBg;
    }

    public void setOnlyColorBg(boolean onlyColorBg) {
        mOnlyColorBg = onlyColorBg;
    }

    public boolean isShouldBlur() {
        return mShouldBlur;
    }

    public void setShouldBlur(boolean shouldBlur) {
        mShouldBlur = shouldBlur;
    }

    public int getBgWidth() {
        return mBgWidth;
    }

    public void setBgWidth(int bgWidth) {
        mBgWidth = bgWidth;
        mBgHeight = DEFAULT_BITMAP_WIDTH * mBgHeight / mBgWidth;
        mBgWidth = DEFAULT_BITMAP_WIDTH;
    }

    public int getBgHeight() {
        return mBgHeight;
    }

    public void setBgHeight(int bgHeight) {
        mBgHeight = bgHeight;
        mBgHeight = DEFAULT_BITMAP_WIDTH * mBgHeight / mBgWidth;
        mBgWidth = DEFAULT_BITMAP_WIDTH;
    }

    public boolean isHideTitle() {
        return mHideTitle;
    }

    public void setHideTitle(boolean mHideTitle) {
        this.mHideTitle = mHideTitle;
    }

    public void save() {
        save(this);
    }

    public void remove() {
        remove(this);
    }

    public static void save(ImageWidgetInfo info) {
        MMKVs.WIDGET.getMMKV().putString(info.mType.name() + info.mWidgetId, GsonUtils.toJson(info));
        EventBus.getDefault().post(new OnUpdateMonthWidgetEvent(info.getWidgetId()));
    }

    public static void remove(ImageWidgetInfo info) {
        MMKVs.WIDGET.getMMKV().remove(info.mType.name() + info.mWidgetId);
    }

    public static ImageWidgetInfo getById(WidgetType type, int id) {
        String str = MMKVs.WIDGET.getMMKV().getString(type.name() + id, "");
        if (str != null && !str.isEmpty()) {
            try {
                return GsonUtils.fromJson(str, ImageWidgetInfo.class);
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
        }
        return null;
    }
}