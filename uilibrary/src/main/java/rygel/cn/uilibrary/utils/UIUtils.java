package rygel.cn.uilibrary.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import rygel.cn.uilibrary.R;

public class UIUtils {

    private static final String TAG = "UIUtils";
    private static final String TAG_FAKE_STATUS_BAR_VIEW = "TAG_FAKE_STATUS_BAR_VIEW";
    private static final String TAG_MARGIN_ADDED = "TAG_MARGIN_ADDED";
    private static final String TAG_MARGIN_REMOVED = "TAG_MARGIN_REMOVED";

    /**
     * dip转换px
     */
    public static int dip2px(@NonNull Context context,int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * pxz转换dip
     */
    public static int px2dip(@NonNull Context context,int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static View inflate(@NonNull Context context,int resId) {
        return LayoutInflater.from(context).inflate(resId, null);
    }

    /**
     * 获取资源
     */
    public static Resources getResources(@NonNull Context context) {
        return context.getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(@NonNull Context context,int resId) {
        return getResources(context).getString(resId);
    }

    /**
     * 获取文字
     */
    public static String getString(@NonNull Context context,int resId,Object... formatArgs) {
        return getResources(context).getString(resId,formatArgs);
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(@NonNull Context context,int resId) {
        return getResources(context).getStringArray(resId);
    }

    /**
     * 获取dimen
     */
    public static int getDimens(@NonNull Context context,int resId) {
        return getResources(context).getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     */
    public static Drawable getDrawable(@NonNull Context context,int resId) {
        return getResources(context).getDrawable(resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(@NonNull Context context,int resId) {
        return getResources(context).getColor(resId);
    }

    /**
     * 获取颜色选择器
     */
    public static ColorStateList getColorStateList(@NonNull Context context,int resId) {
        return getResources(context).getColorStateList(resId);
    }

    /**
     * 得到屏幕的高度
     *
     * @param activity
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getScreenHeight(@NonNull Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight();
    }

    /**
     * 得到屏幕的宽度
     *
     * @param activity
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(@NonNull Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getWidth();
    }

    /**
     * 得到一个控件相对于屏幕左侧的位置
     *
     * @param view
     * @return
     */
    public static int getLeftOnScreen(@NonNull View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[0];

    }

    /**
     * 得到一个控件相对于屏幕左侧的位置
     *
     * @param view
     * @return
     */
    public static int getRightOnScreen(@NonNull View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[0];

    }

    /**
     * 得到一个控件相对于屏幕顶部的位置
     *
     * @param view
     * @return
     */
    public static int getTopOnScreen(@NonNull View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1];

    }

    /**
     * 获得状态栏的高度
     *
     * @return
     */
    public static int getStatusHeight(@NonNull Context context) {
        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = getResources(context).getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 为Activity隐藏状态栏
     * @param activity
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void hideStatusBar(@NonNull Activity activity){
        Window window = activity.getWindow();
        window.addFlags(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * 为Activity隐藏状态栏
     * @param activity
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void showStatusBar(@NonNull Activity activity){
        Window window = activity.getWindow();
        window.addFlags((~View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                & (~View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                & (~View.SYSTEM_UI_FLAG_FULLSCREEN)
                & (~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY));
    }

    /**
     * 设置全屏
     * @param activity
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setFullScreen(@NonNull Activity activity){
        Window window = activity.getWindow();
        window.addFlags(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * 透明状态栏
     * @param activity
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void translucentStatusBar(@NonNull Activity activity) {
        Window window = activity.getWindow();
        //设置Window为透明
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mContentChild = mContentView.getChildAt(0);

        //移除已经存在假状态栏则,并且取消它的Margin间距
        removeFakeStatusBarViewIfExist(activity);
        removeMarginTopOfContentChild(mContentChild, getStatusHeight(activity));
        if (mContentChild != null) {
            //fitsSystemWindow 为 false, 不预留系统栏位置.
            mContentChild.setFitsSystemWindows(false);
        }
    }

    /**
     * 透明状态栏
     * @param activity
     * @param hideStatusBarBackground 是否保留StatusBar的背景，true会留个半透明的背景
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void translucentStatusBar(@NonNull Activity activity, boolean hideStatusBarBackground) {
        Window window = activity.getWindow();
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (hideStatusBarBackground) {
            //如果为全透明模式，取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置状态栏为透明
            window.setStatusBarColor(Color.TRANSPARENT);
            //设置window的状态栏不可见
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            //如果为半透明模式，添加设置Window半透明的Flag
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置系统状态栏处于可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
        //view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            mChildView.setFitsSystemWindows(false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    /**
     * 为Activity设置状态栏颜色
     * @param activity
     * @param statusColor
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setStatusBarColor(@NonNull Activity activity,int statusColor){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = activity.getWindow();
            //取消状态栏透明
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(statusColor);
            //设置系统状态栏处于可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            //让view不根据系统窗口来调整自己的布局
            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                mChildView.setFitsSystemWindows(false);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }else {
            Window window = activity.getWindow();
            //设置Window为全透明
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            //获取父布局
            View mContentChild = mContentView.getChildAt(0);
            //获取状态栏高度
            int statusBarHeight = UIUtils.getStatusHeight(activity);

            //如果已经存在假状态栏则移除，防止重复添加
            UIUtils.removeFakeStatusBarViewIfExist(activity);
            //添加一个View来作为状态栏的填充
            addFakeStatusBarView(activity, statusColor, statusBarHeight);
            //设置子控件到状态栏的间距
            addMarginTopToContentChild(mContentChild, statusBarHeight);
            //不预留系统栏位置
            if (mContentChild != null) {
                mContentChild.setFitsSystemWindows(false);
            }
            //如果在Activity中使用了ActionBar则需要再将布局与状态栏的高度跳高一个ActionBar的高度，否则内容会被ActionBar遮挡
            int action_bar_id = activity.getResources().getIdentifier("action_bar", "id", activity.getPackageName());
            View view = activity.findViewById(action_bar_id);
            if (view != null) {
                TypedValue typedValue = new TypedValue();
                if (activity.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
                    int actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, activity.getResources().getDisplayMetrics());
                    setContentTopPadding(activity, actionBarHeight);
                }
            }
        }
    }

    /**
     * 将Activity设置的假的StatusBar移除
     * @param activity
     */
    private static void removeFakeStatusBarViewIfExist(@NonNull Activity activity) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();

        View fakeView = mDecorView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
        if (fakeView != null) {
            mDecorView.removeView(fakeView);
        }
    }

    /**
     * 向Activity中添加一个假的StatusBar
     * @param activity
     * @param statusBarColor
     * @param statusBarHeight
     * @return
     */
    private static View addFakeStatusBarView(@NonNull Activity activity, int statusBarColor, int statusBarHeight) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();

        View mStatusBarView = new View(activity);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        layoutParams.gravity = Gravity.TOP;
        mStatusBarView.setLayoutParams(layoutParams);
        mStatusBarView.setBackgroundColor(statusBarColor);
        mStatusBarView.setTag(TAG_FAKE_STATUS_BAR_VIEW);

        mDecorView.addView(mStatusBarView);
        return mStatusBarView;
    }

    /**
     * 为内容设置padding
     * @param activity
     * @param padding
     */
    private static void setContentTopPadding(@NonNull Activity activity, int padding) {
        ViewGroup mContentView = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        mContentView.setPadding(0, padding, 0, 0);
    }

    /**
     * 添加了假的StatusBar后将根布局向下移动一个StatusBar的高度
     * @param mContentChild
     * @param statusBarHeight
     */
    private static void addMarginTopToContentChild(@NonNull View mContentChild, int statusBarHeight) {
        if (mContentChild == null) {
            return;
        }
        if (!TAG_MARGIN_ADDED.equals(mContentChild.getTag())) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
            lp.topMargin += statusBarHeight;
            mContentChild.setLayoutParams(lp);
            mContentChild.setTag(TAG_MARGIN_ADDED);
        }
    }

    /**
     * 移除了假的StatusBar后将根布局向上移动一个StatusBar的高度
     * @param mContentChild
     * @param statusBarHeight
     */
    private static void removeMarginTopOfContentChild(@NonNull View mContentChild, int statusBarHeight) {
        if (mContentChild == null) {
            return;
        }
        if (!TAG_MARGIN_ADDED.equals(mContentChild.getTag())) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
            lp.topMargin -= statusBarHeight;
            mContentChild.setLayoutParams(lp);
            mContentChild.setTag(TAG_MARGIN_REMOVED);
        }
    }

    /**
     * 设置状态栏为亮色模式
     * @param activity
     * @param color
     */
    public static void setStatusBarLightMode(@NonNull Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
            activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.getWindow().setStatusBarColor(color);

            //fitsSystemWindow 为 false, 不预留系统栏位置.
            ViewGroup mContentView = (ViewGroup) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                mChildView.setFitsSystemWindows(false);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }
    }

    /**
     * 按比例设置View的大小，固定宽度
     * @param view
     * @param ratio
     */
    public static void setHeightByWidth(View view, float ratio) {
        if (ratio == 0) {
            throw new IllegalArgumentException("比例不能为零!");
        } else {
            view.getLayoutParams().height = (int) (view.getLayoutParams().width / ratio);
        }
    }

    /**
     * 屏幕截图
     * @param activity
     * @return
     */
    public static Bitmap screenshot(@NonNull Activity activity){
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        Bitmap screenshot;
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity) + getStatusHeight(activity);
        screenshot = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(screenshot);
        c.translate(-decorView.getScrollX(), -decorView.getScrollY());
        decorView.draw(c);
        return screenshot;
    }

    /**
     * 屏幕截图（不包含状态栏）
     * @param activity
     * @return
     */
    public static Bitmap screenshotWithoutStatusBar(@NonNull Activity activity){
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        Bitmap screenshot;
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        int statusBarHeight= getStatusHeight(activity);
        screenshot = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(screenshot);
        c.translate(-decorView.getScrollX(), -decorView.getScrollY());
        decorView.draw(c);
        screenshot = Bitmap.createBitmap(screenshot,0,statusBarHeight,width,height - statusBarHeight);
        return screenshot;
    }

    /**
     * 屏幕截图
     * @param view
     * @return
     */
    public static Bitmap screenshot(@NonNull View view){
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(screenshot);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        return screenshot;
    }

}