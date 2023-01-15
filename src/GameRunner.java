public class GameRunner {
    private final int playerCount = 4; /* min and max */

    public int gameIndex;
    private int playersInLobby;

    private Player[] players;
    private int turn;
    private int lastStartingPlayer;
    private int currentPlayer;

    public GameRunner(int gameIndex) {
        this.gameIndex = 0;
        this.players = new Player[playerCount];
        this.playersInLobby = 0;

        this.turn = -1;
        this.lastStartingPlayer = -1;
        this.currentPlayer = -1;
    }

    public void addPlayer(String playerName) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = new Player(playerName, i);
                playersInLobby++;
                break;
            }
        }
    }

    public void removePlayer(String playerName) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].name.equals(playerName)) {
                players[i] = null;
                playersInLobby--;
                break;
            }
        }
    }

    public void startGame() {

    }

}
