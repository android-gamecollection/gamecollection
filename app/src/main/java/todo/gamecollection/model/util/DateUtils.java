package todo.gamecollection.model.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    public static boolean isSameDay(Date date1, Date date2) {
        return isSameDay(dateToCalendar(date1), dateToCalendar(date2));
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        boolean sameEra = cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA);
        boolean sameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        return sameEra && sameYear && sameDay;
    }

    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    public static boolean isYesterday(Date date) {
        Calendar day = getToday();
        Calendar cal = dateToCalendar(date);
        day.add(Calendar.DAY_OF_MONTH, -1);
        return isSameDay(day, cal);
    }

    public static boolean isLastWeek(Date date) {
        Calendar day = getToday();
        Calendar cal = dateToCalendar(date);
        day.add(Calendar.DAY_OF_MONTH, -7);
        return cal.after(day);
    }

    public static Calendar dateToCalendar(Date date) {
        Calendar day = new GregorianCalendar();
        day.setTime(date);
        return day;
    }

    private static Calendar getToday() {
        Calendar day = new GregorianCalendar();
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.MILLISECOND, 0);
        return day;
    }
}
