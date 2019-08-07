package cn.rygel.gd.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.rygel.gd.IRemoteService;
import cn.rygel.gd.R;
import cn.rygel.gd.bean.OnDateChangedEvent;
import cn.rygel.gd.bean.OnEventAddedEvent;
import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.bean.event.base.LocationEvent;
import cn.rygel.gd.db.model.EventModel;
import cn.rygel.gd.ui.index.activity.MainActivity;
import cn.rygel.gd.utils.observer.AsyncTransformer;
import cn.rygel.gd.utils.observer.BaseObserver;
import io.reactivex.Observable;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.SolarUtils;
import rygel.cn.uilibrary.utils.UIUtils;
import rygel.cn.uilibrary.utils.WeakHandler;

public class LocalService extends Service {

    private ServiceConnection mConn;
    private MyService myService;

    private PushHandler mPushHandler = new PushHandler(this);

    @Override
    public IBinder onBind(Intent intent) {
        return myService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i("LocalService created");
        EventBus.getDefault().register(this);
        init();
        initEvents();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel();
        }
    }

    private void init() {
        if (mConn == null) {
            mConn = new MyServiceConnection();
        }
        myService = new MyService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Intent intents = new Intent();
            intents.setClass(this, RemoteService.class);
            bindService(intents, mConn, Context.BIND_IMPORTANT);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    class MyService extends IRemoteService.Stub {
        @Override
        public String getServiceName() throws RemoteException {
            return null;
        }
    }

    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                LocalService.this.startService(new Intent(LocalService.this,
                        RemoteService.class));
                LocalService.this.bindService(new Intent(LocalService.this,
                        RemoteService.class), mConn, Context.BIND_IMPORTANT);
            }
        }

    }

    protected static class PushHandler extends WeakHandler<LocalService> {

        private static final int MESSAGE_PUSH_EVENT = 0;

        public PushHandler(LocalService localService) {
            super(localService);
        }

        @Override
        protected void handleMessage(LocalService localService, Message message) {
            switch (message.what) {
                case MESSAGE_PUSH_EVENT:
                    Logger.i("try to push notification");
                    if(message.obj instanceof BaseEvent) {
                        localService.pushNotification(message.obj.hashCode(),formatNotification(localService,(BaseEvent) message.obj));
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
        String channelId = UIUtils.getString(this,R.string.app_name);
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

    private static Notification formatNotification(Context context,BaseEvent event) {
        Intent intent = new Intent(context,MainActivity.class);
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
