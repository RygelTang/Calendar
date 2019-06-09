package cn.rygel.gd.db.converter;

import io.objectbox.converter.PropertyConverter;
import rygel.cn.calendar.bean.Lunar;

public class LunarConverter implements PropertyConverter<Lunar,Integer> {

    @Override
    public Lunar convertToEntityProperty(Integer databaseValue) {
        return new Lunar((databaseValue & 1) == 1,
                (databaseValue & 0x3FFC00) >> 10,
                (databaseValue & 0x3C0) >> 6,
                (databaseValue & 0x3E) >> 1);
    }

    @Override
    public Integer convertToDatabaseValue(Lunar entityProperty) {
        return (entityProperty.lunarYear << 10) |
                (entityProperty.lunarMonth << 6) |
                (entityProperty.lunarDay << 1) |
                (entityProperty.isLeap ? 1 : 0);
    }
}
