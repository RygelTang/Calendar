package rygel.cn.uilibrary.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxFragment;

public abstract class BaseFragment extends RxFragment {

    private static final String TAG = "BaseFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.i(TAG,"onCreateView");
        final int layoutRes = getLayoutResID();
        Logger.i(TAG,"getLayoutResID : " + layoutRes);
        View layout = inflater.inflate(getLayoutResID(),container,false);
        Logger.i(TAG,"initView");
        initView(layout);
        Logger.i(TAG,"loadData");
        loadData();
        return layout;
    }

    /**
     * 初始化View
     * @param view layout
     */
    protected abstract void initView(View view);

    /**
     * 加载数据
     */
    protected abstract void loadData();

    /**
     * 获取Fragment的布局文件的id
     * @return 布局文件的id
     */
    protected abstract @LayoutRes int getLayoutResID();

}
