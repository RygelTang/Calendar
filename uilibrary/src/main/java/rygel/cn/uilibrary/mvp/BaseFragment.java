package rygel.cn.uilibrary.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

public abstract class BaseFragment extends rygel.cn.uilibrary.base.BaseFragment
        implements IView {

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
    public void onDestroy() {
        if(mPresenter != null){
            mPresenter.dropView();
            mPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoading() {
        mLoadingDialog.show();
    }

    @Override
    public void onLoadFinish() {
        mLoadingDialog.dismiss();
    }

    /**
     * 创建中介
     * @return 根据不同的View创建不同的中介
     */
    protected abstract IPresenter createPresenter();

}
