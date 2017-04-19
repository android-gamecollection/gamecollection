package todo.spielesammlungprototyp.Bauernkrieg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import todo.spielesammlungprototyp.R;

/**
 * Created by phil2 on 19.04.2017.
 */

public class BauernkriegMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bauernkriegmenu);
    }

    public void gotoBauernkrieggame(View view) {
        Intent gotoActivity = new Intent(this, todo.spielesammlungprototyp.Bauernkrieg.Bauernkrieg.class);
        startActivity(gotoActivity);
    }


    }
