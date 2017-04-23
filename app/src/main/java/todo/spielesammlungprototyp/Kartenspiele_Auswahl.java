package todo.spielesammlungprototyp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

public class Kartenspiele_Auswahl extends Activity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Spiel_CardView> spieleListe = new ArrayList<>();
    int[] spiele_icon_id = {R.mipmap.ic_game_dark};
    String[] spiele_titel,spiele_details;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kartenspiele_auswahl);
        spiele_titel = getResources().getStringArray(R.array.spiele_titel_kartenspiele);
        spiele_details = getResources().getStringArray(R.array.spiele_details_kartenspiele);
        int counter = 0;
        for(String titel : spiele_titel)
        {
            Spiel_CardView spiel_cardView = new Spiel_CardView(spiele_icon_id[counter],titel,spiele_details[counter]);
            counter++;
            spieleListe.add(spiel_cardView);
        }
        recyclerView = (RecyclerView) findViewById(R.id.kartenspiele_auswahl_RecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new Spiel_CardViewAdapter(spieleListe);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);

        }
        return super.onOptionsItemSelected(item);
    }
}
