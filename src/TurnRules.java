import java.util.Objects;

/**
 * The `TurnRules` class is a collection of rules that determine rules for each of the turns in game of hearts.
 */
public class TurnRules {
    private final int lastPile = 12;
    private int turnNumber;

    public TurnRules(int turnNumber) {
        this.turnNumber = turnNumber;
    }


    /**
     * > It counts points based on the cards in the pile and turn number.
     *
     * @param turnNumber The turn number of the game.
     * @param endingPile The cards that are in the pile at the end of the turn.
     * @param pileTakenNumber The number of the pile that was taken.
     * @return The number of points that the player has earned.
     */
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

    /**
     * > This function returns the number of cards in the ending pile
     * In turn one taking a pile is worth -20 points.
     *
     * @param endingPile The cards that are in the ending pile.
     */
    private int turnOneCount(Card[] endingPile) {
        return -20;
    }

    /**
     * If the ending pile contains any hearts, subtract 20 points
     *
     * @param endingPile The cards that are in the ending pile.
     * @return The number of points that the player has.
     */
    private int turnTwoCount(Card[] endingPile) {
        int points = 0;
        for (Card card : endingPile) {
            if (card.suit.equals("Hearts"))
                points-=20;
        }
        return points;
    }

    /**
     * If the ending pile contains a Queen, subtract 60 points
     *
     * @param endingPile The array of cards that are in the ending pile.
     * @return The points for the ending pile.
     */
    private int turnThreeCount(Card[] endingPile) {
        int points = 0;
        for (Card card : endingPile) {
            if (card.cardIndex == card.getCardfaceVaulueIndex("Q"))
                points -= 60;
        }
        return points;
    }

    /**
     * If the ending pile contains a King or a Jack, subtract 30 points
     *
     * @param endingPile The array of cards that are in the ending pile.
     * @return The method is returning the points that the player has earned.
     */
    private int turnFourCount(Card[] endingPile) {
        int points = 0;
        for (Card card : endingPile) {
            if (card.cardIndex == card.getCardfaceVaulueIndex("K") || card.cardIndex == card.getCardfaceVaulueIndex("J"))
                points -= 30;
        }
        return points;
    }

    /**
     * If the ending pile contains a King of Hearts, subtract 150 points
     *
     * @param endingPile The array of cards that are in the ending pile.
     * @return The points for the turn five count.
     */
    private int turnFiveCount(Card[] endingPile) {
        int points = 0;
        for (Card card : endingPile) {
            if (card.cardIndex == card.getCardfaceVaulueIndex("K") && card.suit.equals("Hearts"))
                points -= 150;
        }
        return points;
    }

    /**
     * If the player takes the last pile or the 7th pile, they subtract 75 points
     *
     * @param endingPile The pile that the player took from.
     * @param pileTakenNumber the pile number that the player took
     * @return The points that the player gets for taking the pile.
     */
    private int turnSixCount(Card[] endingPile, int pileTakenNumber) {
        int points = 0;
        if (pileTakenNumber == 7 || pileTakenNumber == lastPile)
            points -= 75;
        return points;
    }

    /**
     * > This function combines all rules for turn seven.
     *
     * @param endingPile The pile of cards that the player has at the end of the game.
     * @param pileTakenNumber the number of the pile that was taken
     * @return The total points of the player.
     */
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

    /**
     * The function takes in an array of cards and an opening card, and returns the index of the player who takes the pile
     *
     * @param cardPile an array of cards that were played in the round
     * @param openingCard The card that was played first in the pile.
     * @return The index of the player who takes the pile.
     */
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
