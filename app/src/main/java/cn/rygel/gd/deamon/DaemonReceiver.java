package cn.rygel.gd.deamon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;

public class DaemonReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Logger.i("receive action " + intent.getAction());
        }
        DaemonHolder.startService();
    }
}
