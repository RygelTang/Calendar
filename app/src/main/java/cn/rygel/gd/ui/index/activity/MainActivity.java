package cn.rygel.gd.ui.index.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ftinc.scoop.Scoop;

import butterknife.ButterKnife;
import cn.rygel.gd.R;
import cn.rygel.gd.ui.index.fragment.calendar.impl.CalendarFragment;
import rygel.cn.uilibrary.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void initView() {
        Scoop.getInstance().apply(this);
        hideActionBar();
        loadFragment(new CalendarFragment());
    }

    private void loadFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

}
