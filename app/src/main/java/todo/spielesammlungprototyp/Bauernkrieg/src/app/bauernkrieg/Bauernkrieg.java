// Bauernkrieg.java

package todo.spielesammlungprototyp.Bauernkrieg.src.app.bauernkrieg;

import ch.aplu.android.*;
import android.graphics.Color;
import ch.aplu.jcardgame.*;
import ch.aplu.util.Monitor;

public class Bauernkrieg extends CardGame
{
    //TEST
 //Kartenwertigkeiten festlegen
  public enum Suit
  {
    KREUZ, HERZ, KARO, PIK
  }

  public enum Rank
  {
    ASS, KOENIG, DAME, BAUER, ZEHN, NEUN, ACHT, SIEBEN, SECHS
  }
  //Deck erstellen
  private Deck deck;
  //Anzahl Spieler festlegen
  private final int nbPlayers = 2;
  //Anzahl Karten die man bekommt
  private final int nbCards = 18;
  private boolean isBlindRound;
  //Hand Position
  private final Location[] handLocations =
  {
    new Location(210, 440),
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
  private Hand[] hands = new Hand[nbPlayers];
  private Hand[] bids = new Hand[nbPlayers];
  private Hand[] stocks = new Hand[nbPlayers];
  private int currentPlayer = 0;
  private int nbCardsAtTarget = 0;
  private int nbCardsTransferred;
  private int nbCardsToTransfer;

  public Bauernkrieg()
  {
    super(Color.rgb(20, 80, 0), Color.WHITE, BoardType.FIXED_SQUARE, windowZoom(600));
  }

  public void main()
  {
    deck = new Deck(Suit.values(), Rank.values(), "cover");
    initBids();
    initStocks();
    initHands();
    showToast("Tap to play card. \nStarting player: left ", true);
    setTouchEnabled(true);
    hands[0].setTouchEnabled(true);

    /**
     * This thread is used to evaluate the highest card & transfer all cards
     * from the bid to the winners stock. Additionally, it activates the touch
     * listener for the winning player.
     */
    while (true)
    {
      Monitor.putSleep();
   
      // Check if blind round needed (only when not a blind round)
      if (!isBlindRound && isSameRank())
      {
        if (isGameOver())
          return;
        isBlindRound = true;
        nbCardsAtTarget = 0;
        for (int i = 0; i < nbPlayers; i++)
        {
          Card c = hands[i].getLast();
          c.transferNonBlocking(bids[i], true);
        }
        continue;
      }

      if (isBlindRound)
      {
        if (isGameOver())
          return;
        isBlindRound = false;
        changeCurrentPlayer();
        showToast("Current player: " + ((currentPlayer == 0) ? "left" : "right"), true);
        hands[currentPlayer].setTouchEnabled(true);
        nbCardsAtTarget = 0;
        continue;
      }

      showToast("Evaluating round...");
      delay(2000);
      Hand eval = new Hand(deck);
      for (int i = 0; i < nbPlayers; i++)
        eval.insert(bids[i].getLast(), false);
      int nbWinner = eval.getMaxPosition(Hand.SortType.RANKPRIORITY);
      transferBidsToStock(nbWinner);
      Monitor.putSleep();  // Wait until transfer is done

      currentPlayer = nbWinner;
      if (isGameOver())
        return;

      showToast("Current player: " + ((currentPlayer == 0) ? "left" : "right"), true);
      hands[nbWinner].setTouchEnabled(true);
      nbCardsAtTarget = 0;
    }
  }

  private void changeCurrentPlayer()
  {
    currentPlayer = (currentPlayer + 1) % nbPlayers;
  }

  private void initHands()
  {
    hands = deck.dealingOut(nbPlayers, nbCards);

    for (int i = 0; i < nbPlayers; i++)
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
    }
  }

  private boolean isSameRank()
  {
    return bids[0].getLast().getRank() == bids[1].getLast().getRank();
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

  private boolean isGameOver()
  {
    if (hands[0].isEmpty())
    {
      int nbCard0 = stocks[0].getNumberOfCards();
      int nbCard1 = stocks[1].getNumberOfCards();
      TextActor winnerLabel = new TextActor("Winner!", YELLOW,
        TRANSPARENT, 16);
      if (nbCard0 > nbCard1)
        addActor(winnerLabel, new Location(65, 490).toReal());
      else if (nbCard0 < nbCard1)
        addActor(winnerLabel, new Location(485, 490).toReal());
      else
        addActor(new TextActor("Tie!", YELLOW, TRANSPARENT, 16), new Location(20, 100).toReal());
      addActor(new TextActor("Game over", YELLOW, TRANSPARENT, 20), new Location(20, 200).toReal());
      addActor(new TextActor(nbCard0 + " cards"), new Location(65, 550).toReal());
      addActor(new TextActor(nbCard1 + " cards"), new Location(485, 550).toReal());
      return true;
    }
    return false;
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
}