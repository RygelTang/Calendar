package mt.cn.uilibrary.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import mt.cn.uilibrary.dialog.LoadingDialog;

public abstract class BaseFragment extends mt.cn.uilibrary.base.BaseFragment
        implements IView {

    /**
     * 中介
     */
    private IPresenter mPresenter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initPresenter();
        return super.onCreateView(inflater, container, savedInstanceState);
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
        LoadingDialog.show();
    }

    @Override
    public void onLoadFinish() {
        LoadingDialog.dismiss();
    }

    /**
     * 创建中介
     * @return 根据不同的View创建不同的中介
     */
    protected abstract IPresenter createPresenter();

}
