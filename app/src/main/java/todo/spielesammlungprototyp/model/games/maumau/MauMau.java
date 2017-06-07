package todo.spielesammlungprototyp.model.games.maumau;

// MauMau2.java
// For debugging, see comments // Debug

import android.graphics.Color;

import java.util.ArrayList;

import ch.aplu.android.Actor;
import ch.aplu.android.GGMessageBox;
import ch.aplu.android.GGPushButton;
import ch.aplu.android.GGPushButtonAdapter;
import ch.aplu.android.GameGrid;
import ch.aplu.android.L;
import ch.aplu.android.Location;
import ch.aplu.android.ToolBar;
import ch.aplu.android.ToolBarAdapter;
import ch.aplu.android.ToolBarItem;
import ch.aplu.android.ToolBarSeparator;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardActor;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardCover;
import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jcardgame.StackLayout;
import ch.aplu.jcardgame.TargetArea;

public class MauMau extends CardGame
{
    public enum Suit
    {
        SPADES, HEARTS, DIAMONDS, CLUBS
    }

    public enum Rank
    {
        ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN
        //  ACE, KING, QUEEN, JACK // Debug
    }
    //

    private final String version = "MauMau V2.0\n(www.aplu.ch)";
    private final String info = "It's your move.";
    private final int nbPlayers = 4;
    private final int nbStartCards = 5;
    //  private final int nbStartCards = 3;  // Debug
    private final int handWidth = 300;
    private Deck deck;
    private final Location[] handLocations =
            {
                    new Location(300, 520),
                    new Location(75, 300),
                    new Location(300, 75),
                    new Location(525, 300)
            };
    private final Location talonLocation = new Location(250, 300);
    private final Location pileLocation = new Location(350, 300);
    private final Location textLocation = new Location(300, 400);
    private final int thinkingTime = 3000;
    //  private final int thinkingTime = 500; // Debug
    private Hand[] hands;
    private Hand pile = new Hand(deck);  // Playing stack
    private Hand talon; // Stock
    private boolean isPartnerMoves = false;
    private GGPushButton okBtn;
    private Location btnLocation = new Location(300, 100);
    private Location hideLocation = new Location(-500, - 500);
    private ToolBarItem spadeItem;
    private ToolBarItem heartItem;
    private ToolBarItem diamondItem;
    private ToolBarItem clubItem;
    private ToolBarSeparator sep0;
    private ToolBarSeparator sep1;
    private ToolBarSeparator sep2;
    private ToolBar toolBar;
    private Location toolBarLocation;
    private Actor trumpActor = null;
    private Suit trump = null;
    private Location trumpActorLocation = new Location(350, 200);

    public MauMau()
    {
        super(Color.rgb(20, 80, 0), Color.WHITE, BoardType.HORZ_FULL, windowZoom(600));
    }

    public void main()
    {
        deck = new Deck(Suit.values(), Rank.values(), "cover");
        //   Card.noVerso = true;    // Debug
        //showToast(version);
        initHands();
        initToolBar();
        double z = getZoomFactor();
        if (z < 0.7)
            setZoomFactor(1);  // Don't scale down too much
        okBtn = new GGPushButton("donebtn");
        setZoomFactor(z);
        addActor(okBtn, hideLocation);
        okBtn.addPushButtonListener(
                new GGPushButtonAdapter()
                {
                    public void buttonClicked(GGPushButton button)
                    {
                        okBtn.setLocation(hideLocation.toReal());
                        setPartnerMoves();
                    }

                });

        while (true)
        {
            if (isPartnerMoves)
            {
                isPartnerMoves = false;
                for (int i = 1; i < nbPlayers; i++)
                {
                    //showToast("Player " + i + " thinking...");
                    delay(thinkingTime);

                    if (!simulateMove(i))  // Error: no cards available talon
                        return;
                    if (checkOver(i))
                        return;
                }
                setMyMove();
            }
            delay(100);
        }
    }

    private void initToolBar()
    {
        double z = getZoomFactor();
        if (z < 0.7)
            setZoomFactor(1);  // Don't scale down too much
        spadeItem = new ToolBarItem("spades_item54");
        heartItem = new ToolBarItem("hearts_item54");
        diamondItem = new ToolBarItem("diamonds_item54");
        clubItem = new ToolBarItem("clubs_item54");
        int h = (int)Math.round(54 * getZoomFactor());  // Items have 54 pixels height
        sep0 = new ToolBarSeparator(20, h, Color.BLACK);
        sep1 = new ToolBarSeparator(20, h, Color.BLACK);
        sep2 = new ToolBarSeparator(20, h, Color.BLACK);
        toolBar = new ToolBar(this);
        toolBar.addItem(spadeItem, sep0, heartItem, sep1, diamondItem, sep2, clubItem);
        setZoomFactor(z);
        int width = toolBar.getWidth();
        Location center = new Location(300, 450).toReal();
        toolBarLocation = new Location(center.x - width / 2, center.y);  // Real coordinates
        toolBar.addNoRefresh(hideLocation);
        toolBar.addToolBarListener(new ToolBarAdapter()
        {
            public void pressed(ToolBarItem item)
            {
                if (item == spadeItem)
                    trump = Suit.SPADES;
                if (item == heartItem)
                    trump = Suit.HEARTS;
                if (item == diamondItem)
                    trump = Suit.DIAMONDS;
                if (item == clubItem)
                    trump = Suit.CLUBS;

                trumpActor = getTrumpActor(trump);
                addActor(trumpActor, trumpActorLocation.toReal());
                showTrumpSelection(false);
                setPartnerMoves();
            }

        });
    }

    private void initHands()
    {
        hands = deck.dealingOut(nbPlayers, nbStartCards, true);
        talon = hands[nbPlayers];

        Card top = talon.getLast();
        talon.remove(top, true);
        pile.insert(top, true);

        hands[0].sort(Hand.SortType.SUITPRIORITY, true);

        RowLayout[] layouts = new RowLayout[nbPlayers];
        for (int i = 0; i < nbPlayers; i++)
        {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            hands[i].setView(this, layouts[i]);
            hands[i].setTargetArea(new TargetArea(pileLocation));
            if (i == 0)
                layouts[i].setStepDelay(10);
            hands[i].draw();
        }
        layouts[0].setStepDelay(0);

        for (int i = 1; i <= nbPlayers; i++)
            hands[i].setVerso(true);

        talon.setView(this, new StackLayout(talonLocation));
        talon.draw();
        pile.setView(this, new StackLayout(pileLocation));
        pile.draw();

        hands[0].addCardListener(new CardAdapter()  // Player 0 plays card
        {
            public void longPressed(Card card)
            {
                if (trumpActor == null)  // No trump
                {
                    Card revealed = pile.getLast();

                    if (card.getRank() == Rank.JACK
                            || card.getRank() == revealed.getRank()
                            || card.getSuit() == revealed.getSuit())
                    {
                        setMyTouchEnabled(false);
                        okBtn.setLocation(hideLocation.toReal());
                        card.transfer(pile, true);
                    }
                    //else
                        //showToast("Selected " + card + " forbidden.");
                }
                else  // Got trump
                {
                    if (card.getRank() == Rank.JACK
                            || card.getSuit() == trump)
                    {
                        setMyTouchEnabled(false);
                        okBtn.setLocation(hideLocation.toReal());
                        card.transfer(pile, true);
                        removeTrumpActor();
                    }
                    //else
                        //showToast("Selected " + card + " forbidden.");

                }
            }

            public void atTarget(Card card, Location targetLocation)
            {
                hands[0].draw();
                if (!checkOver(0))
                {
                    if (pile.getLast().getRank() == Rank.JACK)
                        showTrumpSelection(true);
                    else
                        setPartnerMoves();
                }
            }

        });

        talon.addCardListener(new CardAdapter()  // Player 0 reclaims card from talon
        {
            public void longPressed(Card card)
            {
                setMyTouchEnabled(false);
                card.setVerso(false);
                talon.setTargetArea(new TargetArea(handLocations[0]));
                card.transfer(hands[0], false);
                talon.draw();
            }

            public void atTarget(Card card, Location targetLocation)
            {
                if (targetLocation.equals(handLocations[0]))
                {
                    card.setVerso(false);
                    hands[0].sort(Hand.SortType.SUITPRIORITY, true);
                    if (checkTalon())
                    {
                        // Check if you can play it
                        Card top = pile.getLast();
                        if (card.getRank() == Rank.JACK
                                || card.getRank() == top.getRank()
                                || card.getSuit() == top.getSuit())
                            waitOk();
                        else
                            setPartnerMoves();
                    }
                }
                for (int i = 1; i < nbPlayers; i++)
                {
                    if (targetLocation.equals(handLocations[i]))
                    {
                        card.setVerso(true);
                        hands[i].sort(Hand.SortType.SUITPRIORITY, true);
                    }
                }
            }

        });
        setMyMove();
    }

    private void showTrumpSelection(boolean show)
    {
        if (show)
        {
           //showToast("Select a trump suit");
            toolBar.setLocation(toolBarLocation);  // Virtual loc
            toolBar.setOnTop(CardActor.class);
        }
        else
            toolBar.setLocation(hideLocation);
    }

    private void setMyTouchEnabled(boolean enable)
    {
        talon.setTouchEnabled(enable);
        hands[0].setTouchEnabled(enable);
    }

    private void setMyMove()
    {
        setMyTouchEnabled(true);
        //showToast(info);
    }

    private void waitOk()
    {
        setMyTouchEnabled(true);
        //showToast("Press 'Done'\nor a card to move it");
        setPaintOrder(GGPushButton.class);
        okBtn.setLocation(btnLocation.toReal());
    }

    private void setPartnerMoves()
    {
        isPartnerMoves = true;
    }

    private boolean simulateMove(int nbPlayer)
    // Returns false, if checkDrawingStack() fails
    {
        // Check if a Jack available and play it
        for (Card card : hands[nbPlayer].getCardList())
        {
            if (card.getRank() == Rank.JACK)  // Yes, play it
            {
                removeTrumpActor();
                card.setVerso(false);
                card.transfer(pile, true);
                hands[nbPlayer].sort(Hand.SortType.SUITPRIORITY, true);
                int max = 0;
                Suit maxSuit = Suit.SPADES;
                for (Suit suit : Suit.values())
                {
                    int nb = hands[nbPlayer].getNumberOfCardsWithSuit(suit);
                    if (nb > max)
                    {
                        max = nb;
                        maxSuit = suit;
                    }
                }
                trump = maxSuit;
                trumpActor = getTrumpActor(trump);
                addActorNoRefresh(trumpActor, trumpActorLocation.toReal());
                delay(2000);
                return true;
            }
        }

        // Get list of cards that are allowed
        ArrayList<Card> allowed = new ArrayList<Card>();
        if (trumpActor == null)  // No trump imposed
        {
            Card revealed = pile.getLast();
            for (Card card : hands[nbPlayer].getCardList())
                if (card.getRank() == revealed.getRank()
                        || card.getSuit() == revealed.getSuit())
                    allowed.add(card);
        }
        else  // Trump imposed
        {
            for (Card card : hands[nbPlayer].getCardList())
            {
                if (card.getSuit() == trump)
                    allowed.add(card);
            }
        }
        talon.setTargetArea(new TargetArea(handLocations[nbPlayer]));
        if (allowed.isEmpty())
        {
            Card top = talon.getLast();
            top.transfer(hands[nbPlayer], true);
            removeTrumpActor();
            talon.draw();
            top.setVerso(true);
            return checkTalon();
        }

        Card selectedCard = allowed.get(0);   // Other strategy here
        selectedCard.setVerso(false);
        selectedCard.transfer(pile, true);
        removeTrumpActor();
        hands[nbPlayer].sort(Hand.SortType.SUITPRIORITY, true);
        return true;
    }

    private boolean checkTalon()
    // Returns false, if check fails because there are no cards available
    {
        if (talon.isEmpty())  // Talon empty, need to shuffle
        {
            if (pile.getNumberOfCards() < 2)
            {
                //showToast("Fatal error: No cards available for talon");
                doPause();
                setTouchEnabled(false);
                return false;
            }

            // Show info text
            Actor actor = new Actor("reshuffle");
            addActor(actor, textLocation.toReal());
            L.i("Reshuffling now...");

            L.i("Moving card cover");
            // Move animated card cover from playing pile to talon
            CardCover cardCover =
                    new CardCover(this, pileLocation, deck, 1, 0, false);
            cardCover.slideToTarget(talonLocation, 2, false, true); // On bottom

            L.i("Saving talon top card");
            // Save card on pile top and remove it
            Card topCard = pile.getLast();
            pile.remove(topCard, false);
            // Shuffle cards
            pile.shuffle(false);
            // Hide all pictures
            pile.setVerso(true); // Debug
            // Insert into talon
            for (Card card : pile.getCardList())
                talon.insert(card, false);
            // Cleanup playing pile
            pile.removeAll(false);
            // Insert saved card
            pile.insert(topCard, false);
            // Redraw piles
            pile.draw();
            talon.draw();

            // Remove info text and card cover
            delay(2000);
            actor.removeSelf();
            cardCover.removeSelf();
        }
        return true;
    }

    private boolean checkOver(int nbPlayer)
    {
        if (hands[nbPlayer].isEmpty())
        {
            for (int i = 0; i < nbPlayers; i++)
                hands[i].setVerso(false);   // Show player hands
            refresh();
            setTouchEnabled(false);
            doPause();

            String msg = "Game over. Winner is player: " + nbPlayer;
            GGMessageBox.show("Game over. Summary:", msg, GGMessageBox.ButtonLayout.OK);
            killProcess();
            return true;
        }
        return false;
    }

    private Actor getTrumpActor(Suit suit)
    {
        switch (suit)
        {
            case SPADES:
                return new Actor("bigspade");
            case HEARTS:
                return new Actor("bigheart");
            case DIAMONDS:
                return new Actor("bigdiamond");
            case CLUBS:
                return new Actor("bigclub");
        }
        return null;
    }

    private void removeTrumpActor()
    {
        if (trumpActor != null)
        {
            trumpActor.removeSelf();
            trumpActor = null;
        }
    }

}

