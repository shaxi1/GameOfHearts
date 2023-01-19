import java.util.Arrays;

/**
 * It's a class that represents a card in a deck of cards
 */
public class Card {
    String symbol;
    final String[] faceValue = new String[] {
            "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"
    };

    String suit;
    int cardIndex;

    // A constructor that takes in two parameters, suit and symbol. It sets the suit and symbol of the card to the
    // parameters passed in. It also sets the cardIndex to the index of the symbol in the faceValue array.
    public Card(String suit, String symbol) {
        this.suit = suit;
        this.symbol = symbol;

        this.cardIndex = Arrays.asList(faceValue).indexOf(symbol);
    }

    /**
     * It returns the index of the face value of the card in the array of face values
     *
     * @param faceValue The face value of the card.
     * @return The index of the faceValue in the array.
     */
    public int getCardfaceVaulueIndex(String faceValue) {
        return Arrays.asList(this.faceValue).indexOf(faceValue);
    }
}
