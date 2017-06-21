package todo.spielesammlungprototyp.view.activity;
//JGameGrid DOC http://www.aplu.ch/classdoc/jgamegrid/index.html

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import java.io.FileOutputStream;

import ch.aplu.android.Location;
import ch.aplu.android.TextActor;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.StackLayout;
import ch.aplu.jcardgame.TargetArea;
import todo.spielesammlungprototyp.model.games.schafkopf.InitiateGame;

import static todo.spielesammlungprototyp.R.id.card;

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

    //Locations für die Anzeige der übrigen Karten
    public Location l1 = new Location(100, 750);
    public Location l2 = new Location(200, 750);
    public Location l3 = new Location(300, 750);
    public Location l4 = new Location(400, 750);
    public Location l5 = new Location(100, 550);
    public Location l6 = new Location(200, 550);
    public Location l7 = new Location(300, 550);
    public Location l8 = new Location(400, 550);


    public Location l9 = new Location(100, 200);
    public Location l10 = new Location(200, 200);
    public Location l11 = new Location(300, 200);
    public Location l12 = new Location(400, 200);
    public Location l13 = new Location(100, 400);
    public Location l14 = new Location(200, 400);
    public Location l15 = new Location(300, 400);
    public Location l16 = new Location(400, 400);


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

    public Hand[] last = new Hand[2];

    public schafkopf()
    {
        super(Color.rgb(20, 80, 0), Color.WHITE, BoardType.VERT_FULL, windowZoom(600));
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
/*
                    if(bids[1].isEmpty()){
                        showToast("block");
                        blockNotPlayableCards(1);
                    }
                    else*/
                    if (!bids[0].isEmpty() && !bids[1].isEmpty()) {
                            saveLast();
                            if (sticht(0) == 0)
                            {
                                showToast("P1:"+ bids[0].getLast().toString() +" sticht " + bids[1].getLast().toString());
                                delay(2000);
                                transferBidsToStock(0);
                                delay(1500);
                                if(isGameOver())
                                    calculateResult();

                                UpdateCardNumber();
                                picknext();
                                setPlayerMove(0);
                            }

                            else {
                                showToast("P2:"+ bids[1].getLast().toString() +" sticht " + bids[0].getLast().toString());
                                delay(2000);
                                transferBidsToStock(1);
                                delay(1500);
                                if(isGameOver())
                                    calculateResult();

                                UpdateCardNumber();
                                picknext();
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

                    /*if(bids[0].isEmpty()){
                        showToast("block");
                        blockNotPlayableCards(0);
                    }
                    else*/

                    if (!bids[0].isEmpty() && !bids[1].isEmpty()) {
                        saveLast();
                        if (sticht(1) == 1) {
                            showToast("P2:"+ bids[1].getLast().toString() +" sticht " + bids[0].getLast().toString());
                            delay(2000);
                            transferBidsToStock(1);
                            delay(1500);
                            if(isGameOver())
                                calculateResult();

                            UpdateCardNumber();
                            picknext();
                            setPlayerMove(1);
                        } else {
                            showToast("P1:"+ bids[0].getLast().toString() +" sticht " + bids[1].getLast().toString());
                            delay(2000);
                            transferBidsToStock(0);
                            delay(1500);
                            if(isGameOver())
                                calculateResult();

                            UpdateCardNumber();
                            picknext();
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
        int winnerp;
        int looserp;

        for(int i=0;i<2;i++){
            points[i]= stacks[i].getNumberOfCardsWithRank(Rank.ASS) * 11
                    + stacks[i].getNumberOfCardsWithRank(Rank.ZEHN) * 10
                    + stacks[i].getNumberOfCardsWithRank(Rank.KOENIG) * 4
                    + stacks[i].getNumberOfCardsWithRank(Rank.OBER) * 3
                    + stacks[i].getNumberOfCardsWithRank(Rank.UNTER) * 2;
        }

        if(points[0] > points[1]) {
            winnerp = points[0];
            looserp = points[1];
        }
        else {
            winnerp = points[1];
            looserp = points[0];
        }

        TextActor winnerLabel = new TextActor("Gewonnen!!", YELLOW,
                TRANSPARENT, 16);
        TextActor looserlabel = new TextActor("Verloren!!", YELLOW,
                TRANSPARENT, 16);


        TextActor win = new TextActor("GEWONNEN!!", YELLOW, 16, 40);
        TextActor loose = new TextActor("VERLOREN!!", YELLOW, 16, 40);

        if (points[0] > points[1]) {
            addActor(win, new Location(400, 850).toReal());
            addActor(new TextActor(winnerp + "Punkte"), new Location(400, 835).toReal());
            addActor(loose, new Location(400, 100).toReal());
            addActor(new TextActor(looserp + "Punkte"), new Location(400, 85).toReal());
        }
        else if (points[0] < points[1]) {
            addActor(win, new Location(400, 100).toReal());
            addActor(new TextActor(winnerp + "Punkte"), new Location(400, 85).toReal());
            addActor(loose, new Location(400, 850).toReal());
            addActor(new TextActor(looserp + "Punkte"), new Location(400, 835).toReal());
        }
        else {
            addActor(new TextActor("Unentschieden!", YELLOW, 16, 40), new Location(280, 450).toReal());
        }
        addActor(new TextActor("Game Over", YELLOW, 20, 40), new Location(280, 460).toReal());


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
            /*
            //FÜR KARTEN BLOCKEN
            if(bids[0].isEmpty() && !bids[1].isEmpty()){
                for(int i=8;i<16;i++)hands[i].setTouchEnabled(false);
            }
            else{*/
            if(isGameOver())
                calculateResult();

            for (int i = 0; i < 8; i++) hands[i].setTouchEnabled(true);
            for (int i = 8; i < 16; i++) hands[i].setTouchEnabled(false);
            //}
        }
        if(playerWon==1)
        {
            /*
            //FÜR KARTEN BLOCKEN
            if(bids[1].isEmpty() && !bids[0].isEmpty()){
                for(int i=0;i<8;i++)hands[i].setTouchEnabled(false);
            }
            else {*/
            if(isGameOver())
                calculateResult();

                for (int i = 0; i < 8; i++) hands[i].setTouchEnabled(false);
                for (int i = 8; i < 16; i++) hands[i].setTouchEnabled(true);
                //showToast("Player  2");
            //}

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


    public void UpdateCardNumber(){

        TextActor t1 = new TextActor(hands[0].getNumberOfCards() + "");
        TextActor t2 = new TextActor(hands[1].getNumberOfCards() + "");
        TextActor t3 = new TextActor(hands[2].getNumberOfCards() + "");
        TextActor t4 = new TextActor(hands[3].getNumberOfCards() + "");

        TextActor t5 = new TextActor(hands[4].getNumberOfCards() + "");
        TextActor t6 = new TextActor(hands[5].getNumberOfCards() + "");
        TextActor t7 = new TextActor(hands[6].getNumberOfCards() + "");
        TextActor t8 = new TextActor(hands[7].getNumberOfCards() + "");

        TextActor t9 = new TextActor(hands[8].getNumberOfCards() + "");
        TextActor t10 = new TextActor(hands[9].getNumberOfCards() + "");
        TextActor t11 = new TextActor(hands[10].getNumberOfCards() + "");
        TextActor t12 = new TextActor(hands[11].getNumberOfCards() + "");

        TextActor t13 = new TextActor(hands[12].getNumberOfCards() + "");
        TextActor t14 = new TextActor(hands[13].getNumberOfCards() + "");
        TextActor t15 = new TextActor(hands[14].getNumberOfCards() + "");
        TextActor t16 = new TextActor(hands[15].getNumberOfCards() + "");

        addActor(t1, l1.toReal());
        addActor(t2, l2.toReal());
        addActor(t3, l3.toReal());
        addActor(t4, l4.toReal());
        addActor(t5, l5.toReal());
        addActor(t6, l6.toReal());
        addActor(t7, l7.toReal());
        addActor(t8, l8.toReal());

        addActor(t9, l9.toReal());
        addActor(t10, l10.toReal());
        addActor(t11, l11.toReal());
        addActor(t12, l12.toReal());
        addActor(t13, l13.toReal());
        addActor(t14, l14.toReal());
        addActor(t15, l15.toReal());
        addActor(t16, l16.toReal());

        delay(2500);

        removeActor(t1);
        removeActor(t2);
        removeActor(t3);
        removeActor(t4);
        removeActor(t5);
        removeActor(t6);
        removeActor(t7);
        removeActor(t8);
        removeActor(t9);
        removeActor(t10);
        removeActor(t11);
        removeActor(t12);
        removeActor(t13);
        removeActor(t14);
        removeActor(t15);
        removeActor(t16);
    }



    public Hand returnNewHandWithUpperCards(int player){
        Hand h = new Hand(deck);
        Hand[] all = new Hand[16];
        all = hands;

        if(player == 0){

        for(int i = 0; i < 8; i++){
                h.insert(all[i].getFirst(), false);
                if(i == 7)return h;
        }

        }

        else if(player == 1){

            for(int i = 8; i < 16; i ++) {
                h.insert(all[i].getFirst(),false);
                if(i == 15)return h;

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
                hand.removeFirst(false);
            }

            else if (hand.getFirst().getSuit() == bids[otherPlayer].getLast().getSuit()){
             playable.insert(hand.getFirst(), false);
                hand.removeFirst(false);
            }
            else if(i == 7)return playable;
            else{hand.removeFirst(false);}
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
            if(playable.isEmpty()){
                for(int i=0;i<8;i++)hands[i].setTouchEnabled(false);
            }
            else{
                    for (int i = 0; i < 8; i++) {
                        if (!playable.contains(hands[i].getFirst())) {
                            hands[i].setTouchEnabled(false);
                            //showToast("Disabled");
                        } else if (playable.contains(hands[i].getFirst())) {
                            hands[i].setTouchEnabled(true);
                            //showToast("Enabled");
                        }
                    }
                    setPlayerMove(0);
                }
        }
        else if (Player == 1) {
            if(playable.isEmpty()){
                for(int i=8;i<16;i++)hands[i].setTouchEnabled(false);
            }
            else {
                for (int i = 8; i < 16; i++) {
                    if (!playable.contains(hands[i].getFirst())) {
                        hands[i].setTouchEnabled(false);
                        //showToast("Disabled");
                    } else if (playable.contains(hands[i].getFirst())) {
                        hands[i].setTouchEnabled(true);
                        //showToast("Enabled");

                    }
                }
                setPlayerMove(1);
            }
            }
    }

    public void saveLast(){
        last[0] = bids[0];
        last[1] = bids[1];
    }
    public void picknext() {

            //TextActor tl1 = new TextActor(last[0].toString());
            //TextActor tl2 = new TextActor(last[1].toString());

            TextActor nm = new TextActor("Gewinner, wähle die nächste Karte!", YELLOW, 16, 40);
            addActor(nm, new Location(200, 475).toReal());

            //addActor(tl2, new Location(520, 470).toReal());

            delay(3000);

            removeActor(nm);
            //removeActor(tl1);
            //removeActor(tl2);
        }


}




