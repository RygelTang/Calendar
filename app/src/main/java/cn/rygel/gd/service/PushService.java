package cn.rygel.gd.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.rygel.gd.R;
import cn.rygel.gd.bean.OnDateChangedEvent;
import cn.rygel.gd.bean.OnEventAddedEvent;
import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.bean.event.base.LocationEvent;
import cn.rygel.gd.db.model.EventModel;
import cn.rygel.gd.deamon.AbsHeartBeatService;
import cn.rygel.gd.ui.index.activity.MainActivity;
import cn.rygel.gd.utils.observer.AsyncTransformer;
import cn.rygel.gd.utils.observer.BaseObserver;
import io.reactivex.Observable;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.SolarUtils;
import rygel.cn.uilibrary.utils.UIUtils;
import rygel.cn.uilibrary.utils.WeakHandler;

public class PushService extends AbsHeartBeatService {

    private PushHandler mPushHandler = new PushHandler(this);

    @Override
    public void onStartService() {
        Logger.i("service start work!");
        EventBus.getDefault().register(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel();
            createKeepAliveNotificationChannel();
            startForeground(1, getKeepAliveNotification());
        }
        initEvents();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private Notification getKeepAliveNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = null;
        builder = new Notification.Builder(this,UIUtils.getString(this,R.string.keep_alive_notification));
        return builder.setContentTitle(StringUtils.getString(R.string.app_name))
                .setContentText(StringUtils.getString(R.string.running))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
    }

    @Override
    public void onStopService() {
        Logger.e("service killed");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public long getDelayExecutedMillis() {
        return 200;
    }

    @Override
    public long getHeartBeatMillis() {
        return 200;
    }

    @Override
    public void onHeartBeat() {
        Logger.v("heart beat");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAdded(OnEventAddedEvent event) {
        // 重新加载数据
        initEvents();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDateChanged(OnDateChangedEvent event) {
        initEvents();
    }

    private void initEvents() {
        Logger.i("init events to push");
        mPushHandler.removeMessages(PushHandler.MESSAGE_PUSH_EVENT);
        // 异步查询数据
        Observable.just(EventModel.getInstance().queryInDay(SolarUtils.today()))
                .compose(new AsyncTransformer<>())
                .subscribe(new BaseObserver<List<BaseEvent>>() {
                    @Override
                    public Object getTag() {
                        return "init events";
                    }

                    @Override
                    public void onFail(Throwable t) {
                        super.onFail(t);
                        t.printStackTrace(System.err);
                    }

                    @Override
                    public void onSuccess(List<BaseEvent> events) {
                        Logger.i("find " + events.size() + " events!");
                        for(BaseEvent event : events) {
                            // 不提醒，则跳过
                            if(!event.isShowNotification()) {
                                continue;
                            }
                            long offset = getAlertTimeOffset(event);
                            Logger.i("push notification in " + offset + " millis!");
                            // 已经过了提醒时间，跳过
                            if(offset < 0) {
                                continue;
                            }
                            // 延时推送通知
                            mPushHandler.sendMessageDelayed(obtainEventMsg(event), offset);
                        }
                    }
                });
        Logger.i("start query events");

    }

    protected static class PushHandler extends WeakHandler<PushService> {

        private static final int MESSAGE_PUSH_EVENT = 0;

        public PushHandler(PushService service) {
            super(service);
        }

        @Override
        protected void handleMessage(PushService service, Message message) {
            switch (message.what) {
                case MESSAGE_PUSH_EVENT:
                    Logger.i("try to push notification");
                    if(message.obj instanceof BaseEvent) {
                        service.pushNotification(message.obj.hashCode(),formatNotification(service,(BaseEvent) message.obj));
                    }
                    break;
            }
        }
    }

    private void pushNotification(int id, Notification notification) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(id,notification);
    }

    @TargetApi(value = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        Logger.i("notification channel created");
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = UIUtils.getString(this, R.string.event_notification);
        String channelName = UIUtils.getString(this,R.string.event_notification);
        String channelDescription = UIUtils.getString(this,R.string.event_notification_channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(channelId,channelName,importance);
        channel.setDescription(channelDescription);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        manager.createNotificationChannel(channel);
    }

    @TargetApi(value = Build.VERSION_CODES.O)
    private void createKeepAliveNotificationChannel() {
        Logger.i("keep alive notification channel created");
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = UIUtils.getString(this, R.string.keep_alive_notification);
        String channelName = UIUtils.getString(this,R.string.keep_alive_notification);
        String channelDescription = UIUtils.getString(this,R.string.keep_alive_notification_channel_description);
        int importance = NotificationManager.IMPORTANCE_NONE;
        NotificationChannel channel = new NotificationChannel(channelId,channelName,importance);
        channel.setDescription(channelDescription);
        channel.enableLights(false);
        channel.enableVibration(false);
        manager.createNotificationChannel(channel);
    }

    private static Notification formatNotification(Context context,BaseEvent event) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context,UIUtils.getString(context,R.string.app_name));
        } else {
            builder = new Notification.Builder(context);
        }
        return builder.setContentTitle(event.getEventType().getDescription() + " " + event.getName())
                .setContentText(event instanceof LocationEvent ? "地点: " + ((LocationEvent) event).getLocation() : "备注：" + event.getDescription())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
    }

    private static Message obtainEventMsg(BaseEvent event) {
        Message msg = Message.obtain();
        msg.what = PushHandler.MESSAGE_PUSH_EVENT;
        msg.obj = event;
        return msg;
    }

    private static long getAlertTimeOffset(BaseEvent event) {
        long offset = toMillisTime(event.getEventSolarDate(), event.getTimeZone());
        long current = System.currentTimeMillis();
        return event.getStart() * 60 * 1000L + offset - current;
    }

    private static long toMillisTime(Solar solar, long timeZone) {
        int interval = SolarUtils.getIntervalDays(new Solar(1970, 1, 1), solar);
        return interval * 86400000L - timeZone;
    }


}
