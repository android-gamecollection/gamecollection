package todo.spielesammlungprototyp.view.activity;
//JGameGrid DOC http://www.aplu.ch/classdoc/jgamegrid/index.html

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;

import ch.aplu.android.Location;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.StackLayout;
import ch.aplu.jcardgame.TargetArea;
import todo.spielesammlungprototyp.model.games.schafkopf.InitiateGame;

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


    public final Location[] stackLocations =
            {
                    new Location(520, 750),
                    new Location(520, 200)
            };

    public final Location[] bidLocations =
            {
                    new Location(350, 450),
                    new Location(250, 450)
            };

    public Deck deck;
    public Hand[] hands;//16 Hands ala 2 Karten
    public Hand[] bids = new Hand[2];
    public Hand[] stacks = new Hand[2];
    public int z=0;//initialisierung cardlistener


    public schafkopf()
    {
        super(Color.rgb(0, 0, 0), Color.WHITE, BoardType.VERT_FULL, windowZoom(600));
    }

    public void main()
    {
        deck = new Deck(Suit.values(), Rank.values(), "cover");
        InitiateGame game = new InitiateGame(deck, this);
        game.setBids();
        game.setStacks();
        game.setHand();
        initPlayers();
        setPlayerMove(0);
    }


    private void initPlayers()
    {

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

                    /*if(bids[1].isEmpty()){
                        showToast("block");

                        blockNotPlayableCards(1);
                    }*/
                    /*else*/ if (!bids[0].isEmpty() && !bids[1].isEmpty()) {

                            if (sticht(0) == 0)
                            {

                                showToast("P1:"+ bids[0].getLast().toString() +" sticht " + bids[1].getLast().toString());
                                delay(2000);
                                transferBidsToStock(0);
                                delay(1500);
                                if(isGameOver())
                                    calculateResult();
                                setPlayerMove(0);
                            }

                            else {
                                showToast("P2:"+ bids[1].getLast().toString() +" sticht " + bids[0].getLast().toString());
                                delay(2000);
                                transferBidsToStock(1);
                                delay(1500);
                                if(isGameOver())
                                    calculateResult();
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
/*
                    if(bids[0].isEmpty()){
                        showToast("block");

                        blockNotPlayableCards(0);
                    }
                    else*/ if (!bids[0].isEmpty() && !bids[1].isEmpty()) {
                        if (sticht(1) == 1) {

                            showToast("P2:"+ bids[1].getLast().toString() +" sticht " + bids[0].getLast().toString());
                            delay(2000);
                            transferBidsToStock(1);
                            delay(1500);
                            if(isGameOver())
                                calculateResult();;
                            setPlayerMove(1);
                        } else {
                            showToast("P1:"+ bids[0].getLast().toString() +" sticht " + bids[1].getLast().toString());
                            delay(2000);
                            transferBidsToStock(0);
                            delay(1500);
                            if(isGameOver())
                                calculateResult();
                            setPlayerMove(0);
                        }
                    }
                    else setPlayerMove(0);
                }
            });
        }
    }


    private boolean isGameOver(){
        if (stacks[0].getNumberOfCards() + stacks[1].getNumberOfCards() == 32)
            return true;
        return false;
        //showToast("anzahl" + (stacks[0].getNumberOfCards() + stacks[1].getNumberOfCards()));
        //Wenn alle karten auf den stacks sind ist das spiel vorbei
    }

    private void calculateResult(){
        int[] points = new int[2];

        for(int i=0;i<2;i++){
            points[i]= stacks[i].getNumberOfCardsWithRank(Rank.ASS) * 11
                    + stacks[i].getNumberOfCardsWithRank(Rank.ZEHN) * 10
                    + stacks[i].getNumberOfCardsWithRank(Rank.KOENIG) * 4
                    + stacks[i].getNumberOfCardsWithRank(Rank.OBER) * 3
                    + stacks[i].getNumberOfCardsWithRank(Rank.UNTER) * 2;
        }

        printResult(points);


        //Anzahl der Karten mit Punkten pro Spieler herausfinden
       /*
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
        ASS = 11 , ZEHN = 10, KOENIG = 4, OBER = 3, UNTER = 2, Alles andere = 0

        int pointsp1 = ass * 11 + zehn * 10 + k * 4 + o * 3 + u * 2;
        int pointsp2 = ass2 * 11 + zehn2 * 10 + k2 * 4 + o2 * 3 + u2 * 2;

        //Gewinner herausfinden und ausgeben

        */

    }

    private void printResult(int[] p){
        if(p[0] > p[1]){
            showToast("Spieler 1:" + p[0] + "Spieler 2:" + p[1]);
            showToast("Spieler 1 gewinnt!");
        }
        if(p[0] == p[1]){
            showToast("Spieler 1:" + p[0] + "Spieler 2:" + p[1]);
            showToast("Unentschieden!");
        }
        if(p[0] < p[1]){
            showToast("Spieler 1:" + p[0] + "Spieler 2:" + p[1]);
            showToast("Spieler 2 gewinnt!");
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

            for (int i = 0; i < 8; i++) {
                if (hands[i].getFirst().getSuit() == farb)
                    return true;
            }
        }

        if(Player == 1)
        {
            for (int i = 8; i < 16; i++)
            {
                if (hands[i].getFirst().getSuit() == farb)
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


    public Hand returnNewHandWithUpperCards(int player){
        Hand[] hsp = new Hand[8];
        Hand h = new Hand(deck);

        if(player == 0){

        for(int i = 0; i < 8; i++){
                h.insert(hands[i].getFirst(), false);
                if(i == 7)return h;
        }

        }

        if(player == 1){

            for(int i = 0; i < 8; i ++) {
                for(int j = 8; j < 16; i++) {
                    hsp[i] = hands[j];
                    for(int d = 0; d < 8; i++){
                        h.insert(hsp[d].getFirst(), false);
                    if(d == 7)return h;
                    }
                }
            }

        }
    return h;
    }



    public Hand cardsplayable(int Player, Hand hand){
        Hand playable = new Hand(deck);
        int otherPlayer = (Player + 1) % 2;

        for (int i = 0; i < 8; i++) {

            if (    (hand.getFirst().getRank() == Rank.OBER ||
                    hand.getFirst().getRank() == Rank.UNTER ||
                    hand.getFirst().getRank() == Suit.HERZ)
                    && isTrumpf(otherPlayer))
            {
             playable.insert(hand.getFirst(), false);
                playable.removeFirst(false);
            }

            else if (hand.getFirst().getSuitId() == bids[otherPlayer].getLast().getSuitId()){
             playable.insert(hand.getFirst(), false);
                playable.removeFirst(false);
            }
            else if(i == 7)return playable;
        }
        return playable;
    }



    public void blockNotPlayableCards(int Player) {
        showToast("BlockMethodeAufgerufen");
        Hand up = returnNewHandWithUpperCards(Player);
        showToast("up created");
        Hand playable = cardsplayable(Player, up);
        showToast("cardsplayable created");

        if (Player == 0) {
            for (int i = 0; i < 8; i++) {
                if (!playable.contains(hands[i].getFirst()))
                {
                hands[i].setTouchEnabled(false);
                showToast("Disabled");
                }
                else
                {
                hands[i].setTouchEnabled(true);
                showToast("Enabled");
                }
            }
            setPlayerMove(0);

        }
        if (Player == 1) {
            for (int i = 8; i < 16; i++) {
                if (!playable.contains(hands[i].getFirst()))
                {
                    hands[i].setTouchEnabled(false);
                    showToast("Disabled");
                }
                else if(playable.contains(hands[i].getFirst()))
                {
                    hands[i].setTouchEnabled(true);
                    showToast("Enabled");

                }
            }
            setPlayerMove(1);

        }
    }


}




