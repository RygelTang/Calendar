package cn.rygel.gd.ui.about;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.rygel.gd.R;
import cn.rygel.gd.app.APP;
import cn.rygel.gd.utils.ClipboardUtils;
import rygel.cn.uilibrary.base.BaseActivity;
import skin.support.content.res.SkinCompatUserThemeManager;

public class AboutActivity extends BaseActivity {

    private static final String GROUP_KEY = "JMCMAYQbWXA_cVmnpIHScYKbxhMCm-Yi";
    private static final String EMAIL = "rygel.tang@gmail.com";
    private static final String QQ = "1066340496";

    @BindView(R.id.tv_version)
    TextView mTvVersion;

    @BindView(R.id.tb_about)
    Toolbar mToolbar;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mToolbar.setNavigationOnClickListener(l -> finish());
    }

    @Override
    protected void onResume() {
        setStatusBarColor(Color.parseColor(SkinCompatUserThemeManager.get().getColorState(R.color.colorPrimaryDark).getColorDefault()));
        super.onResume();
    }

    @Override
    protected void loadData() {
        loadVersionInfo();
    }

    @OnClick(R.id.layout_star)
    protected void onGotoStar() {
        Uri uri = Uri.parse("market://details?id=" + APP.getInstance().getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.coolapk.market");
        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_ALL) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.not_found_store, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.layout_email)
    protected void onCopyEmail() {
        Toast.makeText(this, R.string.copy_success, Toast.LENGTH_SHORT).show();
        ClipboardUtils.copy(this,"email",EMAIL);
    }

    @OnClick(R.id.layout_qq)
    protected void onCopyQQ() {
        Toast.makeText(this, R.string.copy_success, Toast.LENGTH_SHORT).show();
        ClipboardUtils.copy(this,"qq",QQ);
    }

    @OnClick(R.id.layout_qq_group)
    protected void onAddGroup() {
        if (!joinQQGroup(this)) {
            Toast.makeText(this, R.string.not_found_qq, Toast.LENGTH_SHORT).show();
        }
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

    /****************
     *
     * 发起添加群流程。群号：日历用户群(228434421)
     * 调用 joinQQGroup(JMCMAYQbWXA_cVmnpIHScYKbxhMCm-Yi) 即可发起手Q客户端申请加群 日历用户群(228434421)
     *
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    private static boolean joinQQGroup(Context context) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + GROUP_KEY));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public static void start(Context context){
        context.startActivity(new Intent(context,AboutActivity.class));
    }

}
