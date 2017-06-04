package todo.spielesammlungprototyp.tools;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

import todo.spielesammlungprototyp.view.activity.GameActivity;

public class Savegame {

    public String uuid;
    public String value;
    public String activity;
    public Date date;

    // Empty constructor for deserialization >>essential<<
    public Savegame() {}

    public Savegame(String value, final Class<? extends GameActivity> activity) {
        this.uuid = UUID.randomUUID().toString();
        this.value = value;
        this.activity = activity.getSimpleName();
        this.date = new Date();
    }

    public void update(String value) {
        this.value = value;
        this.date = new Date();
    }
}
