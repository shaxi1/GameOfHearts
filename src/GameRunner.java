import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GameRunner {
    private final int playerCount = 4; /* min and max */
    private final int turnCount = 7;
    private final int handSize = 13;

    public int gameIndex;
    private int playersInLobby;

    private Player[] players;
    private int turn;
    private int lastStartingPlayer;
    private int currentPlayer;
    private Boolean gameStarted;

    private volatile Boolean cardPlayed; // volatile to make sure the changes are visible to all threads
    private Card[] cardPile;
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
                    // TODO: send msg to client
                    sendPileMsg();
                    sendYourTurnMsg(currentPlayer);

                    this.cardPlayed = false;
                    while(!this.cardPlayed) {
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

    private void sendPileMsg() {
        if (isCardPileEmpty())
            return;
        StringBuilder pileMsg = new StringBuilder("Cards in play: ");
        for (Card card : cardPile) {
            pileMsg.append(card.symbol).append(" of ").append(card.suit).append(", ");
        }

        for (int i = 0; i < players.length; i++) {
            clientWriter[i].println(pileMsg);
        }
    }

    private void gameStartNotify() {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                clientWriter[i].println("Game has started");
            }
        }
    }

    private void sendYourTurnMsg(int playerIndex) {
        clientWriter[playerIndex].println("It's your turn, play <name suit>");
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


    public Boolean playCard(String playerName, Card card) {
        // TODO: W rozdaniach 2., 5. i 7. nie można wychodzić w kiery, jeśli ma się karty innego koloru.
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

    private Boolean isCardPileEmpty() {
        for (Card card : cardPile) {
            if (card != null)
                return false;
        }
        return true;
    }

    private Player getPlayerByName(String playerName) {
        for (Player player : players)
            if (player.name.equals(playerName))
                return player;

        return null;
    }
}
