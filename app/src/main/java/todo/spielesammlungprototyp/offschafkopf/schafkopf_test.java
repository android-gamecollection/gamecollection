package todo.spielesammlungprototyp.offschafkopf;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import ch.aplu.android.*;
import todo.spielesammlungprototyp.R;


public class schafkopf_test extends schafkopf {
    private int Player;
    private Hand[] bids;
    private Deck deck;
    String debug = "debug";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schafkopf_test);


        schafkopf schafkopf = new schafkopf();


    }


    public schafkopf_test(int Player, Hand[] bids, Deck deck){
        this.Player = Player;
        this.bids = bids;
        this.deck = deck;
    }



    public void tester() {

        schafkopf_vergleichsmethoden vglObj = new schafkopf_vergleichsmethoden(this.Player, this.bids);

        Toast.makeText(getApplicationContext(), debugger(), Toast.LENGTH_LONG).show();

    }




    public String debugger(){
        return debug;
    }

}
