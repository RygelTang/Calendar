package cn.rygel.gd.widget.calendar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import cn.rygel.gd.utils.provider.EventInfoProvider;
import cn.rygel.gd.utils.provider.impl.DefaultEventInfoProvider;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendarview.CalendarView;
import rygel.cn.calendarview.Options;
import rygel.cn.calendarview.item.impl.DefaultItemCommonImpl;

public class CustomItemCommon extends DefaultItemCommonImpl {

    private EventInfoProvider mEventInfoProvider = new DefaultEventInfoProvider();

    private Paint mEventDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mThemeColor;

    public CustomItemCommon(CalendarView view) {
        super(view);
    }

    @Override
    public void parseOptions(Options options) {
        super.parseOptions(options);
        mThemeColor = options.mThemeColor;
    }

    @Override
    public void draw(Canvas canvas, Rect bound, Solar solar) {
        super.draw(canvas, bound, solar);
        if (mEventInfoProvider.hasEvent(solar)) {
            mEventDotPaint.setColor(mThemeColor);
            mEventDotPaint.setStrokeCap(Paint.Cap.ROUND);
            mEventDotPaint.setStrokeWidth(8);
            float y = bound.bottom - (bound.bottom - bound.top) / 8F;
            canvas.drawPoint(bound.centerX(), y, mEventDotPaint);
        }
    }
}
