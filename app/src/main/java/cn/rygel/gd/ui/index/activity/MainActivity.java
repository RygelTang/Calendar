package cn.rygel.gd.ui.index.activity;

import com.ftinc.scoop.Scoop;

import cn.rygel.gd.R;
import rygel.cn.uilibrary.base.BaseActivity;

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
