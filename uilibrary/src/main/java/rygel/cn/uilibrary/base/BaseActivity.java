package rygel.cn.uilibrary.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import rygel.cn.uilibrary.utils.UIUtils;

public abstract class BaseActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        initView();
        loadData();
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }

    protected void showSoftKeyBoard(){

    }

    protected void hideSoftKeyBoard(){

    }

    protected void setStatusBarVisible(boolean visible){

    }

    protected void setNavigationBarVisible(boolean visible){

    }

    protected void setNavigationBarColor(){

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void setStatusBarColor(int color){
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
