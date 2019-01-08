package cn.rygel.gd.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cncoderx.wheelview.OnWheelChangedListener;
import com.cncoderx.wheelview.Wheel3DView;
import com.cncoderx.wheelview.WheelView;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.rygel.gd.R;
import rygel.cn.uilibrary.utils.UIUtils;

public class TimePicker {

    @BindView(R.id.lv_hour)
    Wheel3DView mHourPicker;

    @BindView(R.id.lv_minute)
    Wheel3DView mMinutePicker;

    @BindView(R.id.btn_ok)
    Button mBtnOK;

    private int mHour = 0;
    private int mMinute = 0;

    private View mContent = null;
    private Context mContext = null;

    private OnTimeSelectListener mTimeSelectListener = null;

    private MaterialDialog mDialog = null;

    public TimePicker(Context context) {
        mContext = context;
        init();
    }

    private void init(){
        initPickers();
        initDialog();
    }

    private void initDialog(){
        mDialog = new MaterialDialog.Builder(mContext)
                .customView(mContent,false)
                .build();
    }

    private void initPickers(){
        mContent = LayoutInflater.from(mContext).inflate(R.layout.layout_time_picker,null);
        ButterKnife.bind(this,mContent);
        mHourPicker.setOnWheelChangedListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView view, int oldIndex, int newIndex) {
                mHour = newIndex;
                Logger.i("on hour select : " + newIndex);
            }
        });
        mMinutePicker.setOnWheelChangedListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView view, int oldIndex, int newIndex) {
                mMinute = newIndex;
                Logger.i("on minute select : " + newIndex);
            }
        });
    }

    @OnClick(R.id.btn_ok)
    protected void onTimeSelected(){
        mDialog.dismiss();
        if(mTimeSelectListener != null) {
            mTimeSelectListener.onTimeSelect(mHour,mMinute);
            Logger.i("on time select : " +
                    UIUtils.getStringArray(mContext,R.array.hours)[mHour] +
                    UIUtils.getString(mContext,R.string.time_separator) +
                    UIUtils.getStringArray(mContext,R.array.minutes)[mMinute]
            );
            return;
        }
        Logger.i("onTimeSelectListener is null,please check has called setTimeSelectListener");
    }

    public void show(){
        if(mDialog != null) {
            mDialog.show();
        }
    }

    public void dismiss(){
        if(mDialog != null) {
            mDialog.dismiss();
        }
    }

    public MaterialDialog getDialog() {
        return mDialog;
    }

    public void setTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        mTimeSelectListener = timeSelectListener;
    }

    public interface OnTimeSelectListener {
        void onTimeSelect(int hour,int minute);
    }

}
