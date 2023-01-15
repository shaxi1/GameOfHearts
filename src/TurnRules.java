import java.util.Objects;

public class TurnRules {
    private final int lastPile = 12;
    private int turnNumber;

    public TurnRules(int turnNumber) {
        this.turnNumber = turnNumber;
    }


    public int calculatePoints(int turnNumber, Card[] endingPile, int pileTakenNumber) {
        int points = 0;

        if (turnNumber == 1)
            return turnOneCount(endingPile);
        else if (turnNumber == 2)
            return turnTwoCount(endingPile);
        else if (turnNumber == 3)
            return turnThreeCount(endingPile);
        else if (turnNumber == 4)
            return turnFourCount(endingPile);
        else if (turnNumber == 5)
            return turnFiveCount(endingPile);
        else if (turnNumber == 6)
            return turnSixCount(endingPile, pileTakenNumber);
        else if (turnNumber == 7)
            return turnSevenCount(endingPile, pileTakenNumber);

        return points;
    }

    // -20 for each pile taken
    private int turnOneCount(Card[] endingPile) {
        return -20;
    }

    // -20 for each heart taken
    private int turnTwoCount(Card[] endingPile) {
        int points = 0;
        for (Card card : endingPile) {
            if (card.suit.equals("Hearts"))
                points-=20;
        }
        return points;
    }

    // -60 for each queen taken
    private int turnThreeCount(Card[] endingPile) {
        int points = 0;
        for (Card card : endingPile) {
            if (card.cardIndex == card.getCardfaceVaulueIndex("Q"))
                points -= 60;
        }
        return points;
    }

    // -30 for each king or jack taken
    private int turnFourCount(Card[] endingPile) {
        int points = 0;
        for (Card card : endingPile) {
            if (card.cardIndex == card.getCardfaceVaulueIndex("K") || card.cardIndex == card.getCardfaceVaulueIndex("J"))
                points -= 30;
        }
        return points;
    }

    // -150 for taking king of hearts
    private int turnFiveCount(Card[] endingPile) {
        int points = 0;
        for (Card card : endingPile) {
            if (card.cardIndex == card.getCardfaceVaulueIndex("K") && card.suit.equals("Hearts"))
                points -= 150;
        }
        return points;
    }

    // -75 for 7th and last pile taken
    private int turnSixCount(Card[] endingPile, int pileTakenNumber) {
        int points = 0;
        if (pileTakenNumber == 7 || pileTakenNumber == lastPile)
            points -= 75;
        return points;
    }

    // all turn rules combined
    public int turnSevenCount(Card[] endingPile, int pileTakenNumber) {
        int points = 0;
        points += turnOneCount(endingPile);
        points += turnTwoCount(endingPile);
        points += turnThreeCount(endingPile);
        points += turnFourCount(endingPile);
        points += turnFiveCount(endingPile);
        points += turnSixCount(endingPile, pileTakenNumber);
        return points;
    }

    public int whoTakesPile(Card[] cardPile, Card openingCard) {
        int winnerIndex = -1;
        Card winnerCard = openingCard;
        for (int i = 0; i < cardPile.length; i++) {
            if (cardPile[i] == openingCard)
                winnerIndex = i;
        }

        for (int i = 0; i < cardPile.length; i++) {
            if (Objects.equals(cardPile[i].suit, openingCard.suit) && cardPile[i].cardIndex > winnerCard.cardIndex) {
                winnerCard = cardPile[i];
                winnerIndex = i;
            }
        }

        return winnerIndex;
    }
}
