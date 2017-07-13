package todo.gamecollection.model.games.schafkopf;
//JGameGrid DOC http://www.aplu.ch/classdoc/jgamegrid/index.html

import android.graphics.Color;

import java.util.ArrayList;

import ch.aplu.android.Location;
import ch.aplu.android.TextActor;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.TargetArea;

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

    public Deck deck;
    public Hand[] hands;//16 Hands ala 2 Karten
    public Hand[] bids = new Hand[2];
    public Hand[] stacks = new Hand[2];
    public int z=0;//initialisierung cardlistener

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

                    if (isBidFull()) {
                            if (sticht(0) == 0)
                            {
                                delay(2000);
                                transferBidsToStock(0);
                                delay(1500);
                                if(isGameOver())
                                    calculateResult();

                                setPlayerMove(0);
                            }

                            else {
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

                    if (isBidFull()) {
                        if (sticht(1) == 1) {
                            delay(2000);
                            transferBidsToStock(1);
                            delay(1500);
                            if(isGameOver())
                                calculateResult();

                            setPlayerMove(1);
                        } else {
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

    private boolean isBidFull() {
        if (!bids[0].isEmpty() && !bids[1].isEmpty()) {
            return true;
        }
        else return false;
    }
    private boolean isGameOver(){
        if (stacks[0].getNumberOfCards() + stacks[1].getNumberOfCards() == 32)
            return true;
        return false;
        //Wenn alle karten auf den stacks sind ist das spiel vorbei
    }

    private void calculateResult(){
        int[] points = new int[2];
        //Berechne Punkte nach Schafkopfregeln
        for(int i=0;i<2;i++){
            points[i]= stacks[i].getNumberOfCardsWithRank(Rank.ASS) * 11
                    + stacks[i].getNumberOfCardsWithRank(Rank.ZEHN) * 10
                    + stacks[i].getNumberOfCardsWithRank(Rank.KOENIG) * 4
                    + stacks[i].getNumberOfCardsWithRank(Rank.OBER) * 3
                    + stacks[i].getNumberOfCardsWithRank(Rank.UNTER) * 2;
        }

        winLabel(points);
    }
    private void winLabel(int[] points){
        //Textanzeige gewonnen verloren

        TextActor win = new TextActor("GEWONNEN!!", YELLOW, 16, 40);
        TextActor loose = new TextActor("VERLOREN!!", YELLOW, 16, 40);

        //Je nachdem wer gewonnen hat wird das win label für player 1 oder 2 angezeigt

        if (points[0] > points[1]) {
            addActor(win, new Location(400, 850).toReal());
            addActor(new TextActor(points[0] + "Punkte"), new Location(400, 835).toReal());
            addActor(loose, new Location(400, 100).toReal());
            addActor(new TextActor(points[1] + "Punkte"), new Location(400, 85).toReal());
        }
        else if (points[0] < points[1]) {
            addActor(win, new Location(400, 100).toReal());
            addActor(new TextActor(points[1] + "Punkte"), new Location(400, 85).toReal());
            addActor(loose, new Location(400, 850).toReal());
            addActor(new TextActor(points[0] + "Punkte"), new Location(400, 835).toReal());
        }
        else {
            addActor(new TextActor("Unentschieden!", YELLOW, 16, 40), new Location(280, 450).toReal());
        }
        addActor(new TextActor("Game Over", YELLOW, 20, 40), new Location(280, 460).toReal());
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
            if(isGameOver())
                calculateResult();

            for (int i = 0; i < 8; i++) hands[i].setTouchEnabled(true);
            for (int i = 8; i < 16; i++) hands[i].setTouchEnabled(false);
        }
        if(playerWon==1)
        {
            if(isGameOver())
                calculateResult();

                for (int i = 0; i < 8; i++) hands[i].setTouchEnabled(false);
                for (int i = 8; i < 16; i++) hands[i].setTouchEnabled(true);
        }
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

        //RANKS sind nach id geordnet, sowie sie im enum angeordnet sind
        //Also Ass (0) -> ober (1) -> usw...

        if (bids[Player].getLast().getRankId() < bids[otherPlayer].getLast().getRankId())
            return true;
        else if(bids[otherPlayer].getLast().getRankId() < bids[Player].getLast().getRankId())
            return false;

        //Ass ist der höchste rank bei einem normalen Farbstich -> instant sieg

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
}




