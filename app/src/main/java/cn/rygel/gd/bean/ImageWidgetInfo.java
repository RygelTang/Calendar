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

    public ImageWidgetInfo(WidgetType type, int widgetId, int textColor, int bgColor, int cornerRadius, int bgAlpha, String bgPath, int bgWidth, int bgHeight, boolean onlyColorBg, boolean shouldBlur) {
        mType = type;
        mWidgetId = widgetId;
        mTextColor = textColor;
        mBgColor = bgColor;
        mCornerRadius = cornerRadius;
        mBgAlpha = bgAlpha;
        mBgPath = bgPath;
        mBgWidth = DEFAULT_BITMAP_WIDTH;
        mBgHeight = DEFAULT_BITMAP_WIDTH * bgHeight / bgWidth;
        mOnlyColorBg = onlyColorBg;
        mShouldBlur = shouldBlur;
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

    public void save() {
        save(this);
        EventBus.getDefault().post(new OnUpdateMonthWidgetEvent(mWidgetId));
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