package cn.rygel.gd.widget.month;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.widget.RemoteViews;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import cn.rygel.gd.R;
import cn.rygel.gd.bean.OnDateChangedEvent;
import cn.rygel.gd.bean.OnUpdateMonthWidgetEvent;
import cn.rygel.gd.bean.WidgetType;
import cn.rygel.gd.ui.index.activity.MainActivity;
import cn.rygel.gd.utils.mmkv.MMKVs;

/**
 * Implementation of App Widget functionality.
 */
public class MonthWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String imgPath = MMKVs.WIDGET.getMMKV().getString(WidgetType.MONTH_WIDGET.name() + "img" + appWidgetId, "");
        int textColor = MMKVs.WIDGET.getMMKV().getInt(WidgetType.MONTH_WIDGET.name() + "tv" + appWidgetId, Color.BLACK);

        File img = new File(imgPath);
        if (img.isFile() && img.exists()) {
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_common_img);
            try {
                views.setImageViewBitmap(R.id.img_common_widget, WidgetType.MONTH_WIDGET.getDemoWidgetImage(ImageUtils.getBitmap(img).copy(Bitmap.Config.ARGB_8888, true), textColor));
                views.setOnClickPendingIntent(R.id.img_common_widget, getPendingIntent(context));
                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
        } else {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_common_img);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private static PendingIntent getPendingIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, 0,intent,0);
    }

    @Subscribe
    public void onDateChanged(OnDateChangedEvent event) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(Utils.getApp());
        final int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(Utils.getApp(), MonthWidget.class));
        for (int widgetId : widgetIds){
            updateAppWidget(Utils.getApp(), appWidgetManager, widgetId);
        }
    }

    @Subscribe
    public void update(OnUpdateMonthWidgetEvent event) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(Utils.getApp());
        updateAppWidget(Utils.getApp(), appWidgetManager, event.mWidgetId);
    }

    public MonthWidget() {
        super();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            MMKVs.WIDGET.getMMKV().remove(WidgetType.MONTH_WIDGET.name() + "img" + appWidgetId);
            MMKVs.WIDGET.getMMKV().remove(WidgetType.MONTH_WIDGET.name() + "tv" + appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        EventBus.getDefault().register(this);
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        EventBus.getDefault().unregister(this);
        // Enter relevant functionality for when the last widget is disabled
    }
}

