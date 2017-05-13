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
        ASS, KOENIG, OBER, UNTER, ZEHN, NEUN, ACHT, SIEBEN
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
                    //if(!bids[1].isEmpty())transferBidsToStock(1);

                    if (!bids[0].isEmpty() && !bids[1].isEmpty()) {
                            if (sticht(0) == 0) {
                                transferBidsToStock(0);
                                setPlayerMove(0);
                            } else {
                                transferBidsToStock(1);
                                setPlayerMove(1);
                            }
                        }

                    else if(!bids[0].isEmpty()){
                        setPlayerMove(1);


                    }
                    else{setPlayerMove(1);}

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
                            transferBidsToStock(1);
                            setPlayerMove(1);
                        } else {
                            transferBidsToStock(0);
                            setPlayerMove(0);
                        }
                    }

                    else if(!bids[0].isEmpty()){
                        setPlayerMove(0);


                    }
                    else{setPlayerMove(0);}


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

/*
    ----------------------------------------------
    |              vergleichsmethoden            |
    ______________________________________________
*/
/*
int p == Spieler der ausspielt

<--------------->
sticht(int p)
isTrumpf(int p)
isOber(int p)
isUnter(int p)
isHerz(int p)
isTrumpfHigher(int p)
isColorHigher(int p)
sameColor(int p)
sameRank(int p)
sameColorOnHand(int p)
TrumpfOnHand(int p)
<--------------->
*/
    public int sticht(int p){
        //int p == spieler der ausspielt
        if(p == 0) {

            if (isTrumpf(p)) {
                if (isTrumpfHigher(p)) {
                    return p;
                } else {
                    return 1;
                }
            }
            else if(sameColor()) {
                if (isTrumpf(1)) {
                    return 1;
                } else {
                    isRankHigher(p);
                }

            }
            else{
                if(isTrumpf(1)){return 1;}
                else{return p;}
            }



        }
        else if(p == 1) {

            if (isTrumpf(p)) {
                if (isTrumpfHigher(p)) {
                    return p;
                } else {
                    return 0;
                }
            } else if (sameColor()) {
                if (isTrumpf(0)) {
                    return 0;
                } else {
                    isRankHigher(p);
                }

            } else {
                if (isTrumpf(1)) {
                    return 1;
                } else {
                    return p;
                }
            }
        }
        return p;

    }






    public boolean isTrumpf(int Player) {
        //int Player = Player der ausspielt
        if (bids[Player].getLast().getRank() == Rank.OBER || bids[Player].getLast().getRank() == Rank.UNTER) {
            return true;
        } else if (bids[Player].getLast().getSuit() == Suit.HERZ) {
            return true;
        } else {
            return false;
        }
    }


    public boolean isOber(int Player) {
        //int Player = Player der ausspielt
        if (bids[Player].getLast().getRank() == Rank.OBER) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isUnter(int Player) {
        //int Player = Player der ausspielt
        if (bids[Player].getLast().getRank() == Rank.UNTER) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isHerz(int Player) {
        //int Player = Player der ausspielt
        if (bids[Player].getLast().getSuit() == Suit.HERZ) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isTrumpfHigher(int Player) {
        //int Player = Player der ausspielt
        if (Player == 0) {
            if (isOber(Player)) {
                if (isOber(1)) {
                    if (isColorHigher(Player)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } else if (isUnter(Player)) {
                if (isOber(1)) {
                    return false;
                } else if (isUnter(1)) {
                    if (isColorHigher(Player)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } else if (isHerz(Player)) {
                if (isOber(1)) {
                    return false;
                } else if (isUnter(1)) {
                    return false;
                } else if (isHerz(1)) {
                    if (isRankHigher(Player)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }

        } else if (Player == 1)

        {
            if (isOber(Player)) {
                if (isOber(0)) {
                    if (isColorHigher(Player)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } else if (isUnter(Player)) {
                if (isOber(0)) {
                    return false;
                } else if (isUnter(0)) {
                    if (isColorHigher(Player)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } else if (isHerz(Player)) {
                if (isOber(0)) {
                    return false;
                } else if (isUnter(0)) {
                    return false;
                } else if (isHerz(0)) {
                    if (isRankHigher(Player)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean isColorHigher(int Player) {
        //Suit Ids vergleichen
        //int Player = Player der ausspielt

        if (Player == 0) {
            if (bids[Player].getLast().getSuitId() < bids[1].getLast().getSuitId()) {
                return true;
            } else {
                return false;
            }
        } else if (Player == 1) {
            if (bids[Player].getLast().getSuitId() < bids[0].getLast().getSuitId()) {
                return true;
            } else {
                return false;
            }
        }
        else return false;

    }

    public boolean isRankHigher(int Player) {
        //int Player = Player der ausspielt

        if (Player == 0) {
            if (bids[Player].getLast().getRankId() < bids[1].getLast().getRankId()) {
                return true;
            } else {
                return false;
            }
        } else if (Player == 1) {
            if (bids[Player].getLast().getRankId() < bids[0].getLast().getRankId()) {
                return true;
            } else {
                return false;
            }
        }
        else return false;
    }
    public boolean sameColor(){
        //int player == spieler der ausspielt
            if(bids[0].getLast().getSuit() == bids[1].getLast().getSuit()){
                return true;
            }
            else{return false;}
    }
    public boolean sameRank(){

            if(bids[1].getLast().getRank() == bids[0].getLast().getRank())
            {
                return true;
            }
            else{return false;}
    }

    public boolean sameColorOnHand(int Player, Suit farb) {
//int Player = spieler der ausspielt
        if (Player == 0) {

            for (int i = 8; i < 16; i++) {

                if (hands[i].getNumberOfCardsWithSuit(farb) > 0)
                    return true;
                else {
                    return false;
                }
            }

        }
        else if (Player == 1) {


            for (int i = 0; i < 8; i++) {

                if (hands[i].getNumberOfCardsWithSuit(farb) > 0)
                    return true;
                else {
                    return false;
                }
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