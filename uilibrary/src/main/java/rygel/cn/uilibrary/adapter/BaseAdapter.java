package rygel.cn.uilibrary.adapter;

import android.content.Context;
import androidx.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends BaseQuickAdapter<T,BaseViewHolder> {

    private Context mContext = null;
    private List<T> mDatas = new ArrayList<>();

    public BaseAdapter(Context context,@LayoutRes int layoutId, List<T> data){
        super(layoutId,data);
        mContext = context;
        mDatas.addAll(data);
    }

    public void add(T item){
        mDatas.add(item);
        notifyItemInserted(mDatas.size() - 1);
    }

    public void add(T item,int index){
        mDatas.add(index,item);
        notifyItemInserted(index);
    }

    public void addAll(List<T> data){
        int start = mDatas.size() - 1;
        mDatas.addAll(data);
        notifyItemRangeInserted(start,data.size());
    }

    public void replace(int index,T item){
        mDatas.set(index,item);
        notifyItemChanged(index);
    }

    public void remove(int index){
        mDatas.remove(index);
        notifyItemRemoved(index);
    }

    public void removeRange(int startIndex,int count){
        mDatas.subList(startIndex,startIndex + count).clear();
        notifyItemRangeRemoved(startIndex,count);
    }
}
