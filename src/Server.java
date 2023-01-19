import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * It creates a server that listens for connections on port 1000.
 */
public class Server {
    static final int PORT = 1000;
    static volatile List<Lobby> lobbies = new ArrayList<Lobby>();

    /**
     * The server starts a new thread for each client that connects to it
     */
    public static void main(String[] args) {
        Vector <Client> clients = new Vector<>();
        ListLobbies listLobbies = new ListLobbies(lobbies);

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            System.out.println("\nCommands: /quit, /end_game <lobby number>\n");
            new ServerReadInput(lobbies).start();

            new LobbyHandlerThread(lobbies, clients).start();
            System.out.println(listLobbies.getLobbiesString());

            while (true) {
                Socket socket = serverSocket.accept();
                clients.add(new Client(socket));

                new ClientThread(lobbies, clients, clients.size() - 1).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
