package cn.rygel.gd.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventPagerAdapter extends PagerAdapter {

    private List<View> mViews = new ArrayList<>();

    public EventPagerAdapter(Context context, List<EventListAdapter> adapters) {
        for (BaseQuickAdapter adapter : adapters) {
            RecyclerView rvEvents = new RecyclerView(context);
            rvEvents.setLayoutManager(new LinearLayoutManager(context));
            rvEvents.setAdapter(adapter);
            mViews.add(rvEvents);
        }
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViews.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (mViews.get(position).getParent() != null) {
            ((ViewGroup) mViews.get(position).getParent()).removeView(mViews.get(position));
        }
        container.addView(mViews.get(position), 0);
        return mViews.get(position);
    }

}
