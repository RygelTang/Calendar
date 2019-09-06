package cn.rygel.gd.ui.setting.widget.month;

import com.bilibili.boxing.loader.IBoxingCrop;
import com.yalantis.ucrop.model.AspectRatio;

import cn.rygel.gd.bean.WidgetType;
import cn.rygel.gd.ui.setting.widget.ImageWidgetSettingActivity;
import cn.rygel.gd.utils.CustomBoxingCrop;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendar.utils.SolarUtils;

public class MonthWidgetSettingActivity extends ImageWidgetSettingActivity {

    @Override
    protected WidgetType getWidgetType() {
        return WidgetType.MONTH_WIDGET;
    }

    @Override
    protected void invalidate() {
        mDemoTvTips.setText(generateMonthInfo(SolarUtils.today()));
        super.invalidate();
    }

    @Override
    protected IBoxingCrop getCropper() {
        return new CustomBoxingCrop(new AspectRatio[]{
                new AspectRatio("1 : 1", 1, 1),
                new AspectRatio("4 : 3", 4, 3),
                new AspectRatio("5 : 4", 5, 4),
        });
    }

    private static String generateMonthInfo(Solar solar) {
        return solar.solarYear + "年" + solar.solarMonth + "月";
    }

}
