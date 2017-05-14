package todo.spielesammlungprototyp.offschafkopf;
import ch.aplu.util.Monitor;
import todo.spielesammlungprototyp.offschafkopf.schafkopf_vergleichsmethoden;
//JGameGrid DOC http://www.aplu.ch/classdoc/jgamegrid/index.html

import android.graphics.Color;
import android.widget.Toast;

import ch.aplu.jcardgame.*;
import ch.aplu.android.*;
import java.util.*;

public class schafkopf extends CardGame
{
    public enum Suit
    {
        EICHEL, GRUEN, HERZ, SCHELLEN
    }

    public enum Rank
    {
        ASS, OBER, UNTER, ZEHN, KOENIG, NEUN, ACHT, SIEBEN
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

                public void atTarget(Card card, Location targetLocation) {
                    //if(bids[1].isEmpty())setPlayerMove(1);//abhängig von stich
                    //if(!bids[1].isEmpty())transferBidsTo
                    // Stock(1);
                    if (!bids[0].isEmpty() && !bids[1].isEmpty()) {

                            if (sticht(0) == 0)
                            {

                                showToast("P1:"+ bids[0].getLast().toString() +" sticht " + bids[1].getLast().toString());
                                delay(2000);
                                transferBidsToStock(0);
                                isGameOver();
                                setPlayerMove(0);
                            }

                            else {
                                showToast("P2:"+ bids[1].getLast().toString() +" sticht " + bids[0].getLast().toString());
                                delay(2000);
                                transferBidsToStock(1);
                                isGameOver();
                                setPlayerMove(1);
                            }
                        }
                        else setPlayerMove(1);

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
                    //if (bids[0].isEmpty()) setPlayerMove(0);
                    //if(!bids[0].isEmpty())transferBidsToStock(0);
                    if (!bids[0].isEmpty() && !bids[1].isEmpty()) {
                        if (sticht(1) == 1) {

                            showToast("P2:"+ bids[1].getLast().toString() +" sticht " + bids[0].getLast().toString());
                            delay(2000);
                            transferBidsToStock(1);
                            isGameOver();
                            setPlayerMove(1);
                        } else {
                            showToast("P1:"+ bids[0].getLast().toString() +" sticht " + bids[1].getLast().toString());
                            delay(2000);
                            transferBidsToStock(0);
                            isGameOver();
                            setPlayerMove(0);
                        }
                    }
                    else setPlayerMove(0);
                }
            });
        }
    }

    private void isGameOver(){

    //Wenn alle karten auf den stacks sind ist das spiel vorbei

        if (stacks[0].getNumberOfCards() + stacks[1].getNumberOfCards() == 32) {

            //Anzahl der Karten mit Punkten pro Spieler herausfinden

            int ass = stacks[0].getNumberOfCardsWithRank(Rank.ASS);
            int zehn = stacks[0].getNumberOfCardsWithRank(Rank.ZEHN);
            int k = stacks[0].getNumberOfCardsWithRank(Rank.KOENIG);
            int o = stacks[0].getNumberOfCardsWithRank(Rank.OBER);
            int u = stacks[0].getNumberOfCardsWithRank(Rank.UNTER);

            int ass2 = stacks[1].getNumberOfCardsWithRank(Rank.ASS);
            int zehn2 = stacks[1].getNumberOfCardsWithRank(Rank.ZEHN);
            int k2 = stacks[1].getNumberOfCardsWithRank(Rank.KOENIG);
            int o2 = stacks[1].getNumberOfCardsWithRank(Rank.OBER);
            int u2 = stacks[1].getNumberOfCardsWithRank(Rank.UNTER);


            //Punkte zusammen rechnen
            //ASS = 11 , ZEHN = 10, KOENIG = 4, OBER = 3, UNTER = 2, Alles andere = 0

            int pointsp1 = ass * 11 + zehn * 10 + k * 4 + o * 3 + u * 2;
            int pointsp2 = ass2 * 11 + zehn2 * 10 + k2 * 4 + o2 * 3 + u2 * 2;

            //Gewinner herausfinden und ausgeben

              if(pointsp1 > pointsp2){
                  showToast("Spieler 1:" + pointsp1 + "Spieler 2:" + pointsp2);
                  delay(2000);
                  showToast("Spieler 1 gewinnt!");

              }
              else if(pointsp1 == pointsp2){
                  showToast("Spieler 1:" + pointsp1 + "Spieler 2:" + pointsp2);
                  delay(2000);
                  showToast("Unentschieden!");
              }
              else {
                  showToast("Spieler 1:" + pointsp1 + "Spieler 2:" + pointsp2);
                  delay(2000);
                  showToast("Spieler 2 gewinnt!");
              }
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

        }
        if(playerWon==1)
        {
            for(int i=0;i<8;i++)hands[i].setTouchEnabled(false);
            for(int i=8;i<16;i++)hands[i].setTouchEnabled(true);
            //showToast("Player  2");
        }
    }

    private void disableInputs()
    {
        for(int i=0;i<16;i++)hands[i].setTouchEnabled(false);
    }


    public int sticht(int Player)
    {
        int otherPlayer = (Player + 1) % 2;

        if(isTrumpf(Player))
        {
            if (isTrumpfHigher(Player))
                return Player;
            return otherPlayer;
        }
        if(sameColor()) {
            if (isTrumpf(otherPlayer)) return otherPlayer;
            if (isRankHigher(Player)) return Player;
                else return otherPlayer;
        }
        if(isTrumpf(otherPlayer))return otherPlayer;
        if(!sameColor()) return otherPlayer;
        return Player;
    }




    public boolean isTrumpf(int Player) {
        //int Player = Player der ausspielt
        if(     bids[Player].getLast().getRank() == Rank.OBER ||
                bids[Player].getLast().getRank() == Rank.UNTER ||
                bids[Player].getLast().getSuit() == Suit.HERZ)
        {
            return true;
        }
        return false;
    }




    public boolean isTrumpfHigher(int Player) {
        //int Player = Player der ausspielt
        int otherPlayer = (Player + 1) % 2;

            if (isOber(Player))
            {
                if (isOber(otherPlayer))
                {
                    if (isColorHigher(otherPlayer))
                        return false;    // Wenn beide Ober, entscheidet die höhere Farbe
                }
                return true;
            }

            else if(isUnter(Player))
            {
                if (isOber(otherPlayer))
                    return false;   // Ober sticht Unter

                if (isUnter(otherPlayer))
                {
                    if (isColorHigher(otherPlayer))
                        return false; // Wenn beide Unter, entscheidet die höhere Farbe
                }
                return true;
            }

            else if(isHerz(Player))
            {
                if (isOber(otherPlayer)) // Ober sticht Herz
                    return false;

                if (isUnter(otherPlayer)) // Unter sticht Herz
                    return false;

                else if (isHerz(otherPlayer))
                {
                    if (isRankHigher(otherPlayer))
                        return false; //wenn beide Herz sticht die höhere Zahl
                }
                return true;
            }

        return false;
    }




    public boolean isColorHigher(int Player)
    {
        int otherPlayer = (Player + 1) % 2;
        if (bids[Player].getLast().getSuitId() < bids[otherPlayer].getLast().getSuitId())
            return true;
        return false;
    }



    public boolean isRankHigher(int Player) {
        int otherPlayer = (Player + 1) % 2;
        if (bids[Player].getLast().getRankId() < bids[otherPlayer].getLast().getRankId())
            return true;
        else if(bids[otherPlayer].getLast().getRankId() < bids[Player].getLast().getRankId())
            return false;


        else if (bids[Player].getLast().getRank() == Rank.ASS)
            return true;

        else if (bids[otherPlayer].getLast().getRank() == Rank.ASS)
            return false;

        else return false;
    }




    public boolean sameColor(){
            if(bids[0].getLast().getSuit() == bids[1].getLast().getSuit())
                return true;
            return false;
    }

    public boolean isOber(int Player) {
        //int Player = Player der ausspielt
        if (bids[Player].getLast().getRank() == Rank.OBER)
            return true;
        return false;
    }

    public boolean isUnter(int Player) {
        //int Player = Player der ausspielt
        if (bids[Player].getLast().getRank() == Rank.UNTER)
            return true;
        return false;
    }

    public boolean isHerz(int Player) {
        //int Player = Player der ausspielt
        if (bids[Player].getLast().getSuit() == Suit.HERZ)
            return true;
        return false;
    }

    public boolean sameRank(){

            if(bids[1].getLast().getRank() == bids[0].getLast().getRank())
                return true;
            return false;
    }


    public boolean isColorOnHand(int Player, Suit farb)
    {


        if(Player == 0)
        {

            for (int i = 8; i < 16; i++) {
                if (hands[i].getNumberOfCardsWithSuit(farb) > 0)
                    return true;
            }
        }

        if(Player == 1)
        {
            for (int i = 0; i < 8; i++)
            {
                if (hands[i].getNumberOfCardsWithSuit(farb) > 0)
                    return true;
            }
        }
        return false;
    }



    public boolean TrumpfOnHand(int player) {
//int player = Spieler der ausspielt
        if(player == 0) {
            for(int i = 8; i < 16; i++) {
                if(hands[i].getNumberOfCardsWithSuit(Suit.HERZ) > 0) {
                    return true;
                }
                else if(hands[i].getNumberOfCardsWithRank(Rank.OBER) > 0){
                    return true;
                }
                else if(hands[i].getNumberOfCardsWithRank(Rank.UNTER) > 0) {
                    return true;
                }
                else{return false;}
            }
        }
        else if(player == 0) {
            for(int i = 0; i < 8; i++) {
                if(hands[i].getNumberOfCardsWithSuit(Suit.HERZ) > 0) {
                    return true;
                }
                else if(hands[i].getNumberOfCardsWithRank(Rank.OBER) > 0){
                    return true;
                }
                else if(hands[i].getNumberOfCardsWithRank(Rank.UNTER) > 0) {
                    return true;
                }
                else{return false;}
            }
        }
        return false;

    }
}





//ToCompare Objekt um vergleichsmethoden zu nutzen
//testObjekt um Klasse zu testen
//schafkopf_vergleichsmethoden toCompare = new schafkopf_vergleichsmethoden(0, bids);
//schafkopf_test testObjekt = new schafkopf_test(0, bids, deck);
//showToast(testObjekt.debugger());
//testObjekt.tester();