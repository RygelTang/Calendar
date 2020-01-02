package cn.rygel.gd.view.calendar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;

import cn.rygel.gd.utils.provider.EventInfoProvider;
import cn.rygel.gd.utils.provider.impl.DefaultEventInfoProvider;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendarview.CalendarView;
import rygel.cn.calendarview.item.impl.DefaultItemTodayImpl;

public class CustomItemToday extends DefaultItemTodayImpl {

    private EventInfoProvider mEventInfoProvider = new DefaultEventInfoProvider();

    private Paint mEventDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CustomItemToday(CalendarView view) {
        super(view);
        setProviderByReflex(this);
    }

    @Override
    public void draw(Canvas canvas, Rect bound, Solar solar) {
        super.draw(canvas, bound, solar);
        if (mEventInfoProvider.hasEvent(solar)) {
            mEventDotPaint.setColor(mBackgroundColor);
            mEventDotPaint.setStrokeCap(Paint.Cap.ROUND);
            mEventDotPaint.setStrokeWidth(8);
            float y = bound.bottom - (bound.bottom - bound.top) / 8F;
            canvas.drawPoint(bound.centerX(), y, mEventDotPaint);
        }
    }

    private static void setProviderByReflex(Object obj) {
        Class clz = obj.getClass().getSuperclass();
        if (clz == null) {
            return;
        }
        try {
            Field f = clz.getDeclaredField("mHolidayInfoProvider");
            f.setAccessible(true);
            f.set(obj, new cn.rygel.gd.utils.provider.HolidayInfoProvider());
        } catch (NoSuchFieldException e) {
            Logger.e(e.getMessage());
        } catch (IllegalAccessException e) {
            Logger.e(e.getMessage());
        }
    }
}
