package cn.mt.gd.ui.index.activity;

import com.ftinc.scoop.Scoop;

import cn.mt.gd.R;
import mt.cn.uilibrary.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView() {
        Scoop.getInstance().apply(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

}
