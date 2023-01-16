import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class Server {
    static final int PORT = 1000;
    public static Scanner scanner;

    public static void main(String[] args) {
        Vector <Client> clients = new Vector<>();
        List<Lobby> lobbies = new ArrayList<>();
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
