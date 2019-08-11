package cn.rygel.gd.ui.index.fragment.calculate;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import cn.rygel.gd.R;
import rygel.cn.uilibrary.base.BaseFragment;

public class IntervalFragment extends BaseFragment {

    @BindView(R.id.btn_start_time)
    Button mBtnStart;
    @BindView(R.id.btn_end_time)
    Button mBtnEnd;
    @BindView(R.id.tv_result_string)
    TextView mTvResult;

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_interval;
    }

}
