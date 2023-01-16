import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class LobbyHandlerThread extends Thread {
    final int maxPlayers = 4;

    volatile List <Lobby> lobbies;
    Vector <Client> clients;

    public LobbyHandlerThread(List<Lobby> lobbies, Vector<Client> clients) {
        this.lobbies = lobbies;
        this.clients = clients;

        lobbies.add(new Lobby(lobbies.size()));
    }

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
            }
        }
    }

    private Boolean isEmptyLobby() {
        for (Lobby lobby : lobbies)
            if (lobby.playersInLobby == 0)
                return true;

        return false;
    }

    private Socket getSocketByName(String name) {
        for (Client client : clients)
            if (client.name.equals(name))
                return client.socket;
        return null;
    }

}
