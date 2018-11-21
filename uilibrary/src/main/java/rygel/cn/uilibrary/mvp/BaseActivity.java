package rygel.cn.uilibrary.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

public abstract class BaseActivity extends rygel.cn.uilibrary.base.BaseActivity
        implements IView {

    /**
     * 中介
     */
    private IPresenter mPresenter = null;

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
    }

    /**
     * 初始化中介
     */
    private void initPresenter(){
        mPresenter = createPresenter();
        if(mPresenter != null){
            mPresenter.bindView(this);
        }
    }

    @Override
    protected void onDestroy() {
        if(mPresenter != null){
            mPresenter.dropView();
            mPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void onLoading() {
        mLoadingDialog.show();
    }

    @Override
    public void onLoadFinish() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    /**
     * 创建中介
     * @return 根据不同的View创建不同的中介
     */
    protected abstract IPresenter createPresenter();
}
