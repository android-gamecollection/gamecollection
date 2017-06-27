package todo.spielesammlungprototyp.model.savegamestorage;

import android.os.Bundle;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import todo.spielesammlungprototyp.App;
import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.util.DateUtils;

public class Savegame implements Comparable<Savegame> {

    public String uuid;
    public String gameUuid;
    public Bundle bundle;
    public Date date;

    // Empty constructor for deserialization >>essential<<
    public Savegame() {
    }

    public Savegame(String gameUuid, Bundle bundle) {
        this.uuid = UUID.randomUUID().toString();
        this.gameUuid = gameUuid;
        this.bundle = bundle;
        this.date = new Date();
    }

    public void update(Bundle bundle) {
        this.bundle = bundle;
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
            str = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale).format(date);
        }
        return str;
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
