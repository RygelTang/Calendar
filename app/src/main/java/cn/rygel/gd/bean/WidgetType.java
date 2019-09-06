package cn.rygel.gd.bean;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.view.View;

import com.blankj.utilcode.util.ColorUtils;

import org.greenrobot.eventbus.EventBus;

import cn.rygel.gd.app.APP;
import cn.rygel.gd.setting.Settings;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.SolarUtils;
import rygel.cn.calendarview.CalendarView;
import rygel.cn.calendarview.MonthView;
import rygel.cn.calendarview.Options;

public enum WidgetType {

    MONTH_WIDGET() {
        @Override
        public Bitmap getDemoWidgetImage(Rect bound, @ColorInt int textColor) {
            final int width =  bound.width();
            final int height = bound.height();
            if (width <= 0 || height <= 0) {
                return null;
            }
            final int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            final int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            final Solar today = SolarUtils.today();
            CalendarView calendar = new CalendarView(APP.getInstance(), null);
            Options options = calendar.getConfig().getOptions();
            options.mWeekbarTextColor = textColor;
            options.mMakeUpTextColor = textColor;
            options.mCommonTextColor = textColor;
            options.mCornerTextColor = textColor;
            options.mTermTextColor = textColor;
            options.mHolidayTextColor = textColor;
            options.mThemeColor = ColorUtils.setAlphaComponent(Settings.getInstance().getCustomThemeColor(), Color.alpha(textColor));
            options.mCornerPadding = width / 100F;
            options.mWeekbarHeight = width / 24F;
            options.mWeekbarTextSize = width / 24F;
            options.mTextSize = width / 24F;
            options.mSubTextSize = width / 36F;
            options.mCornerTextSize = width / 42F;
            options.mShowToday = true;
            calendar.getConfig()
                    .setChildPaddingLeft(2)
                    .setChildPaddingTop(2)
                    .setChildPaddingRight(2)
                    .setChildPaddingBottom(2)
                    .setOptions(options)
                    .config();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            MonthView month = ((MonthView) calendar.getAdapter().instantiateItem(calendar, (today.solarYear - 1901) * 12 + today.solarMonth - 1));
            month.setPadding(width / 40, height / 20, width / 40, height / 40);
            month.measure(measuredWidth, measuredHeight);
            //调用layout方法布局后，可以得到view的尺寸大小
            month.layout(20, 20, calendar.getMeasuredWidth() - 20, calendar.getMeasuredHeight() - 20);
            month.skipFirstLoadAnimation();
            month.setTodayIndex(today.solarDay - 1);
            month.draw(canvas);
            return bmp;
        }

        @Override
        public void update(int widgetId) {
            EventBus.getDefault().post(new OnUpdateMonthWidgetEvent(widgetId));
        }
    };

    public abstract Bitmap getDemoWidgetImage(Rect bound, @ColorInt int textColor);

    public abstract void update(int widgetId);

}
