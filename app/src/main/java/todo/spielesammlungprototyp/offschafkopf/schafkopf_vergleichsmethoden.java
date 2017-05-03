package todo.spielesammlungprototyp.offschafkopf;
import android.graphics.Color;

import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.Hand;


public  class schafkopf_vergleichsmethoden extends schafkopf implements vergleichsmethoden{


    public schafkopf_vergleichsmethoden(int Player, Hand[] bids) {
        this.Player = Player;
        this.bids = bids;
    }



    private int Player;
    private Hand[] bids;



    public void ChangePlayer(int Player){
        this.Player = Player;
    }

    public void ChangeBids(Hand[] bids){
        this.bids = bids;
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

    public boolean isColorHigher(int Player){
        //Suit Ids vergleichen
        //int Player = Player der ausspielt
        if(Player == 0){
            if (bids[Player].getLast().getSuitId() > bids[1].getLast().getSuitId()) {
                return true;
            }
            else{return false;}
        }
        else if(Player == 1){
            if (bids[Player].getLast().getSuitId() > bids[0].getLast().getSuitId()) {
                return true;
            }
            else{return false;}
        }
        return false;

    }
    public boolean isRankHigher(int Player){
        //int Player = Player der ausspielt
        if(Player == 0){
            if (bids[Player].getLast().getRankId() > bids[1].getLast().getRankId()) {
                return true;
            }
            else{return false;}
        }
        else if(Player == 1){
            if (bids[Player].getLast().getRankId() > bids[0].getLast().getRankId()) {
                return true;
            }
            else{return false;}
        }
        return false;
    }


    /*

//Momentan durchsucht es das Hand Array,
//Muss aber die Hände dursuchen

    private boolean sameColorOnHand(int Player) {
        //int Player = Player der ausspielt
        if (Player == 0) {
            if (isTrumpf(Player)) {
                if(TrumphOnHand(hands, 1)){return true;}
                else{return false;}
            }
            else{
                for(int i = 4; i < 8; i++){
                    if(hands[i].getLast().getSuitId() == bids[Player].getLast().getSuitId()){
                        return true;}
                }
            }
        }
        else if (Player == 1) {
            if (isTrumpf(Player)) {
                if(TrumphOnHand(hands, 0)){return true;}
                else{return false;}
            }
            else{
                for(int i = 0; i < 4; i++){
                    if(hands[i].getLast().getSuitId() == bids[Player].getLast().getSuitId()){
                        return true;}
                }
            }
        }
    return false;
    }
    private boolean TrumphOnHand(Hand[] hand, int Player){
        //int Player = Player dessen Deck abgeglichen wird
        if(Player == 0){
            //Decks durchgehen und checken ob  Rank/Suit Id mit Ober/Unter/herzübereinstimmen
            for(int i = 0; i < 4; i++){
                if(hands[i].getLast().getRankId()==4){
                    return true;
                }
                else if(hands[i].getLast().getRankId()==5){
                    return true;
                }
                else if(hands[i].getLast().getSuitId()==3){
                    return true;
                }
                else{return false;}
            }
        }
        else if(Player == 1){
            for(int i = 4; i < 8; i++){
                if(hands[i].getLast().getRankId()==4){
                    return true;
                }
                else if(hands[i].getLast().getRankId()==5){
                    return true;
                }
                else if(hands[i].getLast().getSuitId()==3){
                    return true;
                }
                else{return false;}
            }
        }
        return false;
    }

*/

}
