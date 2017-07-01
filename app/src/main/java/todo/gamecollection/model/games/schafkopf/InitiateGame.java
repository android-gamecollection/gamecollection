package todo.gamecollection.model.games.schafkopf;

import ch.aplu.android.Location;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.StackLayout;
import ch.aplu.jcardgame.TargetArea;

public class InitiateGame {
    public Hand[] hands;
    public Hand[] bids = new Hand[2];
    public Hand[] stacks = new Hand[2];
    private Deck deck;
    schafkopf board;

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

    public InitiateGame(Deck deck, schafkopf board){
        this.deck = deck;
        this.board = board;
    }

    public void setBids(){

        for (int i = 0; i < 2; i++)
        {
            bids[i] = new Hand(deck);
            bids[i].setView(board, new StackLayout(bidLocations[i]));
            bids[i].draw();
        }
        board.bids=this.bids;
    }

    public void setStacks(){
        for (int i = 0; i < 2; i++)
        {
            stacks[i] = new Hand(deck);
            stacks[i].setView(board, new StackLayout(stackLocations[i]));
            stacks[i].draw();
        }
        board.stacks = this.stacks;
    }


public void setHand(){
    hands = deck.dealingOut(16, 2, true);
    StackLayout[] layouts = new StackLayout[16];

        for (int i = 0; i < 16; i++)
    {
        layouts[i] = new StackLayout(handLocations[i]);
        hands[i].setView(board, layouts[i]);
        hands[i].setTargetArea(new TargetArea(bidLocations[0]));
        if(i>8)hands[i].setTargetArea(new TargetArea(bidLocations[1]));
        hands[i].draw();
    }

    board.hands = this.hands;
}
}
