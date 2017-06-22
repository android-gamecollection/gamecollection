package todo.spielesammlungprototyp.model.util;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import todo.spielesammlungprototyp.App;
import todo.spielesammlungprototyp.R;

public class Savegame implements Comparable<Savegame> {

    public String uuid;
    public String gameUuid;
    public String value;
    public Date date;

    // Empty constructor for deserialization >>essential<<
    public Savegame() {
    }

    public Savegame(String gameUuid, String value) {
        this.uuid = UUID.randomUUID().toString();
        this.gameUuid = gameUuid;
        this.value = value;
        this.date = new Date();
    }

    void update(String value) {
        this.value = value;
        this.date = new Date();
    }

    public String getDateString() {
        String str;
        Locale locale = Locale.getDefault();
        if (DateUtils.isToday(date)) {
            str = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, locale).format(date);
        } else if (DateUtils.isYesterday(date)) {
            str = App.getContext().getString(R.string.yesterday);
        } else if (DateUtils.isLastWeek(date)) {
            str = DateUtils.dateToCalendar(date).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale);
        } else {
            return SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale).format(date);
        }
        return str.toUpperCase();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Savegame && this == obj;
    }

    @Override
    public int compareTo(@NonNull Savegame o) {
        return date.compareTo(o.date);
    }
}
