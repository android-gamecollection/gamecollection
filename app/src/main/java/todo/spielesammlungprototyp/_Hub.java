package todo.spielesammlungprototyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class _Hub extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._activity_hub);
    }

    // Funktionen zum wechseln zur anderen Activities
    // (wird mit "onClick" des jeweiligen Buttons aufgerufen)
    // =>
    protected void gotoKartenspiele(View view) {
        Intent gotoActivity = new Intent(this, Kartenspiele.class);
        startActivity(gotoActivity);
    }

    protected void gotoBrettspiele(View view) {
        Intent gotoActivity = new Intent(this, Brettspiele.class);
        startActivity(gotoActivity);
    }
    // <=
}
