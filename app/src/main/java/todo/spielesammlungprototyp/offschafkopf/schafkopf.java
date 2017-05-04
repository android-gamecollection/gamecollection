package todo.spielesammlungprototyp.offschafkopf;
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


    private final int handWidth = 650;
    private Deck deck;
    private final Location[] handLocations =
            {
                    new Location(300, 850),//player1 unten
                    new Location(300, 700),//player1 unten
                    new Location(300, 825),//player1 oben hands[2]
                    new Location(300, 675),//player1 oben hands[3]

                    new Location(300, 125),//player2 unten hands[4}
                    new Location(300, 275),//player2 unten hands[5]
                    new Location(300, 100),//player2 oben
                    new Location(300, 250)//player2 oben


            };

    private final Location pileLocation = new Location(350, 450);

    private Hand[] hands;//8 Hands für Anzeige
    private Hand pile = new Hand(deck);
    private Hand pile2 = new Hand(deck);//Hand für Stiche
    private Hand[] bids = {pile, pile2};//Array Für vergleichsMethoden

    private Hand[] player;//Hand der 2 Spieler mit verdeckten Karten
    private Hand p1Pile = new Hand(deck);//StichStapel
    private Hand p2Pile = new Hand(deck);


    public schafkopf() {
        super(Color.rgb(0, 0, 0), Color.WHITE, BoardType.VERT_FULL, windowZoom(600));
    }

    public void main() {
        deck = new Deck(Suit.values(), Rank.values(), "cover");
        initHands();
        initPlayer();
        if (testAreSynchronized()) showToast("Synchro: OK");
        hands[0].setTouchEnabled(true);
        //ToCompare Objekt um vergleichsmethoden zu nutzen
        //testObjekt um Klasse zu testen
        schafkopf_vergleichsmethoden toCompare = new schafkopf_vergleichsmethoden(0, bids);
        schafkopf_test testObjekt = new schafkopf_test(0, bids, deck);
        showToast(testObjekt.debugger());
        testObjekt.tester();

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


    private boolean testAreSynchronized() {
        //player1 cards
        int numberOfCardsInHand0 = hands[0].getNumberOfCards();
        int numberOfCardsInHand1 = hands[1].getNumberOfCards();
        int numberOfCardsInHand2 = hands[2].getNumberOfCards();
        int numberOfCardsInHand3 = hands[3].getNumberOfCards();
        //player2 cards
        int numberOfCardsInHand4 = hands[4].getNumberOfCards();
        int numberOfCardsInHand5 = hands[5].getNumberOfCards();
        int numberOfCardsInHand6 = hands[6].getNumberOfCards();
        int numberOfCardsInHand7 = hands[7].getNumberOfCards();

        int numberOfCardsPlayer0 = player[0].getNumberOfCards();
        int numberOfCardsPlayer1 = player[1].getNumberOfCards();
        /*
        showToast("player0 : " + numberOfCardsPlayer0);
        showToast("sum : " + (numberOfCardsInHand0+numberOfCardsInHand1+numberOfCardsInHand2+numberOfCardsInHand3));
        showToast("player1 : " + numberOfCardsPlayer1);
        showToast("sum : " + (numberOfCardsInHand4+numberOfCardsInHand5+numberOfCardsInHand6+numberOfCardsInHand7));
        */


        if (!((numberOfCardsInHand0 + numberOfCardsInHand1 + numberOfCardsInHand2 + numberOfCardsInHand3) == numberOfCardsPlayer0))
            return false;

        if (!((numberOfCardsInHand4 + numberOfCardsInHand5 + numberOfCardsInHand6 + numberOfCardsInHand7) == numberOfCardsPlayer1))
            return false;


        for (int i = 0; i < numberOfCardsInHand0; i++) {
            if (!(player[0].get(i).equals(hands[0].get(i)))) {
                showToast("Error: " + player[0].get(i) + " doesn't equal" + hands[0].get(i));
            }
        }

        for (int i = 0; i < numberOfCardsInHand4; i++) {
            if (!(player[1].get(i).equals(hands[4].get(i))))
                showToast("Error: " + player[1].get(i) + " doesn't equal" + hands[4].get(i));
        }
        return true;
    }


    private void initHands() {

        hands = deck.dealingOut(7, 4, true);// 8 hands mit jeweils 4 karten

        RowLayout[] layouts = new RowLayout[8];

        for (int i = 0; i < 8; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            hands[i].setView(this, layouts[i]);
            hands[i].setTargetArea(new TargetArea(pileLocation));
            hands[i].draw();
        }

        for (int i = 1; i < 8; i++)
            hands[i].setVerso(false);//false = revealed

        for (int i = 4; i < 6; i++)
            hands[i].setVerso(true);

        for (int i = 0; i < 2; i++)
            hands[i].setVerso(true);

        pile.setView(this, new StackLayout(pileLocation));
        pile.draw();

        hands[0].addCardListener(new CardAdapter()  // Player 0 plays card
        {
            public void longPressed(Card card) {

                Card played = pile.getLast();//gespielte Karte
                card.transfer(pile, true);//karte wird gespielt
            }

            public void atTarget(Card card, Location targetLocation) {
                //aufgerufen wenn karte bei ziel ist
            }

        });
    }

    private void setPlayerMove(int playerID) {
        hands[playerID].setTouchEnabled(true);
        showToast("Player " + (playerID + 1) + " ");
    }



}





