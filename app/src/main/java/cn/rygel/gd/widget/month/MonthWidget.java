package cn.rygel.gd.widget.month;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.rygel.gd.R;
import cn.rygel.gd.bean.ImageWidgetInfo;
import cn.rygel.gd.bean.OnDateChangedEvent;
import cn.rygel.gd.bean.OnUpdateMonthWidgetEvent;
import cn.rygel.gd.bean.WidgetType;
import cn.rygel.gd.ui.index.activity.MainActivity;
import cn.rygel.gd.ui.setting.widget.month.MonthWidgetSettingActivity;
import cn.rygel.gd.utils.ImageWidgetUtils;
import cn.rygel.gd.utils.mmkv.MMKVs;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.SolarUtils;

/**
 * Implementation of App Widget functionality.
 */
public class MonthWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int width, int height) {

        if (width <= 0 || height <= 0) return;

        ImageWidgetInfo info = ImageWidgetInfo.getById(WidgetType.MONTH_WIDGET, appWidgetId);
        if (info == null) {
            return;
        }

        Drawable setting;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setting = context.getResources().getDrawable(R.drawable.ic_settings_white_24dp, context.getTheme());
        } else {
            setting = context.getResources().getDrawable(R.drawable.ic_settings_white_24dp);
        }
        DrawableCompat.setTint(setting, info.getTextColor());
        setting.setAlpha(80);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_common_img);
        try {
//            final float bmpRatio = ((float) info.getBgWidth()) / info.getBgHeight();
//            final float widgetRatio = ((float) width) / height;
//
//            int paddingX = 0;
//            int paddingY = 0;
//            if (bmpRatio > widgetRatio) {
//                paddingY = - ((int) (width / bmpRatio) - height);
//            } else {
//                paddingX = - ((int) (height * bmpRatio) - width);
//            }
//            views.setViewPadding(R.id.layout_widget_title, paddingX,  paddingY, 0, 0);
            height = 1080 * height / width;
            width = 1080;
            views.setViewVisibility(R.id.layout_widget_title, info.isHideTitle() ? View.GONE : View.VISIBLE);
            views.setImageViewBitmap(R.id.img_background, ImageWidgetUtils.getBackgroundOf(info, width, height));
            views.setImageViewBitmap(R.id.btn_setting, ConvertUtils.drawable2Bitmap(setting));
            views.setTextViewText(R.id.tv_widget_tips, generateMonthInfo(SolarUtils.today()));
            views.setTextColor(R.id.tv_widget_tips, info.getTextColor());
            views.setImageViewBitmap(R.id.img_common_widget, ImageWidgetUtils.getForegroundOf(info, width, height));
            views.setOnClickPendingIntent(R.id.img_common_widget, getPendingIntent(context));
            views.setOnClickPendingIntent(R.id.btn_setting, getSettingPendingIntent(context, info.getWidgetId()));
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(info.getWidgetId(), views);
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    private static String generateMonthInfo(Solar solar) {
        return solar.solarYear + "年" + solar.solarMonth + "月";
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        final int width = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
        final int height = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);
        MMKVs.WIDGET.getMMKV().putInt(WidgetType.MONTH_WIDGET.name() + "widget_width" + appWidgetId, width);
        MMKVs.WIDGET.getMMKV().putInt(WidgetType.MONTH_WIDGET.name() + "widget_height" + appWidgetId, height);
        updateAppWidget(context, appWidgetManager, appWidgetId, width, height);
    }

    private static PendingIntent getPendingIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context,0 ,intent,0);
    }

    private static PendingIntent getSettingPendingIntent(Context context, int widgetId){
        Intent intent = new Intent(context, MonthWidgetSettingActivity.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        return PendingIntent.getActivity(context, (int) (Math.random() * 1000000),intent,0);
    }

    @Subscribe
    public void onDateChanged(OnDateChangedEvent event) {
        updateAll();
    }

    public void updateAll() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(Utils.getApp());
        final int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(Utils.getApp(), MonthWidget.class));
        for (int widgetId : widgetIds){
            try {
                final int width = MMKVs.WIDGET.getMMKV().getInt(WidgetType.MONTH_WIDGET.name() + "widget_width" + widgetId, 540);
                final int height = MMKVs.WIDGET.getMMKV().getInt(WidgetType.MONTH_WIDGET.name() + "widget_height" + widgetId, 810);
                updateAppWidget(Utils.getApp(), appWidgetManager, widgetId, width, height);
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
        }
    }

    @Subscribe
    public void update(OnUpdateMonthWidgetEvent event) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(Utils.getApp());
        try {
            final int width = MMKVs.WIDGET.getMMKV().getInt(WidgetType.MONTH_WIDGET.name() + "widget_width" + event.mWidgetId, 540);
            final int height = MMKVs.WIDGET.getMMKV().getInt(WidgetType.MONTH_WIDGET.name() + "widget_height" + event.mWidgetId, 810);
            updateAppWidget(Utils.getApp(), appWidgetManager, event.mWidgetId, width, height);
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    public MonthWidget() {
        super();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        updateAll();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            MMKVs.WIDGET.getMMKV().remove(WidgetType.MONTH_WIDGET.name() + "widget_width" + appWidgetId);
            MMKVs.WIDGET.getMMKV().remove(WidgetType.MONTH_WIDGET.name() + "widget_height" + appWidgetId);
            ImageWidgetInfo info = ImageWidgetInfo.getById(WidgetType.MONTH_WIDGET, appWidgetId);
            if (info != null) {
                info.remove();
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        EventBus.getDefault().unregister(this);
        // Enter relevant functionality for when the last widget is disabled
    }
}

