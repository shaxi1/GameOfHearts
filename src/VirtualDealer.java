import java.util.ArrayList;
import java.util.List;

// randomize players' hands
public class VirtualDealer {
    final private int handSize = 13;
    final private int playerCount = 4;

    private List<Card> deck;

    public VirtualDealer() {
    }

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

    public void dealCards(Player[] players, int lastStartingPlayer) {
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
    }

    private void setStartVarToFalse(Player[] players, int ignoreIndex) {
        for (int i = 0; i < players.length; i++)
            if (i != ignoreIndex)
                players[i].starts = false;
    }
}
