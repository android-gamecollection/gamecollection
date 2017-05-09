package todo.spielesammlungprototyp.offschafkopf;
import ch.aplu.util.Monitor;
import todo.spielesammlungprototyp.offschafkopf.schafkopf_vergleichsmethoden;
import todo.spielesammlungprototyp.offschafkopf.schafkopf_test;
//JGameGrid DOC http://www.aplu.ch/classdoc/jgamegrid/index.html

import android.graphics.Color;
import ch.aplu.jcardgame.*;
import ch.aplu.android.*;
import java.util.*;

public class schafkopf extends CardGame
{
    public enum Suit
    {
        EICHEL, BLATT, HERZ, SCHELLEN
    }

    public enum Rank
    {
        ASS, KOENIG, OBER, UNTER, ZEHN, NEUN, ACHT, SIEBEN
        //  ACE, KING, QUEEN, JACK // Debug
    }

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
    private final Location[] stackLocations =
            {
                    new Location(520, 750),
                    new Location(520, 200)
            };

    private final Location[] bidLocations =
            {
                    new Location(350, 450),
                    new Location(250, 450)
            };

    private Deck deck;
    public Hand[] hands;//16 Hands ala 2 Karten
    public Hand[] bids = new Hand[2];
    private Hand[] stacks = new Hand[2];
    private int z=0;//initialisierung cardlistener


    public schafkopf()
    {
        super(Color.rgb(0, 0, 0), Color.WHITE, BoardType.VERT_FULL, windowZoom(600));
    }

    public void main()
    {
        deck = new Deck(Suit.values(), Rank.values(), "cover");
        initBids();
        initStacks();
        initHands();
        setPlayerMove(0);
    }

    private void initBids(){

        for (int i = 0; i < 2; i++)
        {
            bids[i] = new Hand(deck);
            bids[i].setView(this, new StackLayout(bidLocations[i]));
            bids[i].draw();
        }
    }

    private void initStacks(){
        for (int i = 0; i < 2; i++)
        {
            stacks[i] = new Hand(deck);
            stacks[i].setView(this, new StackLayout(stackLocations[i]));
            stacks[i].draw();
        }
    }


    private void initHands()
    {
        hands = deck.dealingOut(16, 2, true);
        StackLayout[] layouts = new StackLayout[16];

        for (int i = 0; i < 16; i++)
        {
            layouts[i] = new StackLayout(handLocations[i]);
            hands[i].setView(this, layouts[i]);
            hands[i].setTargetArea(new TargetArea(bidLocations[0]));
            if(i>8)hands[i].setTargetArea(new TargetArea(bidLocations[1]));
            hands[i].draw();
        }

        for(z=0; z<8;z++)
        {
            hands[z].addCardListener(new CardAdapter()  // Player 1 plays card
            {
                public void longPressed(Card card)
                {
                    if(bids[0].isEmpty())card.transfer(bids[0], true);
                    delay(600);//debug
                }

                public void atTarget(Card card, Location targetLocation)
                {
                    if(bids[1].isEmpty())setPlayerMove(1);//abhängig von stich
                    if(!bids[1].isEmpty())transferBidsToStock(1);
                }


            });
        }

        for(z=8; z<16;z++)
        {
            hands[z].addCardListener(new CardAdapter()  // Player 2 plays card
            {
                public void longPressed(Card card)
                {
                    if(bids[1].isEmpty())card.transfer(bids[1], true);
                    delay(600);//debug
                }

                public void atTarget(Card card, Location targetLocation) {
                    if(bids[0].isEmpty())setPlayerMove(0);
                    if(!bids[0].isEmpty())transferBidsToStock(0);
                }


            });
        }
    }

    private void transferBidsToStock(int playerWon)
    {
            bids[0].setTargetArea(new TargetArea(stackLocations[playerWon]));
            bids[1].setTargetArea(new TargetArea(stackLocations[playerWon]));
            bids[0].transferNonBlocking(bids[0].getLast(), stacks[playerWon], true);
            bids[1].transferNonBlocking(bids[1].getLast(), stacks[playerWon], true);
    }

    private void setPlayerMove(int playerWon)
    {
        if(playerWon==0)
        {
            for(int i=0;i<8;i++)hands[i].setTouchEnabled(true);
            for(int i=8;i<16;i++)hands[i].setTouchEnabled(false);
            showToast("Player  1");
        }
        if(playerWon==1)
        {
            for(int i=0;i<8;i++)hands[i].setTouchEnabled(false);
            for(int i=8;i<16;i++)hands[i].setTouchEnabled(true);
            showToast("Player  2");
        }
    }

    private void disableInputs()
    {
        for(int i=0;i<16;i++)hands[i].setTouchEnabled(false);
    }
}





//ToCompare Objekt um vergleichsmethoden zu nutzen
//testObjekt um Klasse zu testen
//schafkopf_vergleichsmethoden toCompare = new schafkopf_vergleichsmethoden(0, bids);
//schafkopf_test testObjekt = new schafkopf_test(0, bids, deck);
//showToast(testObjekt.debugger());
//testObjekt.tester();