package cn.rygel.gd.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.rygel.gd.ui.index.fragment.calculate.IntervalFragment;
import cn.rygel.gd.ui.index.fragment.calculate.TraceFragment;
import cn.rygel.gd.ui.index.fragment.calculate.TransformFragment;

public class CalculateFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments = new ArrayList<>();

    public CalculateFragmentAdapter(FragmentManager fm) {
        super(fm);
        generateFragmentList();
    }

    private void generateFragmentList() {
        mFragments.add(new IntervalFragment());
        mFragments.add(new TraceFragment());
        mFragments.add(new TransformFragment());
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

}
