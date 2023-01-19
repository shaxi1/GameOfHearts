import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * It creates a class called Player, that represents a player in the game of hearts.
 */
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

    /**
     * This function removes a card from the player's hand if it exists in the hand
     *
     * @param card the card to be played
     * @return Boolean
     */
    public Boolean playCard(Card card) {
        for (int i = 0; i < hand.size(); i++) {
            if (Objects.equals(hand.get(i).suit, card.suit) && Objects.equals(hand.get(i).symbol, card.symbol)) {
                hand.remove(i);
                return true;
            }
        }

        return false;
    }

    /**
     * This function empties the hand of the player.
     */
    public void emptyHand() {
        this.hand = new ArrayList<>();
    }
}
