public class Lobby {
    private final int playerCount = 4; /* min and max */

    public int lobbyIndex; /* same as gameIndex */

    private int playersInLobby;
    private Player[] players;
    Boolean gameStarted;

    public Lobby(int lobbyIndex) {
        this.lobbyIndex = lobbyIndex;

        this.playersInLobby = 0;
        this.gameStarted = false;
    }

    public void addPlayer(Player player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = player;
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
