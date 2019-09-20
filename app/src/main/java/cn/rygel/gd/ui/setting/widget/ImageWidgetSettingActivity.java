package cn.rygel.gd.ui.setting.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.BoxingCrop;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingCrop;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.config.BoxingCropOption;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.utils.BoxingFileHelper;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kyleduo.switchbutton.SwitchButton;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.rygel.gd.R;
import cn.rygel.gd.app.APP;
import cn.rygel.gd.bean.ImageWidgetInfo;
import cn.rygel.gd.bean.WidgetType;
import cn.rygel.gd.utils.CircleDrawable;
import cn.rygel.gd.utils.GlideLoader;
import cn.rygel.gd.utils.ImageWidgetUtils;
import me.jessyan.autosize.utils.AutoSizeUtils;
import rygel.cn.uilibrary.base.BaseActivity;
import rygel.cn.uilibrary.utils.LeakClearUtils;

public abstract class ImageWidgetSettingActivity extends BaseActivity implements ColorChooserDialog.ColorCallback {

    private static final int REQUEST_CODE_CHOOSE = 1;
    private static final int DEFAULT_BITMAP_WIDTH = AutoSizeUtils.dp2px(APP.getInstance(), 320);
    private static final int DEFAULT_BITMAP_HEIGHT = AutoSizeUtils.dp2px(APP.getInstance(), 320);

    @BindView(R.id.tb_img_widget)
    protected Toolbar mToolbar;

    @BindView(R.id.sb_corner_radius)
    protected SeekBar mSbCornerRadius;

    @BindView(R.id.sb_alpha)
    protected SeekBar mSbBgAlpha;

    @BindView(R.id.layout_option_blur)
    protected View mLayoutOptionBlur;

    @BindView(R.id.tv_tips)
    protected TextView mTvTips;

    @BindView(R.id.btn_setting)
    protected ImageView mImgSetting;

    @BindView(R.id.img_background)
    protected ImageView mImgBackground;

    @BindView(R.id.img_common_widget)
    protected ImageView mImgWidget;

    @BindView(R.id.switch_color_background)
    protected SwitchButton mSwitchColorBg;

    @BindView(R.id.view_color_text)
    protected View mTextColorPreview;

    @BindView(R.id.view_color_bg)
    protected View mBgColorPreview;

    @BindView(R.id.btn_select_bg_color)
    protected View mBtnSelectBgColor;

    @BindView(R.id.layout_widget_title)
    protected View mDemoWidgetTitle;

    @BindView(R.id.tv_widget_tips)
    protected TextView mDemoTvTips;

    @BindView(R.id.switch_blur)
    protected SwitchButton mSbBlur;

    @BindView(R.id.layout_widget_demo)
    protected View mWidgetDemo;

    protected ImageWidgetInfo mWidgetInfo;

    protected boolean mIsChoosingTextColor = false;

    @OnClick(R.id.img_background)
    protected void onReselect() {
        if (mWidgetInfo.isOnlyColorBg()) {
            return;
        }
        selectPicture();
    }

    @OnCheckedChanged(R.id.switch_color_background)
    protected void onOptionColorChanged(boolean colorOnly) {
        mWidgetInfo.setOnlyColorBg(colorOnly);
        if (colorOnly) {
            mBtnSelectBgColor.setVisibility(View.VISIBLE);
            mLayoutOptionBlur.setVisibility(View.GONE);
            mTvTips.setVisibility(View.GONE);
            invalidate();
        } else {
            mBtnSelectBgColor.setVisibility(View.GONE);
            mLayoutOptionBlur.setVisibility(View.VISIBLE);
            mTvTips.setVisibility(View.VISIBLE);
            selectPicture();
        }
    }

    @OnCheckedChanged({R.id.switch_widget_title})
    protected void onHideTitleChanged(boolean shouldHide) {
        mWidgetInfo.setHideTitle(shouldHide);
        invalidate();
    }

    @OnCheckedChanged(R.id.switch_blur)
    protected void onOptionBlurChanged(boolean shouldBlur) {
        mWidgetInfo.setShouldBlur(shouldBlur);
        // 清除掉缓存的bmp
        invalidate();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.menu_add_widget);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_save:
                        Intent resultValue = new Intent();
                        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetInfo.getWidgetId());
                        setResult(RESULT_OK, resultValue);
                        Toast.makeText(ImageWidgetSettingActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
                        finish();
                        mWidgetInfo.save();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        mTextColorPreview.setBackground(new CircleDrawable(Color.BLACK));
        mBgColorPreview.setBackground(new CircleDrawable(Color.WHITE));
        mSbCornerRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mWidgetInfo.setCornerRadius(progress);
                invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSbBgAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mWidgetInfo.setBgAlpha(progress);
                invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void loadData() {
        setResult(RESULT_CANCELED);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            final int widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            mWidgetInfo = ImageWidgetInfo.getById(getWidgetType(), widgetId);
            if (mWidgetInfo == null) {
                mWidgetInfo = new ImageWidgetInfo(
                        getWidgetType(),
                        widgetId,
                        Color.BLACK,
                        Color.WHITE,
                        18,
                        100,
                        "",
                        DEFAULT_BITMAP_WIDTH,
                        DEFAULT_BITMAP_HEIGHT,
                        true,
                        false,
                        false
                );
            }
            mTextColorPreview.setBackground(new CircleDrawable(mWidgetInfo.getTextColor()));
            mSwitchColorBg.setCheckedNoEvent(mWidgetInfo.isOnlyColorBg());
            mSbBgAlpha.setProgress(mWidgetInfo.getBgAlpha());
            mSbCornerRadius.setProgress(mWidgetInfo.getCornerRadius());
            mSbBlur.setCheckedNoEvent(mWidgetInfo.isShouldBlur());
            mBgColorPreview.setBackground(new CircleDrawable(mWidgetInfo.getBgColor()));
            invalidate();
            return;
        }
        Logger.e("could not found widget id to set!");
        Toast.makeText(this, R.string.widget_id_not_found, Toast.LENGTH_SHORT).show();
        finish();
    }

    protected abstract WidgetType getWidgetType();

    /**
     * 选择背景图片
     */
    protected void selectPicture() {
        Uri cacheUri = getCacheFileURI();
        if (cacheUri == null) return;
        BoxingCrop.getInstance().init(getCropper());
        BoxingMediaLoader.getInstance().init(new GlideLoader());
        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG);
        BoxingCropOption option = new BoxingCropOption(cacheUri);
        config.needCamera(R.drawable.ic_camera_black_24dp).withCropOption(option);
        Boxing.of(config).withIntent(this, BoxingActivity.class).start(this, REQUEST_CODE_CHOOSE);
    }

    protected abstract IBoxingCrop getCropper();

    /**
     * 获取缓存路径
     * @return
     */
    private Uri getCacheFileURI() {
        String cachePath = BoxingFileHelper.getCacheDir(this);
        if (TextUtils.isEmpty(cachePath)) {
            Toast.makeText(getApplicationContext(), R.string.boxing_storage_deny, Toast.LENGTH_SHORT).show();
            return null;
        }
        return new Uri.Builder()
                .scheme("file")
                .appendPath(cachePath)
                .appendPath(String.format(Locale.US, "%s.png", System.currentTimeMillis()))
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_widget_setting;
    }

    /**
     * 背景图片选中的回调，这里只是取出数据
     * @param media
     */
    private void onImageSelected(BaseMedia media) {
        mWidgetInfo.setBgPath(media.getPath());
        int[] size = ImageUtils.getSize(media.getPath());
        mWidgetInfo.setBgHeight(size[1]);
        mWidgetInfo.setBgWidth(size[0]);
        invalidate();
    }

    protected void invalidate() {
        mDemoTvTips.setTextColor(mWidgetInfo.getTextColor());
        mDemoWidgetTitle.setVisibility(mWidgetInfo.isHideTitle() ? View.GONE : View.VISIBLE);
        Drawable setting;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setting = getResources().getDrawable(R.drawable.ic_settings_white_24dp, getTheme());
        } else {
            setting = getResources().getDrawable(R.drawable.ic_settings_white_24dp);
        }
        DrawableCompat.setTint(setting, mWidgetInfo.getTextColor());
        setting.setAlpha(200);
        mImgSetting.setImageDrawable(setting);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mWidgetInfo.getBgWidth(), mWidgetInfo.getBgHeight());
        params.gravity = Gravity.CENTER;
        mWidgetDemo.setLayoutParams(params);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(mWidgetInfo.getBgHeight() / 12, mWidgetInfo.getBgHeight() / 12);
        imgParams.gravity = Gravity.CENTER;
        imgParams.setMargins(mWidgetInfo.getBgHeight() / 20,mWidgetInfo.getBgHeight() / 30,mWidgetInfo.getBgHeight() / 20,mWidgetInfo.getBgHeight() / 40);
        mImgSetting.setLayoutParams(imgParams);
        mDemoTvTips.setTextSize(mWidgetInfo.getBgHeight() / 63F);
        ThreadUtils.getSinglePool().execute(new Runnable() {
            @Override
            public void run() {
                final Bitmap bg = ImageWidgetUtils.getBackgroundOf(mWidgetInfo);
                final Bitmap fg = ImageWidgetUtils.getForegroundOf(mWidgetInfo);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(mImgBackground)
                                .asBitmap()
                                .load(bg)
                                .apply(new RequestOptions().dontAnimate().placeholder(mImgBackground.getDrawable()))
                                .into(mImgBackground);
                        Glide.with(mImgWidget)
                                .load(fg)
                                .apply(new RequestOptions().dontAnimate().placeholder(mImgWidget.getDrawable()))
                                .into(mImgWidget);
                    }
                });
            }
        });
    }

    /**
     * 字体颜色选中回调
     * @param dialog
     * @param selectedColor
     */
    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        if (mIsChoosingTextColor) {
            mWidgetInfo.setTextColor(selectedColor);
            mTextColorPreview.setBackground(new CircleDrawable(selectedColor));
        } else {
            mWidgetInfo.setBgColor(selectedColor);
            mBgColorPreview.setBackground(new CircleDrawable(selectedColor));
        }
        invalidate();
    }

    /**
     * 关闭颜色选择对话框回调，这里主要是因为他内部可能会有内存泄漏，通过修复工具进行修复
     * @param dialog
     */
    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {
        LeakClearUtils.fixColorPickerLeak(dialog);
    }

    /**
     * 打开颜色选择对话框
     */
    @OnClick(R.id.btn_select_text_color)
    protected void selectTextColor() {
        mIsChoosingTextColor = true;
        new ColorChooserDialog.Builder(this, R.string.choose_color)
                .titleSub(R.string.choose_color)
                .doneButton(R.string.select)  // changes label of the done button
                .cancelButton(R.string.cancel)  // changes label of the cancel button
                .preselect(mWidgetInfo.getTextColor())  // 开始的时候的默认颜色
                .dynamicButtonColor(true)  // defaults to true, false will disable changing action buttons' color to currently selected color
                .build()
                .show(getSupportFragmentManager());
    }

    /**
     * 打开颜色选择对话框
     */
    @OnClick(R.id.btn_select_bg_color)
    protected void selectBgColor() {
        mIsChoosingTextColor = false;
        new ColorChooserDialog.Builder(this, R.string.choose_color)
                .titleSub(R.string.choose_color)
                .doneButton(R.string.select)  // changes label of the done button
                .cancelButton(R.string.cancel)  // changes label of the cancel button
                .preselect(mWidgetInfo.getBgColor())  // 开始的时候的默认颜色
                .dynamicButtonColor(true)  // defaults to true, false will disable changing action buttons' color to currently selected color
                .build()
                .show(getSupportFragmentManager());
    }

    /**
     * 当取消选择时，将回退到纯色背景
     */
    private void onCanceledSelect() {
        mSwitchColorBg.setChecked(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE:
                // 解析图片选择结果
                List<BaseMedia> medias = Boxing.getResult(data);
                if (medias == null || medias.size() == 0) {
                    onCanceledSelect();
                    break;
                }
                onImageSelected(medias.get(0));
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 修改图片透明度
     * @param sourceImg
     * @param alpha
     * @return
     */
    public static Bitmap getTransparentBitmap(Bitmap sourceImg, int alpha){
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg
                .getWidth(), sourceImg.getHeight());// 获得图片的ARGB值
        alpha = alpha * 255 / 100;
        for (int i = 0; i < argb.length; i++) {
            argb[i] = (alpha << 24) | (argb[i] & 0x00FFFFFF);
        }
        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg
                .getHeight(), Bitmap.Config.ARGB_8888);
        return sourceImg;
    }
}
