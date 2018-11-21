package cn.rygel.gd.utils;

public class CalendarUtils {

    private static final int[] LEAP_YEAR_DAYS_OF_MONTH = {31,29,31,30,31,30,31,31,30,31,30,31};
    private static final int[] COMMON_YEAR_DAYS_OF_MONTH = {31,28,31,30,31,30,31,31,30,31,30,31};

    /**
     * 星期,0开始，0对应周日
     * @param solar
     * @return
     */
    public static int getWeekDay(LunarUtils.Solar solar){
        return getIntervalDaysToBase(solar) % 7;
    }

    /**
     * 是否闰年
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year){
        return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0);
    }

    /**
     * 一年内的第几天
     * @param day
     * @return
     */
    public static int getDaysInYear(LunarUtils.Solar day){
        final boolean isLeapYear = isLeapYear(day.solarYear);
        int daysInYear = day.solarDay;
        for(int i = 0;i < day.solarMonth - 1;i++){
            if(isLeapYear){
                daysInYear += LEAP_YEAR_DAYS_OF_MONTH[i];
            }else{
                daysInYear += COMMON_YEAR_DAYS_OF_MONTH[i];
            }
        }
        return daysInYear;
    }

    /**
     * 求当年的第几天的日期
     * @param days
     * @param year
     * @return
     */
    private static LunarUtils.Solar dayInYear(int days, int year){
        //System.out.println(" days : " + days + " year : " + year);
        final boolean isLeapYear = isLeapYear(year);
        LunarUtils.Solar solar = new LunarUtils.Solar(year,0,days);
        while(days > 0){
            solar.solarDay = days;
            solar.solarMonth++;
            if(isLeapYear){
                days -= LEAP_YEAR_DAYS_OF_MONTH[solar.solarMonth - 1];
            }else {
                days -= COMMON_YEAR_DAYS_OF_MONTH[solar.solarMonth - 1];
            }
        }
        return solar;
    }

    /**
     * 日期间隔
     * @param start
     * @param end
     * @return
     */
    public static int getIntervalDays(LunarUtils.Solar start, LunarUtils.Solar end){
        return getIntervalDaysToBase(end) - getIntervalDaysToBase(start);
    }

    /**
     * 根据日期间隔计算日期
     * @param start
     * @param interval
     * @return
     */
    public static LunarUtils.Solar getDayByInterval(LunarUtils.Solar start,int interval){
        final int daysToBase = getIntervalDaysToBase(start);
        final int newDayToBase = daysToBase + interval;
        int year = newDayToBase / 365;  //大致估计年份
        final int leapOffset = getLeapYearCount(year);  //大致估计闰年年份(因为年份是不准确的，所以这里也是不准确的)
        final int yearOffset = leapOffset / 365;    //大致估计年份的偏差
        year -= yearOffset; //粗略修正偏差
        LunarUtils.Solar yearBase = new LunarUtils.Solar(year,1,1);
        int yearBaseDaysToBase = getIntervalDaysToBase(yearBase); //计算当年的年初到标准年的间隔
        int offset = newDayToBase - yearBaseDaysToBase + 1; //误差
        int daysOfYear = isLeapYear(year) ? 366 : 365;
        while (offset < 0){
            yearBase = new LunarUtils.Solar(--year,1,1);
            yearBaseDaysToBase = getIntervalDaysToBase(yearBase); //计算当年的年初到标准年的间隔
            offset = newDayToBase - yearBaseDaysToBase + 1; //误差
        }
        while (offset >= daysOfYear){
            yearBase = new LunarUtils.Solar(++year,1,1);
            yearBaseDaysToBase = getIntervalDaysToBase(yearBase); //计算当年的年初到标准年的间隔
            offset = newDayToBase - yearBaseDaysToBase + 1; //误差
            daysOfYear = isLeapYear(year) ? 366 : 365;
        }
        return dayInYear(offset,year);
    }

    /**
     * 计算到基准年的日期间隔
     * @param day
     * @return
     */
    private static int getIntervalDaysToBase(LunarUtils.Solar day){
        final int leapYearCount = getLeapYearCount(day.solarYear);
        int daysInYear = getDaysInYear(day);
        return leapYearCount + day.solarYear * 365 + daysInYear;
    }

    /**
     * 闰年的年数，包含今年
     * @param year
     * @return
     */
    private static int getLeapYearCount(int year){
        return year / 4 - year / 100 + year % 400;
    }

}