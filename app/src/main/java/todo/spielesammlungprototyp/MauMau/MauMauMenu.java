package todo.spielesammlungprototyp.MauMau;

/**
 * Created by phil2 on 23.04.2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import todo.spielesammlungprototyp.R;

/**
 * Created by phil2 on 19.04.2017.
 */

public class MauMauMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maumaumenu);
    }

    public void gotoMauMauGame(View view) {
        Intent gotoActivity = new Intent(this, todo.spielesammlungprototyp.MauMau.MauMau.class);
        startActivity(gotoActivity);
    }



}