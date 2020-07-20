package cn.rygel.gd.deamon;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;

import cn.rygel.gd.IDaemon;

/**
 * 远程进程服务
 */
public class DaemonService extends Service {
    private ScreenBroadcastReceiver screenBroadcastReceiver = new ScreenBroadcastReceiver();
    private boolean connected = false;

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
            connected = true;
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
            connected = false;
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
            if (DaemonHolder.mService != null) {
                Intent serviceIntent = new Intent(this, DaemonHolder.mService);
                startService(serviceIntent);
                bindService(new Intent(this, DaemonHolder.mService), serviceConnection, Context.BIND_IMPORTANT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i("onCreate()");
        startBindService();
        listenNetworkConnectivity();
        screenBroadcastReceiver.registerScreenBroadcastReceiver(this);
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
        if (connected) {
            unbindService(serviceConnection);
        }

        DaemonHolder.restartService(getApplicationContext(), getClass());
        screenBroadcastReceiver.unregisterScreenBroadcastReceiver(this);
    }

    private void listenNetworkConnectivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        Logger.i("onAvailable()");
                        DaemonHolder.startService();
                    }

                    @Override
                    public void onUnavailable() {
                        super.onUnavailable();
                        Logger.i("onUnavailable()");
                        DaemonHolder.startService();
                    }

                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                        Logger.i("onLost()");
                        DaemonHolder.startService();
                    }
                });
            }
        }
    }

    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private boolean isRegistered = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Logger.e("onReceive() action: " + intent.getAction());
            }
            DaemonHolder.startService();
        }

        public void registerScreenBroadcastReceiver(Context context) {
            if (!isRegistered) {
                isRegistered = true;
                IntentFilter filter = new IntentFilter();
                filter.addAction(Intent.ACTION_SCREEN_ON); // 开屏
                filter.addAction(Intent.ACTION_SCREEN_OFF); // 锁屏
                filter.addAction(Intent.ACTION_USER_PRESENT); // 解锁
                filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); // Home键
                context.registerReceiver(ScreenBroadcastReceiver.this, filter);
            }
        }

        public void unregisterScreenBroadcastReceiver(Context context) {
            if (isRegistered) {
                isRegistered = false;
                context.unregisterReceiver(ScreenBroadcastReceiver.this);
            }
        }
    }
}
