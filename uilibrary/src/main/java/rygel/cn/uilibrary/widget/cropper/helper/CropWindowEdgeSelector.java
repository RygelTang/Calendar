package rygel.cn.uilibrary.widget.cropper.helper;

import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * 表示手指选中的裁剪框的哪一个边：有如下几种情况：
 * 手指选中一条边的情况：LEFT,TOP,RIGHT,BOTTOM
 * 手指选中两条边的情况：此时手指位于裁剪框的四个角度的某一个：LEFT and TOP, TOP and RIGHT, RIGHT and BOTTOM, BOTTOM and RIGHT
 * 手指在裁剪框的中间区域，此时移动手指进行的是平移操作
 */
public enum CropWindowEdgeSelector {

    /////////////////////图A///////////////////////////

    // 静态左上角：调整的话会按比例来放缩
    STATIC_TOP_LEFT(new CropWindowScaleHelper(Edge.TOP,Edge.LEFT,Edge.sWHRatio)),

    // 静态右下角：调整的话会按比例来放缩
    STATIC_BOTTOM_RIGHT(new CropWindowScaleHelper(Edge.BOTTOM,Edge.RIGHT,Edge.sWHRatio)),

    // 静态右上角：调整的话会按比例来放缩
    STATIC_TOP_RIGHT(new CropWindowScaleHelper(Edge.TOP,Edge.RIGHT,Edge.sWHRatio)),

    // 静态左下角：调整的话会按比例来放缩
    STATIC_BOTTOM_LEFT(new CropWindowScaleHelper(Edge.BOTTOM,Edge.LEFT,Edge.sWHRatio)),

    //左上角：此时是控制裁剪框最上边和最左边的两条边
    TOP_LEFT(new CropWindowScaleHelper(Edge.TOP, Edge.LEFT)),

    //右上角：此时是控制裁剪框最上边和最右边的两条边
    TOP_RIGHT(new CropWindowScaleHelper(Edge.TOP, Edge.RIGHT)),

    //左下角：此时是控制裁剪框最下边和最左边的两条边
    BOTTOM_LEFT(new CropWindowScaleHelper(Edge.BOTTOM, Edge.LEFT)),

    //右下角：此时是控制裁剪框最下边和最右边的两条边
    BOTTOM_RIGHT(new CropWindowScaleHelper(Edge.BOTTOM, Edge.RIGHT)),

    //////////////////图C/////////////

    //仅控制裁剪框左边线
    LEFT(new CropWindowScaleHelper(null, Edge.LEFT)),

    //仅控制裁剪框右边线
    TOP(new CropWindowScaleHelper(Edge.TOP, null)),

    //仅控制裁剪框上边线
    RIGHT(new CropWindowScaleHelper(null, Edge.RIGHT)),

    //仅控制裁剪框下边线
    BOTTOM(new CropWindowScaleHelper(Edge.BOTTOM, null)),

    //////////////图B//////////////

    //中间位置
    CENTER(new CropWindowMoveHelper());

    private CropWindowScaleHelper mHelper;

    CropWindowEdgeSelector(CropWindowScaleHelper helper) {
        mHelper = helper;
    }

    public void updateCropWindow(float dx, float dy, @NonNull RectF imageRect) {

        mHelper.updateCropWindow(dx, dy, imageRect);

    }

}