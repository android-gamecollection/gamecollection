package todo.spielesammlungprototyp;

class Spiel_CardView {

    Spiel_CardView(int spiele_icon_id, String spiele_titel, String spiele_details) {
        this.spiele_icon_id = spiele_icon_id;
        this.spiele_titel = spiele_titel;
        this.spiele_details = spiele_details;
    }

    private int spiele_icon_id;
    private String spiele_titel,spiele_details;

    int getSpiele_icon_id() {
        return spiele_icon_id;
    }

    public void setSpiele_icon_id(int spiele_icon_id) {
        this.spiele_icon_id = spiele_icon_id;
    }

    String getSpiele_titel() {
        return spiele_titel;
    }

    public void setSpiele_titel(String spiele_titel) {
        this.spiele_titel = spiele_titel;
    }

    String getSpiele_details() {
        return spiele_details;
    }

    public void setSpiele_details(String spiele_details) {
        this.spiele_details = spiele_details;
    }
}
