import java.io.PrintWriter;
import java.util.Objects;

/**
 * It creates a lobby that will use GameRunner Class to run the game, when the lobby is full.
 */
public class Lobby {
    private final int playerCount = 4; /* min and max */

    public int lobbyIndex; /* same as gameIndex */
    public GameRunner gameRunner;
    public PrintWriter[] clientWriter;

    public String[] playerNames;
    public int playersInLobby;
    volatile Boolean gameStarted;
    volatile Boolean gameFinished;
    String winnerName;

    public Lobby(int lobbyIndex) {
        this.lobbyIndex = lobbyIndex;
        this.playerNames = new String[playerCount];
        this.clientWriter = new PrintWriter[playerCount];
        this.gameRunner = new GameRunner(lobbyIndex);

        this.playersInLobby = 0;
        this.gameStarted = false;
        this.gameFinished = false;
    }

    /**
     * If there's an empty spot in the array, add the player to the array and increment the number of players in the lobby.
     *
     * @param playerName The name of the player that is joining the lobby.
     * @param clientWrite The PrintWriter that the client is using to send messages to the server.
     */
    public void addPlayer(String playerName, PrintWriter clientWrite) {
        for (int i = 0; i < playerNames.length; i++) {
            if (playerNames[i] == null) {
                playerNames[i] = playerName;
                clientWriter[i] = clientWrite;
                playersInLobby++;

                gameRunner.addPlayer(playerName, clientWrite);
                break;
            }
        }
    }

    /**
     * "If the player is in the lobby, remove them from the lobby."
     *
     * The function is called when a player disconnects from the server
     *
     * @param playerName The name of the player to be removed.
     */
    public void removePlayer(String playerName) {
        for (int i = 0; i < playerNames.length; i++) {
            if (Objects.equals(playerNames[i], playerName)) {
                playerNames[i] = null;
                clientWriter[i] = null;
                playersInLobby--;

                gameRunner.removePlayer(playerName);
                break;
            }
        }
    }

    /**
     * If the game hasn't started yet, return a message saying so. Otherwise, if the player doesn't have the card, return a
     * message saying so. Otherwise, play the card
     *
     * @param playerName The name of the player who is playing the card
     * @param card The card that the player wants to play.
     * @return A string
     */
    public String playCard(String playerName, Card card) {
        if (!gameStarted) {
            return "Game haven't started yet";
        }

        if(!gameRunner.playCard(playerName, card)) {
            return "You don't have this card";
        }

        return "Card played";
    }

    /**
     * "Start the game, and return the winner."
     *
     * The function is doing too much. It's starting the game, and it's returning the winner
     *
     * @return The winner of the game.
     */
    public Player startGame() {
        gameStarted = true;
        Player winner = gameRunner.startGame();

        winnerName = winner.name;
        gameFinished = true;
        return winner;
    }

}
