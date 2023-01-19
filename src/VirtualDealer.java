import java.util.ArrayList;
import java.util.List;

/**
 * The VirtualDealer class is a class that represents a card dealer in a game of hearts.
 * it randomizes the cards in players' hands.
 */
public class VirtualDealer {
    final private int handSize = 13;
    final private int playerCount = 4;

    private List<Card> deck;

    public VirtualDealer() {
    }

    /**
     * We create a deck of cards by creating an array of suits and an array of face values, and then looping through each
     * suit and face value to create a new card
     */
    private void createDeck() {
        deck = new ArrayList<>();
        String[] suits = new String[] {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] faceValue = new String[] {
                "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"
        };

        for (String suit : suits) {
            for (String symbol : faceValue) {
                deck.add(new Card(suit, symbol));
            }
        }
    }

    /**
     * It creates a deck, deals cards to each player, and returns the index of the player who starts the game
     *
     * @param players The array of players in the game.
     * @param lastStartingPlayer The index of the player who started the last round.
     * @return The index of the player who starts the game.
     */
    public int dealCards(Player[] players, int lastStartingPlayer) {
        createDeck();

        for (Player player : players) {
            player.hand = new ArrayList<>();

            for (int i = 0; i < handSize; i++) {
                Card card = deck.get((int) (Math.random() * deck.size()));
                player.hand.add(card);
                deck.remove(card);
            }
        }

        int randomIndex;
        if (lastStartingPlayer == -1) {
            randomIndex = (int) (Math.random() * playerCount);
        } else {
            randomIndex = (lastStartingPlayer + 1) % playerCount;
        }
        players[randomIndex].starts = true;
        setStartVarToFalse(players, randomIndex);

        return randomIndex;
    }

    /**
     * Set all players' starts to false, except for the player at the given index.
     *
     * @param players The array of players
     * @param ignoreIndex The index of the player who is not going to be set to false.
     */
    private void setStartVarToFalse(Player[] players, int ignoreIndex) {
        for (int i = 0; i < players.length; i++)
            if (i != ignoreIndex)
                players[i].starts = false;
    }
}
