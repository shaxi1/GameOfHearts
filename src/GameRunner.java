import java.io.PrintWriter;

/**
 * A class that runs the game of hearts.
 */
public class GameRunner {
    private final int playerCount = 4; /* min and max */
    private final int turnCount = 7;
    private final int handSize = 13;
    private final int maxPileSize = 4;

    public int gameIndex;
    private int playersInLobby;

    private Player[] players;
    private volatile int turn;
    // Used to keep track of the player that starts the turn and the player that is currently playing.
    private int lastStartingPlayer;
    private int currentPlayer;

    private Boolean gameStarted;

    // volatile to make sure the changes are visible to all threads
    private volatile Boolean cardPlayed;
    // An array of cards that are played in one cycle.
    private Card[] cardPile;
    // Used to store the first card that is played in a turn.
    private volatile Card openingCard;

    private PrintWriter[] clientWriter;

    public GameRunner(int gameIndex) {
        this.gameIndex = 0;
        gameStarted = false;
        this.players = new Player[playerCount];
        this.clientWriter = new PrintWriter[playerCount];
        this.playersInLobby = 0;

        this.turn = -1;
        this.lastStartingPlayer = -1;
        this.currentPlayer = -1;
    }

    /**
     * It adds a player to the game
     *
     * @param playerName The name of the player that is joining the lobby.
     * @param clientWriter The PrintWriter that is used to send messages to the client.
     */
    public void addPlayer(String playerName, PrintWriter clientWriter) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = new Player(playerName, i);
                this.clientWriter[i] = clientWriter;

                playersInLobby++;
                break;
            }
        }
    }

    /**
     * It removes a player from the game, called when a player leaves the lobby.
     * If the player exists, set the player to null and decrement the number of players in the lobby.
     *
     * @param playerName The name of the player to be removed.
     */
    public void removePlayer(String playerName) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].name.equals(playerName)) {
                players[i] = null;
                this.clientWriter[i] = null;

                playersInLobby--;
                break;
            }
        }
    }

    /**
     * The game starts, the dealer deals cards every cycle, the players play cards, the pile taker is determined,
     * the pile taker gets points based on rules of the current turn, the pile taker starts the next round,
     * and the game ends
     *
     * @return The winner of the game.
     */
    public Player startGame() {
        gameStarted = true;
        cardPile = new Card[playerCount];
        VirtualDealer virtualDealer = new VirtualDealer();
        gameStartNotify();

        for (int i = 1; i <= turnCount; i++) {
            turn = i;
            TurnRules turnRules = new TurnRules(turn);
            lastStartingPlayer = virtualDealer.dealCards(players, lastStartingPlayer);
            currentPlayer = lastStartingPlayer;

            for (int j = 0; j < handSize; j++) {
                for (int z = 0; z < playerCount; z++) {
                    sendPileMsg();
                    sendYourTurnMsg(currentPlayer);
                    sendYourHandMsg();

                    this.cardPlayed = false;
                    while(true) {
                        if (this.cardPlayed)
                            break;
                        // wait for card to be played (var set by playCard called in MessageHandler)
                    }

                    currentPlayer = (currentPlayer + 1) % playerCount;
                }

                int pileTakerIndex = turnRules.whoTakesPile(cardPile, openingCard);
                int points = turnRules.calculatePoints(turn, cardPile, j);
                players[pileTakerIndex].score += points;

                // pileTaker starts
                currentPlayer = pileTakerIndex;
                cardPile = new Card[playerCount];
            }

        }

        return getWinner();
    }


    /**
     * "Send each player their hand."
     *
     * The first thing we do is create a StringBuilder object. This is a class that allows us to build a string by
     * appending characters to it. We'll use this to build a string that contains the cards in each player's hand
     */
    private void sendYourHandMsg() {
        for (int i = 0; i < playerCount; i++) {
            StringBuilder hand = new StringBuilder();
            int j = 0;
            for (Card card : players[i].hand) {
                hand.append(card.symbol).append(" of ").append(card.suit).append(", ");

                j++;
                if (j == handSize/2)
                    hand.append("\n");
            }

            clientWriter[i].println("\nYour hand:\n" + hand);
        }
    }

    /**
     * This function sends a message to all players that displays the cards in the pile
     */
    private void sendPileMsg() {
        if (isCardPileEmpty())
            return;
        StringBuilder pileMsg = new StringBuilder("\n\nCards in play: ");
        for (int i = 0; i < maxPileSize; i++) {
            if (cardPile[i] != null)
                pileMsg.append(cardPile[i].symbol).append(" of ").append(cardPile[i].suit).append(", ");
        }

        for (int i = 0; i < players.length; i++) {
            clientWriter[i].println(pileMsg);
        }
    }

    /**
     * This function sends a message to all players that the game has started.
     */
    private void gameStartNotify() {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                clientWriter[i].println("Game has started");
            }
        }
    }

    /**
     * This function sends a message to the player whose turn it is to play a card
     *
     * @param playerIndex the index of the player whose turn it is
     */
    private void sendYourTurnMsg(int playerIndex) {
        clientWriter[playerIndex].println("\nIt's your turn, play <name suit>");
    }

    private Player getWinner() {
        Player winner = players[0];
        for (int i = 1; i < players.length; i++) {
            if (players[i].score > winner.score) {
                winner = players[i];
            }
        }

        return winner;
    }


    /**
     * "Play a card for the current player."
     *
     * The function is called by the client when the player wants to play a card. The function checks to see if
     * the player is the current player, and if so, it plays the card using Player class's playCard function.
     *
     * @param playerName The name of the player who is playing the card.
     * @param card The card that the player wants to play.
     * @return A boolean value.
     */
    public Boolean playCard(String playerName, Card card) {
        Player player = getPlayerByName(playerName);
        if (player == null)
            return false;

        if (currentPlayer != player.playerIndex)
            return false;

        Boolean played = player.playCard(card);
        if (!played)
            return false;

        if (isCardPileEmpty())
            this.openingCard = card;
        cardPile[currentPlayer] = card;
        this.cardPlayed = true;

        return true;
    }

    /**
     * If any card in the card pile is not null, return false, otherwise return true.
     *
     * @return A boolean value.
     */
    private Boolean isCardPileEmpty() {
        for (Card card : cardPile) {
            if (card != null)
                return false;
        }
        return true;
    }

    /**
     * Return the player with the given name, or null if no such player exists.
     *
     * @param playerName The name of the player to get.
     * @return The player object with the name that matches the playerName parameter.
     */
    private Player getPlayerByName(String playerName) {
        for (Player player : players)
            if (player.name.equals(playerName))
                return player;

        return null;
    }
}
