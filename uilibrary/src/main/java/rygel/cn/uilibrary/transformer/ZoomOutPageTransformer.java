package rygel.cn.uilibrary.transformer;

import androidx.viewpager.widget.ViewPager;
import android.view.View;

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {

    private float mMinScale = 0.85f;
    private float mMinAlpha = 0.5f;

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
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);
        } else if (position <= 1) { // [-1,1]
            // a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(mMinScale, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between mMinScale and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            view.setAlpha(mMinAlpha + (scaleFactor - mMinScale)
                    / (1 - mMinScale) * (1 - mMinAlpha));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}
