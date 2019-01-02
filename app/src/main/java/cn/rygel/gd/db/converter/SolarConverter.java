package cn.rygel.gd.db.converter;

import cn.rygel.gd.utils.calendar.LunarUtils;
import io.objectbox.converter.PropertyConverter;

public class SolarConverter implements PropertyConverter<LunarUtils.Solar,Integer> {

    public static SolarConverter getInstance(){
        return Instance.sInstance;
    }

    @Override
    public LunarUtils.Solar convertToEntityProperty(Integer databaseValue) {
        return new LunarUtils.Solar((databaseValue & 0x1FFE00) >> 9,
                (databaseValue & 0x1E0) >> 5,
                databaseValue & 0x1F);
    }

    @Override
    public Integer convertToDatabaseValue(LunarUtils.Solar entityProperty) {
        return (entityProperty.solarYear << 9) |
                (entityProperty.solarMonth << 5) |
                entityProperty.solarDay;
    }

    private static class Instance {
        private static SolarConverter sInstance = new SolarConverter();
    }
}
