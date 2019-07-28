package cn.rygel.gd.ui.about;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.rygel.gd.R;
import cn.rygel.gd.utils.ClipboardUtils;
import rygel.cn.uilibrary.base.BaseActivity;
import skin.support.content.res.SkinCompatUserThemeManager;

public class AboutActivity extends BaseActivity {

    private static final String EMAIL = "rygel.tang@gmail.com";
    private static final String QQ = "1066340496";

    @BindView(R.id.tv_version)
    TextView mTvVersion;

    @BindView(R.id.tb_about)
    Toolbar mToolbar;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setStatusBarColor(Color.parseColor(SkinCompatUserThemeManager.get().getColorState(R.color.colorPrimary).getColorDefault()));
        mToolbar.setNavigationOnClickListener(l -> finish());
    }

    @Override
    protected void loadData() {
        loadVersionInfo();
    }

    @OnClick(R.id.layout_email)
    protected void onCopyEmail() {
        ClipboardUtils.copy(this,"email",EMAIL);
    }

    @OnClick(R.id.layout_qq)
    protected void onCopyQQ() {
        ClipboardUtils.copy(this,"qq",QQ);
    }

    private void loadVersionInfo() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            mTvVersion.setText(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about;
    }

    public static void start(Context context){
        context.startActivity(new Intent(context,AboutActivity.class));
    }

}
