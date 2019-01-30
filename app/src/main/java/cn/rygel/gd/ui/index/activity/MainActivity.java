package cn.rygel.gd.ui.index.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;

import com.ftinc.scoop.Scoop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.rygel.gd.R;
import cn.rygel.gd.bean.OnDrawerStateChangeEvent;
import cn.rygel.gd.dialog.AddUserDialog;
import cn.rygel.gd.service.LocalService;
import cn.rygel.gd.service.RemoteService;
import cn.rygel.gd.service.RestartService;
import cn.rygel.gd.setting.Settings;
import cn.rygel.gd.ui.about.AboutActivity;
import cn.rygel.gd.ui.index.fragment.calendar.impl.CalendarFragment;
import cn.rygel.gd.ui.setting.SettingsActivity;
import rygel.cn.uilibrary.base.BaseActivity;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.dl_main)
    DrawerLayout mDLMain;

    @BindView(R.id.nv_drawer)
    NavigationView mNVMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册EventBus
        EventBus.getDefault().register(this);
        if(Settings.getInstance().isKeepAlive()) {
            startKeepAliveService();
        }
    }

    @Override
    protected void initView() {
        Scoop.getInstance().apply(this);
        ButterKnife.bind(this);
        hideActionBar();
        mNVMain.setNavigationItemSelectedListener(this);
        mNVMain.getChildAt(0).setVerticalScrollBarEnabled(false);
        loadFragment(new CalendarFragment());
    }

    private void loadFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }

    private void startKeepAliveService(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0以上使用JobScheduler保活方案
            startService(new Intent(this, RestartService.class));
        }
        // 5.0以下使用双进程守护保活方案
        startService(new Intent(this, LocalService.class));
        startService(new Intent(this, RemoteService.class));
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_calendar:
                loadFragment(new CalendarFragment());
                break;
            case R.id.action_about:
                AboutActivity.start(this);
            break;
            case R.id.action_create_local_user:
                new AddUserDialog(this).show();
                break;
            case R.id.action_setting:
                SettingsActivity.start(MainActivity.this);
                break;
            default:
                break;
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDrawerStateChanged(OnDrawerStateChangeEvent event) {
        if(event.mState) {
            showDrawer();
        } else {
            hideDrawer();
        }
    }

    private void showDrawer(){
        if(mDLMain.isDrawerOpen(Gravity.START)) {
            return;
        }
        mDLMain.openDrawer(Gravity.START);
    }

    private void hideDrawer(){
        if(mDLMain.isDrawerOpen(Gravity.START)) {
            mDLMain.closeDrawer(Gravity.START);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (mDLMain.isDrawerOpen(Gravity.START)) {
            mDLMain.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }
}
