package todo.spielesammlungprototyp.tools;


import java.util.Date;
import java.util.UUID;

public class SavegameObject {

    private UUID uuid;
    private String value;
    private String activity;
    private Date date;

    public SavegameObject(String value, String activity) {
        this.uuid = UUID.randomUUID();
        this.value = value;
        this.activity = activity;
        this.date = new Date();
    }

    public SavegameObject(UUID uuid, String value, String activity) {
        this.uuid = uuid;
        this.value = value;
        this.activity = activity;
        this.date = new Date();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
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
