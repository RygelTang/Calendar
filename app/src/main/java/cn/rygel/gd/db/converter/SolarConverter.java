package cn.rygel.gd.db.converter;

import io.objectbox.converter.PropertyConverter;
import rygel.cn.calendar.bean.Solar;

public class SolarConverter implements PropertyConverter<Solar,Integer> {

    @Override
    public Solar convertToEntityProperty(Integer databaseValue) {
        return new Solar((databaseValue & 0x1FFE00) >> 9,
                (databaseValue & 0x1E0) >> 5,
                databaseValue & 0x1F);
    }

    @Override
    public Integer convertToDatabaseValue(Solar entityProperty) {
        return (entityProperty.solarYear << 9) |
                (entityProperty.solarMonth << 5) |
                entityProperty.solarDay;
    }

}
