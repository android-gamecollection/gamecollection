/*
TO DO:

SameColor()
SameRank()
Split Deck in open and Blind
compareCardValence()
GameAblauf








*/
package todo.spielesammlungprototyp.offschafkopf;

import android.graphics.Color;

import ch.aplu.android.Location;
import ch.aplu.android.TextActor;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jcardgame.StackLayout;
import ch.aplu.jcardgame.TargetArea;
import ch.aplu.util.Monitor;
import ch.aplu.jcardgame.Deck;

//JGameGrid DOC http://www.aplu.ch/classdoc/jgamegrid/index.html

public class schafkopf extends CardGame {
//Karten wertigkeiten festlegen über enums => Läd automatisch Sprites
    public enum Suits
    {
        EICHEL, BLATT, HERZ, SCHELLEN
    }

    public enum Ranks
    {
    SEVEN, EIGHT, NINE, UNTER, OBER, KING, TEN, ACE
    }

//Locations festlegen und Anzahl spieler/Karten
    private Deck deck;
    private final int nbPlayers = 2;
    private final int nbCards = 8;
    private boolean isBlindRound;

    private final Location[] handLocations =
            {
                    new Location(210, 440),
                    new Location(210, 440),
                    new Location(390, 440),
                    new Location(390, 440),
            };
    private final Location[] bidLocations =
            {
                    new Location(210, 200),
                    new Location(390, 200),
            };
    private final Location[] stockLocations =
            {
                    new Location(90, 400),
                    new Location(510, 400),
            };

    private Hand[] hands = new Hand[nbPlayers + 2];
    private Hand[] bids = new Hand[nbPlayers];
    private Hand[] stocks = new Hand[nbPlayers];

    private int currentPlayer = 0;
    private int nbCardsAtTarget = 0;
    private int nbCardsTransferred;
    private int nbCardsToTransfer;

    public schafkopf()
    {
        super(Color.rgb(20, 80, 0), Color.WHITE, BoardType.FIXED_SQUARE, windowZoom(600));
    }

    private void changeCurrentPlayer()
    {
        currentPlayer = (currentPlayer + 1) % nbPlayers;
    }



public void main(String[]args)
{
    deck = new Deck(Suits.values(), Ranks.values(), "cover");
    //initBids();
    initStocks();
    initHands();
    showToast("Berühre eine Karte um sie zu spielen", true);
    setTouchEnabled(true);
    hands[0].setTouchEnabled(true);
    Hand p1o;
    Hand p1c;
    Hand p2o;
    Hand p2c;
    p1o = hands[0];
    p1c = hands[1];
    p2o = hands[2];
    p2c = hands[3];
}







    private void initStocks()
    {
        for (int i = 0; i < nbPlayers; i++)
        {
            stocks[i] = new Hand(deck);
            stocks[i].setView(this, new StackLayout(stockLocations[i]));
        }
    }

    private void transferBidsToStock(int player)
    {
        nbCardsTransferred = 0;
        nbCardsToTransfer = bids[0].getNumberOfCards() + bids[1].getNumberOfCards();
        for (int i = 0; i < nbPlayers; i++)
        {
            bids[i].setTargetArea(new TargetArea(stockLocations[player]));
            Card c = bids[i].getLast();
            while (c != null)
            {
                c.setVerso(true);
                bids[i].transferNonBlocking(c, stocks[player], false);
                c = bids[i].getLast();
            }
        }
    }

//Init Hands
private void initHands()
{
    hands = deck.dealingOut(nbPlayers, nbCards);

    /*for (int i = 0; i < nbPlayers; i++)
    {
        hands[i].setView(this, new StackLayout(handLocations[i]));
        hands[i].setVerso(true);
        final int k = i;
        hands[i].addCardListener(new CardAdapter()
        {
            public void pressed(Card card)
            {
                hands[currentPlayer].setTouchEnabled(false);
                card.setVerso(false);
                card.transferNonBlocking(bids[k], true);
            }

            public void atTarget(Card card, Location loc)
            {
                // Normal round, one card at target
                if (!isBlindRound && nbCardsAtTarget == 0)
                {
                    nbCardsAtTarget = 1;
                    changeCurrentPlayer();
                    hands[currentPlayer].setTouchEnabled(true);
                    return;
                }

                if (isBlindRound)
                {
                    if (nbCardsAtTarget == 0)
                    {
                        nbCardsAtTarget = 1;
                        return;
                    }
                    if (nbCardsAtTarget == 1)
                    {
                        Monitor.wakeUp();
                        return;
                    }
                }

                Monitor.wakeUp(); // Resume main thread to evaluate
            }

        });
        hands[i].draw();
    }*/
}
    private boolean isGameOver()
    {
        if (hands[0].isEmpty())
        {
            int nbCard0 = stocks[0].getNumberOfCards();
            int nbCard1 = stocks[1].getNumberOfCards();
            TextActor winnerLabel = new TextActor("Gewonnen!", YELLOW,
                    TRANSPARENT, 16);
            if (nbCard0 > nbCard1)
                addActor(winnerLabel, new Location(65, 490).toReal());
            else if (nbCard0 < nbCard1)
                addActor(winnerLabel, new Location(485, 490).toReal());
            else
                addActor(new TextActor("Unentschieden!", YELLOW, TRANSPARENT, 16), new Location(20, 100).toReal());
            addActor(new TextActor("Game Over", YELLOW, TRANSPARENT, 20), new Location(20, 200).toReal());
            addActor(new TextActor(nbCard0 + " Karten"), new Location(65, 550).toReal());
            addActor(new TextActor(nbCard1 + " Karten"), new Location(485, 550).toReal());
            return true;
        }
        return false;
    }

    private void initBids()
    {
        for (int i = 0; i < nbPlayers; i++)
        {
            bids[i] = new Hand(deck);
            bids[i].setView(this, new RowLayout(bidLocations[i], 130));
            bids[i].draw();
            bids[i].addCardListener(new CardAdapter()
            {
                public void atTarget(Card card, Location loc)
                {
                    nbCardsTransferred++;
                    if (nbCardsTransferred == nbCardsToTransfer)
                        Monitor.wakeUp();
                }

            });
        }
    }

    private boolean isTrumpf(){
        return true;
    }
    private boolean isTrumpfHigher() {
        return true;
    }
    private boolean isOber(){
        return true;
    }
    private boolean isUnter(){
        return true;
    }
    private boolean isHerz(){
        return true;
    }


    private boolean sameColorOnHand(){
        return true;
    }
    private boolean isRankHigher(){
        return true;
    }










}
























