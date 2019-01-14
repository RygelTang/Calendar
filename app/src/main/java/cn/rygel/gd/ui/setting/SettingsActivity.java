package cn.rygel.gd.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.rygel.gd.R;
import rygel.cn.uilibrary.mvp.BaseActivity;
import rygel.cn.uilibrary.utils.UIUtils;

public class SettingsActivity extends BaseActivity<SettingPresenter> implements ISettingView {

    MaterialDialog mWeekDaySelector = null;

    List<String> mWeekDays = null;

    @BindView(R.id.tb_setting)
    Toolbar mTbSetting;

    @BindView(R.id.switch_keep_alive)
    SwitchButton mSwitchKeepAlive;

    @BindView(R.id.tv_summary_weekday)
    TextView mTvWeekDay;

    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        initWeekDaySelector();
        mTbSetting.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initWeekDaySelector() {
        mWeekDays =  Arrays.asList(UIUtils.getStringArray(this,R.array.weekdays));
        BaseQuickAdapter<String,BaseViewHolder> adapter = new BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_weekday, mWeekDays) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_weekday,item);
            }

        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onWeekItemSelect(position);
            }
        });
        mWeekDaySelector = new MaterialDialog.Builder(this)
                .adapter(adapter,new LinearLayoutManager(this))
                .build();
    }

    @Override
    protected void loadData() {
        mSwitchKeepAlive.setChecked(getPresenter().isKeepAlive());
        mTvWeekDay.setText(mWeekDays.get(getPresenter().getWeekdayOffset()));
    }

    @OnCheckedChanged(R.id.switch_keep_alive)
    protected void onKeepAliveChanged(boolean state){
        if(!getPresenter().putKeepAlive(state)) {
            showToast(R.string.save_fail);
        }
    }

    @OnClick(R.id.btn_keep_alive)
    protected void onClickKeepAlive() {
        mSwitchKeepAlive.setChecked(!mSwitchKeepAlive.isChecked());
    }

    @OnClick(R.id.btn_about)
    protected void onClickAbout() {
        // TODO: 2019/1/14 跳转到关于界面
    }

    @OnClick(R.id.btn_select_first_weekday)
    protected void onClickSelectFirstWeekday() {
        mWeekDaySelector.show();
    }

    protected void onWeekItemSelect(int index) {
        if(mWeekDaySelector != null && mWeekDaySelector.isShowing()) {
            mWeekDaySelector.dismiss();
        }
        if(!getPresenter().putWeekDayOffset(index)) {
            showToast(R.string.save_fail);
            return;
        }
        mTvWeekDay.setText(mWeekDays.get(index));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @Override
    public void refresh() {

    }

    public static void start(Context context) {
        Intent intent = new Intent(context,SettingsActivity.class);
        context.startActivity(intent);
    }

}
