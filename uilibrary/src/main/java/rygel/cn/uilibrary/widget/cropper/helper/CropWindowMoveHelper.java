package rygel.cn.uilibrary.widget.cropper.helper;

import android.graphics.RectF;
import androidx.annotation.NonNull;

/**
 * 表示手指再裁剪框里面，此时手指移动表明是移动（平移）裁剪框的操作
 */
class CropWindowMoveHelper extends CropWindowScaleHelper {

    CropWindowMoveHelper() {
        super(null, null);
    }

    @Override
    void updateCropWindow(float dx,
                          float dy,
                          @NonNull RectF imageRect) {

        //判断手指移动的距离
        final float offsetX = dx;
        final float offsetY = dy;

        //更新裁剪框四条边的坐标
        Edge.LEFT.offset(offsetX);
        Edge.TOP.offset(offsetY);
        Edge.RIGHT.offset(offsetX);
        Edge.BOTTOM.offset(offsetY);

        //////////////裁剪框越界处理/////////////////

        if (Edge.LEFT.isOutsideMargin(imageRect)) {
            //获取此时x越界时的坐标位置
            float currentCoordinate = Edge.LEFT.getCoordinate();

            //重新指定左边的值为初始值
            Edge.LEFT.initCoordinate(imageRect.left);

            //越界的距离
            float offset = Edge.LEFT.getCoordinate() - currentCoordinate;

            //修正最右边的偏移量
            Edge.RIGHT.offset(offset);
        } else if (Edge.RIGHT.isOutsideMargin(imageRect)) {

            float currentCoordinate = Edge.RIGHT.getCoordinate();

            Edge.RIGHT.initCoordinate(imageRect.right);

            float offset = Edge.RIGHT.getCoordinate() - currentCoordinate;

            Edge.LEFT.offset(offset);
        }


        if (Edge.TOP.isOutsideMargin(imageRect)) {

            float currentCoordinate = Edge.TOP.getCoordinate();

            Edge.TOP.initCoordinate(imageRect.top);

            float offset = Edge.TOP.getCoordinate() - currentCoordinate;

            Edge.BOTTOM.offset(offset);

        } else if (Edge.BOTTOM.isOutsideMargin(imageRect)) {

            float currentCoordinate = Edge.BOTTOM.getCoordinate();

            Edge.BOTTOM.initCoordinate(imageRect.bottom);

            float offset = Edge.BOTTOM.getCoordinate() - currentCoordinate;

            Edge.TOP.offset(offset);
        }

    }

}
