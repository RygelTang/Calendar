package cn.rygel.gd.ui.setting.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.BoxingCrop;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.config.BoxingCropOption;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.utils.BoxingFileHelper;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.rygel.gd.R;
import cn.rygel.gd.bean.WidgetType;
import cn.rygel.gd.utils.CircleDrawable;
import cn.rygel.gd.utils.CustomBoxingCrop;
import cn.rygel.gd.utils.GlideLoader;
import rygel.cn.uilibrary.base.BaseActivity;
import rygel.cn.uilibrary.utils.LeakClearUtils;

public class ImageWidgetSettingActivity extends BaseActivity implements ColorChooserDialog.ColorCallback {

    private static final String WIDGET_TYPE = "WIDGET_TYPE";

    private static final int REQUEST_CODE_CHOOSE = 1;
    private static final int DEFAULT_BITMAP_WIDTH = 1200;
    private static final int DEFAULT_BITMAP_HEIGHT = 800;

    @BindView(R.id.sb_corner_radius)
    SeekBar mSbCornerRadius;

    @BindView(R.id.sb_alpha)
    SeekBar mSbBgAlpha;

    @BindView(R.id.layout_option_blur)
    View mLayoutOptionBlur;

    @BindView(R.id.tv_tips)
    TextView mTvTips;

    @BindView(R.id.img_preview)
    ImageView mImgPreview;

    @BindView(R.id.switch_color_background)
    SwitchButton mSwitchColorBg;

    @BindView(R.id.view_color_text)
    View mTextColorPreview;

    @BindView(R.id.view_color_bg)
    View mBgColorPreview;

    @BindView(R.id.btn_select_bg_color)
    View mBtnSelectBgColor;

    private WidgetType mWidgetType;
    private String mBgPath;
    private int mTextColor = Color.BLACK;

    private int mBgColor = Color.WHITE;

    private Bitmap mBmpBgCache;
    private boolean mShouldBlur = false;

    private boolean mIsChoosingTextColor = false;
    private boolean mIsColorBgOnly = true;

    private RequestListener<Drawable> mRoundCornerTransListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            if (!(resource instanceof BitmapDrawable) || ((BitmapDrawable) resource).getBitmap() == null || target == null) {
                return false;
            }
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), ((BitmapDrawable) resource).getBitmap());
            drawable.setCornerRadius(mSbCornerRadius.getProgress());
            target.onResourceReady(drawable, null);
            return true;
        }
    };

    @OnClick(R.id.img_preview)
    protected void onReselect() {
        if (mIsColorBgOnly) {
            return;
        }
        selectPicture();
    }

    @OnCheckedChanged(R.id.switch_color_background)
    protected void onOptionColorChanged(boolean colorOnly) {
        mIsColorBgOnly = colorOnly;
        if (colorOnly) {
            mBmpBgCache = null;
            mBtnSelectBgColor.setVisibility(View.VISIBLE);
            mLayoutOptionBlur.setVisibility(View.GONE);
            mTvTips.setVisibility(View.GONE);
            drawForeground();
        } else {
            mBtnSelectBgColor.setVisibility(View.GONE);
            mLayoutOptionBlur.setVisibility(View.VISIBLE);
            mTvTips.setVisibility(View.VISIBLE);
            selectPicture();
        }
    }

    @OnCheckedChanged(R.id.switch_blur)
    protected void onOptionBlurChanged(boolean shouldBlur) {
        mShouldBlur = shouldBlur;
        // 清除掉缓存的bmp
        mBmpBgCache = null;
        drawForeground();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mTextColorPreview.setBackground(new CircleDrawable(mTextColor));
        mBgColorPreview.setBackground(new CircleDrawable(mBgColor));
        mSbCornerRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBmpBgCache = null;
                drawForeground();
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
                mBmpBgCache = null;
                drawForeground();
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
        mWidgetType = (WidgetType) getIntent().getSerializableExtra(WIDGET_TYPE);
        drawForeground();
    }

    /**
     * 创建纯色背景图
     * @return
     */
    private Bitmap createColorBmp() {
        Bitmap bitmap = Bitmap.createBitmap(DEFAULT_BITMAP_WIDTH, DEFAULT_BITMAP_HEIGHT, Bitmap.Config.ARGB_4444);
        bitmap.eraseColor(mBgColor);
        return bitmap;
    }

    /**
     * 选择背景图片
     */
    protected void selectPicture() {
        Uri cacheUri = getCacheFileURI();
        if (cacheUri == null) return;
        BoxingCrop.getInstance().init(new CustomBoxingCrop());
        BoxingMediaLoader.getInstance().init(new GlideLoader());
        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG);
        BoxingCropOption option = new BoxingCropOption(cacheUri);
        config.needCamera(R.drawable.ic_camera_black_24dp).withCropOption(option);
        Boxing.of(config).withIntent(this, BoxingActivity.class).start(this, REQUEST_CODE_CHOOSE);
    }

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
     * 具体的还是在{@link ImageWidgetSettingActivity#drawForeground()}中绘制
     * @param media
     */
    private void onImageSelected(BaseMedia media) {
        mBmpBgCache = null;
        mBgPath = media.getPath();
        drawForeground();
    }

    /**
     * 以Bitmap的形式绘制整个小部件
     */
    private void drawForeground() {
        if (mBmpBgCache == null || mBmpBgCache.isRecycled()) {
            if (mIsColorBgOnly) {
                mBmpBgCache = createColorBmp();
            } else {
                mBmpBgCache = ImageUtils.getBitmap(mBgPath);
                if (mBmpBgCache == null) return;
                if (mShouldBlur) {
                    mBmpBgCache = ImageUtils.fastBlur(mBmpBgCache, 1, 25);
                }
            }
            mBmpBgCache = getTransparentBitmap(mBmpBgCache, mSbBgAlpha.getProgress());
        }
        if (mWidgetType != null) {
            Glide.with(mImgPreview)
                    .load(
                            mWidgetType.getDemoWidgetImage(
                                    mBmpBgCache.copy(Bitmap.Config.ARGB_8888, true),
                                    mTextColor
                            )
                    )
                    .apply(new RequestOptions().dontAnimate().placeholder(mImgPreview.getDrawable()))
                    .listener(mRoundCornerTransListener)
                    .into(mImgPreview);
        }
    }

    /**
     * 字体颜色选中回调
     * @param dialog
     * @param selectedColor
     */
    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        if (mIsChoosingTextColor) {
            mTextColor = selectedColor;
            mTextColorPreview.setBackground(new CircleDrawable(selectedColor));
        } else {
            mBgColor = selectedColor;
            // 清除缓存
            mBmpBgCache = null;
            mBgColorPreview.setBackground(new CircleDrawable(selectedColor));
        }
        drawForeground();
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
                .preselect(mTextColor)  // 开始的时候的默认颜色
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
                .preselect(mBgColor)  // 开始的时候的默认颜色
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

    public static void start(Context context, WidgetType type) {
        Intent intent = new Intent(context, ImageWidgetSettingActivity.class);
        intent.putExtra(WIDGET_TYPE, type);
        context.startActivity(intent);
    }

}
