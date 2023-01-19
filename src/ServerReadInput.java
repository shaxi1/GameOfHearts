import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * This class is a thread that reads input from the server and handles the commands
 */
public class ServerReadInput extends Thread {
    final int maxPlayers = 4;

    private volatile List<Lobby> lobbies;

    public ServerReadInput(List<Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    /**
     * It reads a line from the standard input, and if it's `/quit`, it shuts the server. If it's `/end_game
     * <lobby_number>`, it sets the winner of the game to a random player in the lobby
     */
    public void run() {
        while (true) {
            try {
                String input = new BufferedReader(new InputStreamReader(System.in)).readLine();

                if (input.equals("/quit")) {
                    System.exit(0);
                } else if (input.startsWith("/end_game")) {
                    int lobbyNumber = Integer.parseInt(input.split(" ")[1]);
                    if (lobbies.get(lobbyNumber).playersInLobby != maxPlayers) {
                        System.out.println("Not enough players in lobby!");
                        continue;
                    }

                    // random winner
                    int randomPlayer = (int) (Math.random() * maxPlayers);
                    lobbies.get(lobbyNumber).winnerName = lobbies.get(lobbyNumber).playerNames[randomPlayer];
                    lobbies.get(lobbyNumber).gameFinished = true;

                    System.out.println("Game ended");
                } else {
                    System.out.println("Invalid command! Available commands: /quit, /end_game <lobby_number>");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
