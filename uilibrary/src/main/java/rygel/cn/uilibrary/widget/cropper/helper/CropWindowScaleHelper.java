package rygel.cn.uilibrary.widget.cropper.helper;

import android.graphics.RectF;
import androidx.annotation.NonNull;

/**
 * 操控裁剪框的辅助类:操控裁剪框的缩放
 */
class CropWindowScaleHelper {

    private static final String TAG = "CropWindowScaleHelper";

    private Edge mHorizontalEdge;
    private Edge mVerticalEdge;
    private float mWHRatio = 0f;


    CropWindowScaleHelper(Edge verticalEdge,Edge horizontalEdge) {
        mHorizontalEdge = horizontalEdge;
        mVerticalEdge = verticalEdge;
    }

    CropWindowScaleHelper(Edge verticalEdge, Edge horizontalEdge, float WHRatio) {
        mHorizontalEdge = horizontalEdge;
        mVerticalEdge = verticalEdge;
        mWHRatio = WHRatio;
    }

    /**
     * 随着手指的移动而改变裁剪框的大小
     *
     * @param dx        手指相对于x方向的偏移量
     * @param dy        手指相对于y方向的偏移量
     * @param imageRect 用来表示图片边界的矩形
     */
    void updateCropWindow(float dx,
                          float dy,
                          @NonNull RectF imageRect) {

        float x = 0;
        float y = 0;
        if(mWHRatio != 0){
            // 横轴和竖轴的比例计算
            if((mHorizontalEdge == Edge.LEFT || mVerticalEdge == Edge.TOP) &&
                    !(mHorizontalEdge == Edge.LEFT && mVerticalEdge == Edge.TOP)){
                if(Math.abs(dx) > Math.abs(dy)){
                    dy = - dx / mWHRatio;
                }else{
                    dx = - dy * mWHRatio;
                }
            }else {
                if(Math.abs(dx) > Math.abs(dy)){
                    dy = dx / mWHRatio;
                }else{
                    dx = dy * mWHRatio;
                }
            }
            x = mHorizontalEdge.getCoordinate() + dx;
            y = mVerticalEdge.getCoordinate() + dy;

            // 边界检测
            if(x < imageRect.left ||
                    x > imageRect.right ||
                    y < imageRect.top ||
                    y > imageRect.bottom){
                // 越界将偏移量缩小一些再缩放
                updateCropWindow(dx / 4,dy / 4,imageRect);
                return;
            }

            // 最小值检测
            float min = Math.min(mVerticalEdge == Edge.BOTTOM ? Math.abs(y - Edge.TOP.getCoordinate()) : Math.abs(y - Edge.BOTTOM.getCoordinate()),
                    mHorizontalEdge == Edge.RIGHT ? Math.abs(x - Edge.LEFT.getCoordinate()) : Math.abs(x - Edge.RIGHT.getCoordinate())
            );
            if(min < Edge.MIN_CROP_LENGTH_PX){
                // 超出最小值范围，也对偏移量进行放缩
                updateCropWindow(dx / 4,dy / 4,imageRect);
                return;
            }

        }
        if (mHorizontalEdge != null){
            x = mHorizontalEdge.getCoordinate() + dx;
            mHorizontalEdge.updateCoordinate(x, 0, imageRect);
        }

        if (mVerticalEdge != null){
            y = mVerticalEdge.getCoordinate() + dy;
            mVerticalEdge.updateCoordinate(0, y, imageRect);
        }
    }
}