package rygel.cn.uilibrary.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import rygel.cn.uilibrary.utils.LeakClearUtils;
import rygel.cn.uilibrary.utils.UIUtils;

public abstract class BaseActivity extends RxAppCompatActivity {

    private boolean mIsFirstLoad = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("onCreate");
        final int layoutRes = getLayoutResId();
        Logger.i("getLayoutResId : " + layoutRes);
        setContentView(getLayoutResId());
        Logger.i("initView");
        initView();
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsFirstLoad) {
            Logger.i("loadData");
            loadData();
            mIsFirstLoad = false;
        }
    }

    protected void hideActionBar(){
        Logger.i("hideActionBar");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        LeakClearUtils.fixInputMethodManagerLeak(this);
        Logger.i("onDestroy");
        super.onDestroy();
    }

    protected void setStatusBarVisible(boolean visible){
        Logger.i("set status bar visible : " + visible);
        if(!visible)
            UIUtils.hideStatusBar(this);
        else
            UIUtils.showStatusBar(this);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void setStatusBarColor(int color){
        Logger.i("set status bar color : " + Integer.toHexString(color));
        UIUtils.setStatusBarColor(this,color);
    }

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 加载数据
     */
    protected abstract void loadData();

    /**
     * 获取布局ID
     * 返回Activity的布局文件ID即可
     *
     * @return 布局文件id
     */
    protected abstract @LayoutRes int getLayoutResId();

}
