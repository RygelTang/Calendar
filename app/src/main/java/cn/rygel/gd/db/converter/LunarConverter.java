package cn.rygel.gd.db.converter;

import cn.rygel.gd.utils.calendar.LunarUtils;
import io.objectbox.converter.PropertyConverter;

public class LunarConverter implements PropertyConverter<LunarUtils.Lunar,Integer> {

    @Override
    public LunarUtils.Lunar convertToEntityProperty(Integer databaseValue) {
        return new LunarUtils.Lunar((databaseValue & 1) == 1,
                (databaseValue & 0x3FFC00) >> 10,
                (databaseValue & 0x3C0) >> 6,
                (databaseValue & 0x3E) >> 1);
    }

    @Override
    public Integer convertToDatabaseValue(LunarUtils.Lunar entityProperty) {
        return (entityProperty.lunarYear << 10) |
                (entityProperty.lunarMonth << 6) |
                (entityProperty.lunarDay << 1) |
                (entityProperty.isLeap ? 1 : 0);
    }
}
