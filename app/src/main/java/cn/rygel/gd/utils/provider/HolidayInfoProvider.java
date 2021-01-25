package cn.rygel.gd.utils.provider;

import android.content.Context;
import androidx.annotation.Keep;
import cn.rygel.gd.R;
import cn.rygel.gd.app.APP;
import cn.rygel.gd.utils.InputStreamUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import rygel.cn.calendar.bean.Solar;
import rygel.cn.calendarview.provider.impl.DefaultHolidayInfoProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class HolidayInfoProvider extends DefaultHolidayInfoProvider {

    private final static List<Solar> STATUTORY_HOLIDAY = new ArrayList<>();
    private final static List<Solar> MAKE_UP_DAY = new ArrayList<>();
    private final static Calendar calendar = Calendar.getInstance();

    static {
        loadLocalHolidayInfo(APP.getInstance());
    }

    private static void loadLocalHolidayInfo(Context context) {
        final String holidays = InputStreamUtils.readTextFromInputStream(context.getApplicationContext().getResources().openRawResource(R.raw.holiday));
        try {
            final DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
            final Holiday holiday = new Gson().fromJson(holidays, Holiday.class);
            for (String h : holiday.holiday) {
                Date date = format.parse(h);
                if (date != null) {
                    STATUTORY_HOLIDAY.add(dateToSolar(date));
                }
            }
            for (String m : holiday.make_up) {
                Date date = format.parse(m);
                if (date != null) {
                    MAKE_UP_DAY.add(dateToSolar(date));
                }
            }
        } catch (Exception e) {
            Logger.e(e, "load fail");
        }
    }

    private static Solar dateToSolar(Date date) {
        calendar.setTime(date);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH) + 1;
        final int d = calendar.get(Calendar.DATE);
        return new Solar(year, month, d);
    }

    @Override
    public boolean isStatutoryHoliday(Solar solar) {
        for (Solar s : STATUTORY_HOLIDAY) {
            if (s.equals(solar)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMakeUpDay(Solar solar) {
        for (Solar s : MAKE_UP_DAY) {
            if (s.equals(solar)) {
                return true;
            }
        }
        return false;
    }

    @Keep
    private static class Holiday {
        List<String> holiday;
        List<String> make_up;

        public Holiday(List<String> holiday, List<String> make_up) {
            this.holiday = holiday;
            this.make_up = make_up;
        }

        public List<String> getHoliday() {
            return holiday;
        }

        public void setHoliday(List<String> holiday) {
            this.holiday = holiday;
        }

        public List<String> getMake_up() {
            return make_up;
        }

        public void setMake_up(List<String> make_up) {
            this.make_up = make_up;
        }
    }

}
