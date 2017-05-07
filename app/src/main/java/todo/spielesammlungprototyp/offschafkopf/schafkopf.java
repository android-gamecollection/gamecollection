package todo.spielesammlungprototyp.offschafkopf;
import ch.aplu.util.Monitor;
import todo.spielesammlungprototyp.offschafkopf.schafkopf_vergleichsmethoden;
import todo.spielesammlungprototyp.offschafkopf.schafkopf_test;
//JGameGrid DOC http://www.aplu.ch/classdoc/jgamegrid/index.html

import android.graphics.Color;
import ch.aplu.jcardgame.*;
import ch.aplu.android.*;
import java.util.*;

public class schafkopf extends CardGame {
    public enum Suit {
        SPADES, HEARTS, DIAMONDS, CLUBS
    }

    public enum Rank {
        ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN
        //  ACE, KING, QUEEN, JACK // Debug
    }


    private Deck deck;
    private final Location[] handLocations =
            {
                    new Location(100, 850),//player1
                    new Location(200, 850),
                    new Location(300, 850),
                    new Location(400, 850),

                    new Location(100, 650),
                    new Location(200, 650),
                    new Location(300, 650),
                    new Location(400, 650),

                    new Location(100, 100),//player2
                    new Location(200, 100),
                    new Location(300, 100),
                    new Location(400, 100),

                    new Location(100, 300),
                    new Location(200, 300),
                    new Location(300, 300),
                    new Location(400, 300)



            };

    private final Location p1SticheLocation = new Location(520, 750);
    private final Location p2SticheLocation = new Location(520, 200);
    private final Location pileLocation = new Location(350, 450);
    private final Location pileLocation2 = new Location(250, 450);

    private Hand[] hands;//16 Hands ala 2 Karten

    private int z=0;//counter für initialisierung der cardlistener

    private Hand pile = new Hand(deck);
    private Hand pile2 = new Hand(deck);//Hand für Stiche



    //private Hand[] bids = {pile, pile2};//Array Für vergleichsMethoden

    private Hand[] player;
    private Hand p1Stiche = new Hand(deck);//StichStapel
    private Hand p2Stiche = new Hand(deck);

    private int playerTurn=1;


    public schafkopf() {
        super(Color.rgb(0, 0, 0), Color.WHITE, BoardType.VERT_FULL, windowZoom(600));
    }

    public void main() {
        deck = new Deck(Suit.values(), Rank.values(), "cover");
        initHands();
        //initPlayer();

        //for(int i=0;i<16;i++)hands[i].setTouchEnabled(true);
        setPlayerMove(playerTurn);

        //ToCompare Objekt um vergleichsmethoden zu nutzen
        //testObjekt um Klasse zu testen
        //schafkopf_vergleichsmethoden toCompare = new schafkopf_vergleichsmethoden(0, bids);
        //schafkopf_test testObjekt = new schafkopf_test(0, bids, deck);
        //showToast(testObjekt.debugger());
        //testObjekt.tester();
        }

    private void initPlayer() {
        player = deck.dealingOut(2, 0, true);//hand arrays initalisierung
        initPlayer0();
        initPlayer1();
    }

    private void initPlayer0() {
        for (int n = 0; n < 4; n++) {
            player[0].insert(hands[n], false);//hands[0-3 sind spieler 0)
        }
    }

    private void initPlayer1() {
        for (int i = 4; i < 8; i++) {
            player[1].insert(hands[i], false);//hands[4-7 sind spieler 1)
        }
    }


    private void initHands() {

        hands = deck.dealingOut(16, 2, true);// 8 hands mit jeweils 4 karten

        StackLayout[] layouts = new StackLayout[16];


        for (int i = 0; i < 16; i++) {
            layouts[i] = new StackLayout(handLocations[i]);
            hands[i].setView(this, layouts[i]);
            if(i<8)
                hands[i].setTargetArea(new TargetArea(pileLocation));
            else
                hands[i].setTargetArea(new TargetArea(pileLocation2));
            hands[i].draw();
        }



        //pile.setVerso(false);
        pile.setView(this, new StackLayout(pileLocation));
        pile.draw();

        pile2.setView(this, new StackLayout(pileLocation2));
        pile2.draw();

        p1Stiche.setView(this, new StackLayout(p1SticheLocation));
        p2Stiche.setView(this, new StackLayout(p2SticheLocation));

        p1Stiche.draw();
        p2Stiche.draw();

        for(z=0; z<8;z++) {
            hands[z].addCardListener(new CardAdapter()  // Player 1 plays card
            {
                public void clicked(Card card) {

                    //Card played = pile.getLast();//gespielte Karte
                    if(pile.isEmpty())
                    card.transfer(pile, true);//karte wird gespiel-t
                    //pile.draw();
                }

                public void atTarget(Card card, Location targetLocation) {
                    /*Card top1 = pile.getLast();
                    Card top2 = pile.getLast();
                    if(top1.getRank() > top2.getRank())
                    */


                    if(pile2.isEmpty())setPlayerMove(2);//abhängig von stich
                    if(!pile2.isEmpty())transferBidsToStock(2);
                }


            });
        }

        for(z=8; z<16;z++) {
            hands[z].addCardListener(new CardAdapter()  // Player 2 plays card
            {
                public void clicked(Card card) {

                    //Card played = pile.getLast();//gespielte Karte
                    if(pile2.isEmpty())
                    card.transfer(pile2, true);//karte wird gespiel-t
                    //pile2.draw();
                }

                public void atTarget(Card card, Location targetLocation) {
                    if(pile.isEmpty())setPlayerMove(1);

                    if(!pile.isEmpty())transferBidsToStock(1);
                }


            });
        }}




    private void transferBidsToStock(int playerWon) {
        if (playerWon == 1) {
            pile.setTargetArea(new TargetArea(p1SticheLocation));
            pile2.setTargetArea(new TargetArea(p1SticheLocation));

            pile.transferNonBlocking(pile.getLast(), p1Stiche, true);
            pile2.transferNonBlocking(pile2.getLast(), p1Stiche, true);

        } else {
            pile.setTargetArea(new TargetArea(p2SticheLocation));
            pile2.setTargetArea(new TargetArea(p2SticheLocation));
            pile.transferNonBlocking(pile.getLast(), p2Stiche, true);
            pile2.transferNonBlocking(pile2.getLast(), p2Stiche, true);
        }
    }

    private void setPlayerMove(int playerID) {
        if(playerID==1){
            playerTurn=1;
            for(int i=0;i<8;i++)hands[i].setTouchEnabled(true);
            for(int i=8;i<16;i++)hands[i].setTouchEnabled(false);
            showToast("Player  1");
        }

        if(playerID==2){
            playerTurn=2;
            for(int i=0;i<8;i++)hands[i].setTouchEnabled(false);
            for(int i=8;i<16;i++)hands[i].setTouchEnabled(true);
            showToast("Player  2");
        }




    }

    private void changeCurrentPlayer()
    {
        playerTurn = (playerTurn + 1) % 2;
    }
}





