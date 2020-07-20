package cn.rygel.gd.ui.index.fragment.calculate;

import android.content.Context;
import android.graphics.Color;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.rygel.gd.R;
import cn.rygel.gd.adapter.CalculateFragmentAdapter;
import cn.rygel.gd.bean.OnDrawerStateChangeEvent;
import rygel.cn.uilibrary.base.BaseFragment;
import skin.support.content.res.SkinCompatUserThemeManager;

public class CalculateFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.vp_fragment)
    ViewPager mVpFragment;
    @BindView(R.id.indicator)
    MagicIndicator mIndicator;

    private static String[] mTitles = {
            "日期间隔计算", "日期推算", "公农历转换"
    };

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new OnDrawerStateChangeEvent(true));
            }
        });
        mVpFragment.setAdapter(new CalculateFragmentAdapter(getChildFragmentManager()));
        initIndicator();
    }

    private void initIndicator() {
        CommonNavigator navigator = new CommonNavigator(getContext());
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.parseColor(SkinCompatUserThemeManager.get().getColorState(R.color.colorPrimary).getColorDefault()));
                colorTransitionPagerTitleView.setText(mTitles[index]);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mVpFragment.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.parseColor(SkinCompatUserThemeManager.get().getColorState(R.color.colorPrimary).getColorDefault()));
                indicator.setLineHeight(5F);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        mIndicator.setNavigator(navigator);
        ViewPagerHelper.bind(mIndicator, mVpFragment);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_calculate;
    }

}
