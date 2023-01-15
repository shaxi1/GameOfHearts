import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    public String name;
    /*  indexes go as follows:
            2
           1 3
            0
    */
    public int playerIndex;
    public int score;

    public List<Card> hand;
    Boolean starts;
    Boolean myTurn;

    public Player(String name, int playerIndex) {
        this.name = name;
        this.score = 0;
        this.myTurn = false;
        this.playerIndex = playerIndex;

        this.hand = new ArrayList<>();
    }

    public Boolean playCard(Card card) { // remove card from hand and let gamerunner handle the rest earlier
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).equals(card)) {
                hand.remove(i);
                return true;
            }
        }

        return false;
    }

    public void emptyHand() {
        this.hand = new ArrayList<>();
    }
}
