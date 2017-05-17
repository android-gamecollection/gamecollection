package todo.spielesammlungprototyp.model.games.offschafkopf;

import ch.aplu.jcardgame.Hand;
import todo.spielesammlungprototyp.view.activity.schafkopf;


public  class schafkopf_vergleichsmethoden extends schafkopf implements vergleichsmethoden {
    public schafkopf_vergleichsmethoden(Hand[] bids, Hand[] hands, int Player){
        this.bids = bids;
        this.hands = hands;
        this.Player = Player;
    }

    private int Player = 0;
    private Hand[] bids = super.bids;
    private Hand[] hands = super.hands;

    public void ChangePlayer(int Player) {this.Player = Player;}

    public void ChangeBids(Hand[] bids) {
        this.bids = bids;
    }


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
        else if(sameColor(p)) {
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
        if(p == 1) {

            if (isTrumpf(p)) {
                if (isTrumpfHigher(p)) {
                    return p;
                } else {
                    return 0;
                }
            }

            else if(sameColor(p)) {
                if (isTrumpf(0)) {
                    return 0;
                } else {
                    isRankHigher(p);
                }

            }
            else{
                if(isTrumpf(1)){return 1;}
                else{return p;}
            }
        }

    return p;
    }





    public boolean isTrumpf(int Player) {
        //int Player = Player der ausspielt
        if (bids[Player].getLast().getRankId() == 4 || bids[Player].getLast().getRankId() == 5) {
            return true;
        } else if (bids[Player].getLast().getSuitId() == 3) {
            return true;
        } else {
            return false;
        }
    }


    public boolean isOber(int Player) {
        //int Player = Player der ausspielt
        if (bids[Player].getLast().getRankId() == 4) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isUnter(int Player) {
        //int Player = Player der ausspielt
        if (bids[Player].getLast().getRankId() == 5) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isHerz(int Player) {
        //int Player = Player der ausspielt
        if (bids[Player].getLast().getSuitId() == 3) {
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
                    if (bids[Player].getLast().getSuitId() > bids[1].getLast().getSuitId()) {
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
                    if (bids[Player].getLast().getSuitId() > bids[1].getLast().getSuitId()) {
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
                    if (bids[Player].getLast().getRankId() > bids[1].getLast().getRankId()) {
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
                    if (bids[Player].getLast().getSuitId() > bids[0].getLast().getSuitId()) {
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
                    if (bids[Player].getLast().getSuitId() > bids[0].getLast().getSuitId()) {
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
                    if (bids[Player].getLast().getRankId() > bids[0].getLast().getRankId()) {
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
            if (bids[Player].getLast().getSuitId() > bids[1].getLast().getSuitId()) {
                return true;
            } else {
                return false;
            }
        } else if (Player == 1) {
            if (bids[Player].getLast().getSuitId() > bids[0].getLast().getSuitId()) {
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
            if (bids[Player].getLast().getRankId() > bids[1].getLast().getRankId()) {
                return true;
            } else {
                return false;
            }
        } else if (Player == 1) {
            if (bids[Player].getLast().getRankId() > bids[0].getLast().getRankId()) {
                return true;
            } else {
                return false;
            }
        }
        else return false;
    }
    public boolean sameColor(int player){
        //int player == spieler der ausspielt
    if(player == 0){
        if(bids[player].getLast().getSuitId() == bids[1].getLast().getSuitId()){
            return true;
        }
        else{return false;}
    }
        if(player == 1){
            if(bids[player].getLast().getSuitId() == bids[0].getLast().getSuitId()){
                return true;
            }
            else{return false;}
        }
        else return false;
    }
    public boolean sameRank(int player){
        //int player == spieler der ausspielt

        if(player == 0){
            if(bids[player].getLast().getRankId() == bids[1].getLast().getRankId()){
                return true;
            }
            else{return false;}
        }
        if(player == 1){
            if(bids[player].getLast().getRankId() == bids[0].getLast().getRankId()){
                return true;
            }
            else{return false;}
        }
        else return false;
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

















