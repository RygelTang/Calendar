package cn.rygel.gd.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import cn.rygel.gd.R;
import cn.rygel.gd.db.model.EventModel;
import cn.rygel.gd.db.model.UserModel;
import cn.rygel.gd.utils.observer.AsyncTransformer;
import cn.rygel.gd.utils.observer.BaseObserver;
import io.reactivex.Observable;

public class AddUserDialog {

    private Context mContext = null;

    private MaterialDialog mDialog = null;

    private MaterialDialog mLoadingDialog = null;

    private String mUserName = null;

    public AddUserDialog(@NonNull Context context) {
        mContext = context;
    }

    public void show() {
        if(mDialog == null) {
            mDialog = new MaterialDialog.Builder(mContext)
                    .title(R.string.create_local_user)
                    .input(R.string.create_local_user, R.string.default_user, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            if(input != null) {
                                mUserName = input.toString();
                            }
                        }
                    })
                    .positiveText(R.string.save)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if(mLoadingDialog == null) {
                                mLoadingDialog = new MaterialDialog.Builder(mContext)
                                        .content(R.string.saving)
                                        .cancelable(false)
                                        .progress(true,0)
                                        .build();
                            }
                            mLoadingDialog.show();
                            EditText text = mDialog.getInputEditText();
                            if(text != null && text.getText() != null) {
                                mUserName = text.getText().toString();
                            } else {
                                return;
                            }
                            Observable
                                    .just(UserModel
                                            .getInstance()
                                            .putUser(mUserName)
                                    )
                                    .compose(new AsyncTransformer())
                                    .subscribe(new BaseObserver<Boolean>() {
                                        @Override
                                        public Object getTag() {
                                            return null;
                                        }

                                        @Override
                                        public void onSuccess(Boolean aBoolean) {
                                            if(aBoolean) {
                                                onSaveSuccess();
                                            } else {
                                                onSaveFail();
                                            }
                                        }
                                    });
                        }
                    })
                    .build();
        }
        mDialog.show();
    }

    private void onSaveSuccess() {
        dismiss();
        Toast.makeText(mContext,R.string.save_success,Toast.LENGTH_SHORT).show();
    }

    private void onSaveFail() {
        mLoadingDialog.dismiss();
        Toast.makeText(mContext,R.string.save_fail,Toast.LENGTH_SHORT).show();
    }

    public void dismiss() {
        mLoadingDialog.dismiss();
        mDialog.dismiss();
    }
}
