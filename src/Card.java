import java.util.Arrays;

public class Card {
    String symbol;
    final String[] faceValue = new String[] {
            "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"
    };

    String suit;
    int cardIndex;

    public Card(String suit, String symbol) {
        this.suit = suit;
        this.symbol = symbol;

        this.cardIndex = Arrays.asList(faceValue).indexOf(symbol);
    }
}
