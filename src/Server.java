import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Server {
    static final int PORT = 1000;

    public static void main(String[] args) {
        Vector <Client> clients = new Vector<>();
        List<Lobby> lobbies = new ArrayList<>();

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            new LobbyHandlerThread(lobbies, clients).start();

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
