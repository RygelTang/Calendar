package rygel.cn.uilibrary.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleProvider;

public abstract class BaseFragment extends rygel.cn.uilibrary.base.BaseFragment
        implements IView {

    private static final String TAG = "BaseFragment";

    /**
     * 中介
     */
    private IPresenter mPresenter = null;

    protected MaterialDialog mLoadingDialog = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initPresenter();
        initLoadingDialog();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 初始化加载中对话框
     */
    private void initLoadingDialog() {
        mLoadingDialog = new MaterialDialog.Builder(getContext())
                .progress(true,0)
                .cancelable(false)
                .build();
        Logger.i(TAG,"create loading dialog success?" + (mLoadingDialog == null));
    }

    /**
     * 初始化中介
     */
    private void initPresenter(){
        mPresenter = createPresenter();
        Logger.i(TAG,"create presenter success? " + (mPresenter == null));
        if(mPresenter != null){
            Logger.i(TAG,"presenter bind view.");
            mPresenter.bindView(this);
        }
    }

    @Override
    public void onDestroy() {
        if(mPresenter != null){
            Logger.i(TAG,"presenter drop view.");
            mPresenter.dropView();
            mPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void showToast(String str) {
        Logger.i(TAG,"show toast -------------------------> \n\t" + str);
        Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoading() {
        Logger.i(TAG,"onLoading");
        mLoadingDialog.show();
    }

    @Override
    public void onLoadFinish() {
        Logger.i(TAG,"onLoadFinish");
        mLoadingDialog.dismiss();
    }

    @Override
    public LifecycleProvider getLifecycleProvider() {
        return this;
    }

    /**
     * 创建中介
     * @return 根据不同的View创建不同的中介
     */
    protected abstract IPresenter createPresenter();

}
