package cn.rygel.gd.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import cn.rygel.gd.IRemoteService;
import cn.rygel.gd.bean.event.base.BaseEvent;
import rygel.cn.uilibrary.utils.WeakHandler;

public class LocalService extends Service {

    private ServiceConnection conn;
    private MyService myService;

    @Override
    public IBinder onBind(Intent intent) {
        return myService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        initEvents();
    }

    private void init() {
        if (conn == null) {
            conn = new MyServiceConnection();
        }
        myService = new MyService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intents = new Intent();
        intents.setClass(this, RemoteService.class);
        bindService(intents, conn, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    private void initEvents() {
        // TODO: 2019/1/23 从数据库中读取数据，并且通过Handler延时发送，实现定时推送
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
            LocalService.this.startService(new Intent(LocalService.this,
                    RemoteService.class));
            LocalService.this.bindService(new Intent(LocalService.this,
                    RemoteService.class), conn, Context.BIND_IMPORTANT);

        }

    }

    private void pushNotification(BaseEvent event) {
        // TODO: 2019/1/23 推送通知的具体逻辑
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
                    if(message.obj instanceof BaseEvent) {
                        localService.pushNotification((BaseEvent) message.obj);
                    }
                    break;
            }
        }
    }

}
