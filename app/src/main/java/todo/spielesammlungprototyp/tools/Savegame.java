package todo.spielesammlungprototyp.tools;

import java.util.Date;
import java.util.UUID;

public class Savegame {

    private String uuid;
    private String value;
    private String activity;
    private Date date;

    // Empty constructor for deserialization (uses setter on "Savegame" object)
    public Savegame() {}

    public Savegame(String value, String activity) {
        this.uuid = UUID.randomUUID().toString();
        this.value = value;
        this.activity = activity;
        this.date = new Date();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
