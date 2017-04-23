package todo.spielesammlungprototyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Kartenspiele extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kartenspiele);
    }
    public void gotoBauernkrieg(View view) {
        Intent gotoActivity = new Intent(this, todo.spielesammlungprototyp.Bauernkrieg.BauernkriegMenu.class);
        startActivity(gotoActivity);
    }
    public void gotoMauMauMenu(View view) {
        Intent gotoActivity = new Intent(this, todo.spielesammlungprototyp.MauMau.MauMauMenu.class);
        startActivity(gotoActivity);
    }

}
