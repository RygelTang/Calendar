package rygel.cn.uilibrary.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleProvider;

public abstract class BaseFragment<P extends IPresenter> extends rygel.cn.uilibrary.base.BaseFragment
        implements IView {

    /**
     * 中介
     */
    protected P mPresenter = null;

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
        Logger.i("create loading dialog success?" + (mLoadingDialog == null));
    }

    /**
     * 初始化中介
     */
    private void initPresenter(){
        mPresenter = createPresenter();
        Logger.i("create presenter success? " + (mPresenter == null));
        if(mPresenter != null){
            Logger.i("presenter bind view.");
            mPresenter.bindView(this);
        }
    }

    /**
     * 获取中介
     * @return
     */
    protected P getPresenter(){
        return mPresenter;
    }

    @Override
    public void onDestroy() {
        if(mPresenter != null){
            Logger.i("presenter drop view.");
            mPresenter.dropView();
            mPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void showToast(String str) {
        Logger.i("show toast -------------------------> \n\t" + str);
        Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(@StringRes int strRes) {
        Logger.i("show string res toast -------------------------> \n\t" + strRes);
        Toast.makeText(getContext(),strRes,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoading() {
        Logger.i("onLoading");
        mLoadingDialog.show();
    }

    @Override
    public void onLoadFinish() {
        Logger.i("onLoadFinish");
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
    protected abstract P createPresenter();

}
