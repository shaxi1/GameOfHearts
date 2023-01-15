import java.util.Objects;

public class Lobby {
    private final int playerCount = 4; /* min and max */

    public int lobbyIndex; /* same as gameIndex */
    public GameRunner gameRunner;

    public String[] playerNames;
    public int playersInLobby;
    Boolean gameStarted;
    Boolean gameFinished;
    String winnerName;

    public Lobby(int lobbyIndex) {
        this.lobbyIndex = lobbyIndex;
        this.playerNames = new String[playerCount];
        this.gameRunner = new GameRunner(lobbyIndex);

        this.playersInLobby = 0;
        this.gameStarted = false;
        this.gameFinished = false;
    }

    public void addPlayer(String playerName) {
        for (int i = 0; i < playerNames.length; i++) {
            if (playerNames[i] == null) {
                playerNames[i] = playerName;
                playersInLobby++;

                gameRunner.addPlayer(playerName);
                break;
            }
        }
    }

    public void removePlayer(String playerName) {
        for (int i = 0; i < playerNames.length; i++) {
            if (Objects.equals(playerNames[i], playerName)) {
                playerNames[i] = null;
                playersInLobby--;

                gameRunner.removePlayer(playerName);
                break;
            }
        }
    }

    public String playCard(String playerName, Card card) {
        if (!gameStarted) {
            return "Game haven't started yet";
        }

        if(!gameRunner.playCard(playerName, card)) {
            return "You don't have this card";
        }

        return "Card played";
    }

    public Player startGame() {
        gameStarted = true;
        Player winner = gameRunner.startGame();

        winnerName = winner.name;
        gameFinished = true;
        return winner;
    }

}
