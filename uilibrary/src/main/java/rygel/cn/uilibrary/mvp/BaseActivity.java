package rygel.cn.uilibrary.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleProvider;

public abstract class BaseActivity<P extends IPresenter> extends rygel.cn.uilibrary.base.BaseActivity
        implements IView {

    /**
     * 中介
     */
    protected P mPresenter = null;

    protected MaterialDialog mLoadingDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initPresenter();
        initLoadingDialog();
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化加载中对话框
     */
    private void initLoadingDialog() {
        mLoadingDialog = new MaterialDialog.Builder(this)
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
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onDestroy() {
        if(mPresenter != null){
            Logger.i("presenter drop view.");
            mPresenter.dropView();
            mPresenter = null;
        }
        super.onDestroy();
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
    public void showToast(String str) {
        Logger.i("show toast -------------------------> \n\t" + str);
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(@StringRes int strRes) {
        Logger.i("show res toast -------------------------> \n\t" + strRes);
        Toast.makeText(this,strRes,Toast.LENGTH_SHORT).show();
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
