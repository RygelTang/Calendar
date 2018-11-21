package rygel.cn.uilibrary.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

public class MultiZoomOutPageTransformer implements ViewPager.PageTransformer {

    private float mOffsetProgress = 0.5f;
    private float mMaxScale = 0.88f;
    private float mMinScale = 0.54f;
    private float mMinAlpha = 0.5f;

    public float getOffsetProgress() {
        return mOffsetProgress;
    }

    public void setOffsetProgress(float offsetProgress) {
        mOffsetProgress = offsetProgress;
    }

    public float getMaxScale() {
        return mMaxScale;
    }

    public void setMaxScale(float maxScale) {
        mMaxScale = maxScale;
    }

    public float getMinScale() {
        return mMinScale;
    }

    public void setMinScale(float minScale) {
        mMinScale = minScale;
    }

    public float getMinAlpha() {
        return mMinAlpha;
    }

    public void setMinAlpha(float minAlpha) {
        mMinAlpha = minAlpha;
    }

    @Override
    public void transformPage(View view, float v) {
        if(view.getParent() != null){
            View parent = ((View) view.getParent());
            final int width = parent.getWidth();
            if(v < -(1 + mOffsetProgress) || v > (1 + mOffsetProgress)) {
                view.setAlpha(0);
            }else {
                final float scale = Math.max(mMinScale,mMaxScale - Math.abs(v));
                final float alpha = Math.max(mMinAlpha,1 - Math.abs(v));
                view.setScaleX(scale);
                view.setScaleY(scale);

                view.setTranslationX( - v * (width - view.getMeasuredWidth() * mOffsetProgress));

                view.setAlpha(alpha);
            }
        }
    }

}
