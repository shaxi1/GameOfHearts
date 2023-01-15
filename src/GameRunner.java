public class GameRunner {
    private final int playerCount = 4; /* min and max */

    public int gameIndex;
    private int playersInLobby;
    private Lobby lobby;

    private Player[] players;
    private int turn;
    private int lastStartingPlayer;
    private int currentPlayer;

    public GameRunner(int gameIndex) {
        this.gameIndex = 0;
        this.playersInLobby = 0;
        this.lobby = new Lobby(gameIndex);

        this.turn = 0;
        this.lastStartingPlayer = -1;
        this.currentPlayer = -1;
    }

    public void addPlayer(String playerName) {
        Player player = new Player(playerName, playersInLobby);
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = player;
                playersInLobby++;
                break;
            }
        }

        lobby.addPlayer(player);
        if (playersInLobby == playerCount) {
            lobby.gameStarted = true;
            startGame();
        }
    }

    public void removePlayer(String playerName) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].name.equals(playerName)) {
                players[i] = null;
                playersInLobby--;

                lobby.removePlayer(playerName);
                break;
            }
        }
    }

    public void startGame() {

    }

}
