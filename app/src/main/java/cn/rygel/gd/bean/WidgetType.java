package cn.rygel.gd.bean;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.view.View;

import cn.rygel.gd.app.APP;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.SolarUtils;
import rygel.cn.calendarview.CalendarView;
import rygel.cn.calendarview.MonthView;
import rygel.cn.calendarview.Options;

public enum WidgetType {

    MONTH_WIDGET() {
        @Override
        public Bitmap getDemoWidgetImage(Bitmap background, @ColorInt int textColor) {
            final int width =  background.getWidth();
            final int height = background.getHeight();
            final int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            final int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            final int min = Math.min(width, height);
            final Solar today = SolarUtils.today();
            CalendarView calendar = new CalendarView(APP.getInstance(), null);
            Options options = calendar.getConfig().getOptions();
            options.mWeekbarTextColor = textColor;
            options.mMakeUpTextColor = textColor;
            options.mCommonTextColor = textColor;
            options.mCornerTextColor = textColor;
            options.mTermTextColor = textColor;
            options.mHolidayTextColor = textColor;
            options.mThemeColor = textColor;
            options.mWeekbarTextSize = min / 20F;
            options.mTextSize = min / 20F;
            options.mSubTextSize = min / 30F;
            options.mCornerTextSize = min / 40F;
            calendar.getConfig().setOptions(options).config();
            Canvas canvas = new Canvas(background);
            MonthView month = ((MonthView) calendar.getAdapter().instantiateItem(calendar, (today.solarYear - 1901) * 12 + today.solarMonth - 1));
            month.setPadding(width / 40, height / 20, width / 40, height / 40);
            month.measure(measuredWidth, measuredHeight);
            //调用layout方法布局后，可以得到view的尺寸大小
            month.layout(20, 20, calendar.getMeasuredWidth() - 20, calendar.getMeasuredHeight() - 20);
            month.draw(canvas);
            return background;
        }

    };

    public abstract Bitmap getDemoWidgetImage(Bitmap background, @ColorInt int textColor);

}
