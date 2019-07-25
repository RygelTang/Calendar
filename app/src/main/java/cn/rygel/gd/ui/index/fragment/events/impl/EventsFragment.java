package cn.rygel.gd.ui.index.fragment.events.impl;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.rygel.gd.R;
import cn.rygel.gd.bean.OnDrawerStateChangeEvent;
import cn.rygel.gd.ui.index.fragment.events.IEventsView;
import cn.rygel.gd.widget.adapter.EventPagerAdapter;
import rygel.cn.uilibrary.mvp.BaseFragment;

public class EventsFragment extends BaseFragment<EventsPresenter> implements IEventsView {

    @BindView(R.id.tb_event)
    Toolbar mToolbar;
    @BindView(R.id.indicator)
    MagicIndicator mIndicator;
    @BindView(R.id.vpEvent)
    ViewPager mVpEventPager;

    private String[] mTitles = {
            "7天内", "本月", "全部"
    };

    @Override
    protected EventsPresenter createPresenter() {
        return new EventsPresenter();
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new OnDrawerStateChangeEvent(true));
            }
        });
    }

    private void initViewPager(List<BaseQuickAdapter> adapters) {
        mVpEventPager.setAdapter(new EventPagerAdapter(getContext(), adapters));
    }

    @Override
    public void onAdapterGenerated(List<BaseQuickAdapter> adapters) {
        initViewPager(adapters);
        initIndicator();
    }

    /**
     * 初始化indicator
     */
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
                colorTransitionPagerTitleView.setSelectedColor(ColorUtils.getColor(R.color.colorPrimary));
                colorTransitionPagerTitleView.setText(mTitles[index]);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mVpEventPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(ColorUtils.getColor(R.color.colorPrimary));
                indicator.setLineHeight(5F);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        mIndicator.setNavigator(navigator);
        ViewPagerHelper.bind(mIndicator, mVpEventPager);
    }

    @Override
    protected void loadData() {
        getPresenter().generateEventListAdapter(StringUtils.getString(R.string.default_user));
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_events;
    }

    @Override
    public void refresh() {
        Logger.e("this fragment do not support refresh option!");
    }
}
