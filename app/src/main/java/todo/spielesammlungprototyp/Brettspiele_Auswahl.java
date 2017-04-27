package todo.spielesammlungprototyp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class Brettspiele_Auswahl extends Activity implements ClickListener{

    RecyclerView recyclerView;
    Spiel_CardViewAdapter adapterC;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Spiel_CardView> spieleListe = new ArrayList<>();
    int[] spiele_icon_id = {R.mipmap.ic_game_dark};
    String[] spiele_titel, spiele_details;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brettspiele_auswahl);

        spiele_titel = getResources().getStringArray(R.array.spiele_titel_brettspiele);
        spiele_details = getResources().getStringArray(R.array.spiele_details_brettspiele);
        int counter = 0;
        for(String titel : spiele_titel)
        {
            Spiel_CardView spiel_cardView = new Spiel_CardView(spiele_icon_id[counter],titel,spiele_details[counter]);
            counter++;
            spieleListe.add(spiel_cardView);
        }
        recyclerView = (RecyclerView) findViewById(R.id.brettspiele_auswahl_RecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapterC = new Spiel_CardViewAdapter(spieleListe);
        adapterC.setClickListener(this);
        recyclerView.setAdapter(adapterC);
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

    @Override
    public void itemClicked(View view, int position) {
        Intent intent = new Intent();
        Context context = view.getContext();
        String[] stringClassnames = getResources().getStringArray(R.array.spiele_activity_brettspiele);
        intent.setClassName(context, context.getPackageName() + "." + stringClassnames[position]);
        context.startActivity(intent);
    }

}
