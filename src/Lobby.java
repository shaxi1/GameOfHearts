import java.util.Objects;

public class Lobby {
    private final int playerCount = 4; /* min and max */

    public int lobbyIndex; /* same as gameIndex */
    public GameRunner gameRunner;

    private String[] playerNames;
    private int playersInLobby;
    Boolean gameStarted;

    public Lobby(int lobbyIndex) {
        this.lobbyIndex = lobbyIndex;
        this.playerNames = new String[playerCount];
        this.gameRunner = new GameRunner(lobbyIndex);

        this.playersInLobby = 0;
        this.gameStarted = false;
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

    public void startGame() {
        gameStarted = true;
        gameRunner.startGame();
    }

}
