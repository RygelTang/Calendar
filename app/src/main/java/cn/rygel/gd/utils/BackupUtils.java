package cn.rygel.gd.utils;

import android.os.Environment;

import com.blankj.utilcode.util.FileUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Nonnull;

import cn.rygel.gd.app.APP;
import cn.rygel.gd.utils.observer.AsyncTransformer;
import cn.rygel.gd.utils.observer.BaseObserver;
import io.reactivex.Observable;

public class BackupUtils {

    public static void backup(BackupCallback callback) {
        Observable.just(copyDbFile())
                .compose(new AsyncTransformer<>())
                .subscribe(new BaseObserver<Boolean>() {
                    @Override
                    public Object getTag() {
                        return null;
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        callback.callback(aBoolean);
                    }
                });
    }

    private static File getBackupLocation() {
        return new File(Environment.getExternalStorageDirectory(), "cn.rygel.gd/backup");
    }

    private static boolean copyDbFile() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.SIMPLIFIED_CHINESE);
        File dbFile = getDbFile();
        Logger.i("db file exists?" + dbFile.exists());
        File destBackupFile = new File(getBackupLocation(), "backup-" + format.format(new Date(System.currentTimeMillis())) + ".mdb");
        Logger.i("dest backup file location : " + destBackupFile);
        boolean success = FileUtils.copyFile(dbFile, destBackupFile);
        Logger.i("copy success?" + success);
        return success;
    }

    /**
     * 获取数据库文件位置
     * @return
     */
    private static File getDbFile() {
        return new File(getAndroidFilesDir(APP.getInstance()), "objectbox/objectbox/data.mdb");
    }

    @Nonnull
    private static File getAndroidFilesDir(Object context) {
        File filesDir;
        try {
            Method getFilesDir = context.getClass().getMethod("getFilesDir");
            filesDir = (File) getFilesDir.invoke(context);
            if (filesDir == null) {
                // Race condition in Android before 4.4: https://issuetracker.google.com/issues/36918154 ?
                System.err.println("getFilesDir() returned null - retrying once...");
                filesDir = (File) getFilesDir.invoke(context);
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not init with given Android context (must be sub class of android.content.Context)", e);
        }
        if (filesDir == null) {
            throw new IllegalStateException("Android files dir is null");
        }
        if (!filesDir.exists()) {
            throw new IllegalStateException("Android files dir does not exist");
        }
        return filesDir;
    }

    public interface BackupCallback {
        void callback(boolean success);
    }

}
