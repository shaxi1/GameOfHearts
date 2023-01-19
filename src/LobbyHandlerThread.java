import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

/**
 * > This class is a thread that handles the lobbies. It is responsible for
 * creating and deleting lobbies, and for moving players from
 * lobbies after the game is finished.
 */
public class LobbyHandlerThread extends Thread {
    final int maxPlayers = 4;

    volatile List <Lobby> lobbies;
    volatile Vector <Client> clients;

    public LobbyHandlerThread(List<Lobby> lobbies, Vector<Client> clients) {
        this.lobbies = lobbies;
        this.clients = clients;

        lobbies.add(new Lobby(lobbies.size()));
    }

    /**
     * If there are no empty lobbies, create a new one. If a game is finished, move the players out of the lobby and print
     * the winner. If there are enough players in a lobby, start the game
     */
    public synchronized void run() {
        while (true) {
            // if no empty lobbies create a new one
            if (!isEmptyLobby()) {
                lobbies.add(new Lobby(lobbies.size()));
            }
            // if game finished recreate the lobby
            for (Lobby lobby : lobbies) {
                if (lobby.gameFinished) {
                    String winnerName = lobby.winnerName;
                    movePlayersAndPrintWinner(lobby, winnerName);

                    lobbies.remove(lobby);
                    lobbies.add(new Lobby(lobbies.size()));
                }
            }

            // check if there are enough players to start a game
            for (Lobby lobby : lobbies) {
                if (lobby.playersInLobby == maxPlayers) {
                    lobby.startGame();
                }
            }

        }
    }

    /**
     * It takes a lobby and a winner name, and then it writes the winner name to all the players in the lobby
     * Also it moves the players out of the lobby
     *
     * @param lobby The lobby that the game is in.
     * @param winnerName The name of the player who won the game.
     */
    private void movePlayersAndPrintWinner(Lobby lobby, String winnerName) {
        for (int i = 0; i < lobby.playerNames.length; i++) {
            Socket socket;
            if (lobby.playerNames[i] != null) {
                String playerName = lobby.playerNames[i];
                socket = getSocketByName(playerName);

                // write winner name to clients
                PrintWriter clientWrite = null;
                try {
                    assert socket != null;
                    clientWrite = new PrintWriter(socket.getOutputStream(), true);
                    clientWrite.println("Game finished! Winner: " + winnerName);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lobby.removePlayer(lobby.playerNames[i]);
                Client temp = getClientByUsername(playerName);
                if (temp != null)
                    temp.currentLobbyIndex = -1;
            }
        }
    }

    /**
     * If there are no lobbies with 0 players, return false, otherwise return true.
     *
     * @return A boolean value.
     */
    private Boolean isEmptyLobby() {
        for (Lobby lobby : lobbies)
            if (lobby.playersInLobby == 0)
                return true;

        return false;
    }

    /**
     * Return the socket of the client with the given name.
     *
     * @param name The name of the client you want to get the socket of.
     * @return A socket.
     */
    private Socket getSocketByName(String name) {
        for (Client client : clients)
            if (client.name.equals(name))
                return client.socket;
        return null;
    }

    /**
     * Return the client with the given username, or null if no such client exists.
     *
     * @param username The username of the client that sent the message.
     * @return The client object that matches the username.
     */
    private Client getClientByUsername(String username) {
        for (Client client : clients)
            if (client.name.equals(username))
                return client;
        return null;
    }

}
