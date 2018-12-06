package cn.rygel.gd.db.converter;

import cn.rygel.gd.bean.event.constants.RepeatType;
import io.objectbox.converter.PropertyConverter;

public class RepeatTypeConverter implements PropertyConverter<RepeatType,Integer> {

    @Override
    public RepeatType convertToEntityProperty(Integer databaseValue) {
        return RepeatType.values()[databaseValue];
    }

    @Override
    public Integer convertToDatabaseValue(RepeatType entityProperty) {
        return RepeatType.EVERY_DAY.mIndex;
    }

}
