package todo.spielesammlungprototyp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class Kartenspiele_Auswahl extends AppCompatActivity implements ClickListener{

    RecyclerView recyclerView;
    Spiel_CardViewAdapter adapterC;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Spiel_CardView> spieleListe = new ArrayList<>();
    int[] spiele_icon_id = {R.mipmap.ic_launcher};
    String[] spiele_titel, spiele_details;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kartenspiele_auswahl);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        String[] stringClassnames = getResources().getStringArray(R.array.spiele_activity_kartenspiele);
        intent.setClassName(context, context.getPackageName() + "." + stringClassnames[position]);
        context.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}