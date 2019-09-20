package cn.rygel.gd.deamon;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import java.util.Timer;
import java.util.TimerTask;

import cn.rygel.gd.IDaemon;

/**
 * 当前进程服务
 */
public abstract class AbsHeartBeatService extends Service {

    private Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            onHeartBeat();
        }
    };

    private final IDaemon aidl = new IDaemon.Stub() {
        @Override
        public void startService() throws RemoteException {
            Logger.i("aidl startService()");
        }

        @Override
        public void stopService() throws RemoteException {
            Logger.e("aidl stopService()");
        }
    };

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.i("onServiceConnected() 已绑定");
            try {
                service.linkToDeath(() -> {
                    Logger.e("onServiceConnected() linkToDeath");
                    try {
                        aidl.startService();
                        startBindService();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }, 1);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.e("onServiceDisconnected() 已解绑");
            try {
                aidl.stopService();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBindingDied(ComponentName name) {
            onServiceDisconnected(name);
        }
    };

    private void startBindService() {
        try {
            startService(new Intent(this, DaemonService.class));
            bindService(new Intent(this, DaemonService.class), serviceConnection, Context.BIND_IMPORTANT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i("onCreate()");
        onStartService();
        startBindService();
        if (getHeartBeatMillis() > 0) {
            timer.schedule(timerTask, getDelayExecutedMillis(), getHeartBeatMillis());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.i("onStartCommand()");
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.i("onBind()");
        return (IBinder) aidl;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.e("onDestroy()");
        onStopService();

        unbindService(serviceConnection);
        DaemonHolder.restartService(getApplicationContext(), DaemonHolder.mService);

        try {
            timer.cancel();
            timer.purge();
            timerTask.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void onStartService();

    public abstract void onStopService();

    public abstract long getDelayExecutedMillis();

    public abstract long getHeartBeatMillis();

    public abstract void onHeartBeat();
}
