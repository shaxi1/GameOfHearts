import java.io.*;

import java.util.List;
import java.util.Vector;

/**
 * This class is a thread that handles a client connection.
 */
public class ClientThread extends Thread {
    private Vector<Client> clients;
    private List<Lobby> lobbies;
    private Client client;
    private int clientIndex;

    private InputStream inputStream;
    private BufferedReader clientRead;
    private PrintWriter clientWrite;



    public ClientThread(List<Lobby> lobbies, Vector<Client> clients, int clientIndex) {
        this.clients = clients;
        this.lobbies = lobbies;

        this.clientIndex = clientIndex;
        this.client = clients.get(clientIndex);
    }

    /**
     * The function reads the client's input and parses it to see if it's a command. If it is, it handles the command
     */
    public void run() {
        try {
            MessageHandler messageParser = new MessageHandler();
            this.inputStream = client.socket.getInputStream();
            this.clientRead = new BufferedReader(new InputStreamReader(inputStream));
            this.clientWrite = new PrintWriter(client.socket.getOutputStream(), true);

            while (true) {
                String username = askForUsername();
                if (username != null) {
                    client.name = username;
                    break;
                }
                clientWrite.println("Username already taken!");
            }


            clientWrite.println("Welcome to the server, " + client.name + "!");
            ListLobbies listLobbies = new ListLobbies(lobbies);
            String lobbiesString = listLobbies.getLobbiesString();
            clientWrite.println(lobbiesString);
            clientWrite.println("Commands: /join <lobby number>, /quit, /leave_lobby, /say | In game: /play <card name card suit>");

            while (true) {
                String message = clientRead.readLine();
                if (message == null) {

                    if (client.currentLobbyIndex != -1)
                        leaveLobby(client, lobbies);

                    // remove client from clients vector by name
                    for (int i = 0; i < clients.size(); i++) {
                        if (clients.get(i).name.equals(client.name)) {
                            clients.remove(i);
                            break;
                        }
                    }
                    break;
                }

                messageParser.handleCommand(message, client, lobbies, clients, clientWrite);

                messageParser.handlePlayCommand(message, client, lobbies, clients, clientWrite);
            }


            //clients.remove(clientIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ask the user for a username, and if it's not taken, return it.
     *
     * @return The username that the client has entered.
     */
    private String askForUsername() throws IOException {
        String message = "Please enter a username: ";
        this.clientWrite.println(message);
        String username = this.clientRead.readLine();

        /* check if username exists */
        for (Client client : clients) {
            if (client.name == null)
                continue;
            if (client.name.equals(username))
                return null;
        }

        return username;
    }

    /**
     * It removes a player from a lobby
     *
     * @param client The client that is leaving the lobby
     * @param lobbies A list of all the lobbies
     */
    private void leaveLobby(Client client, List<Lobby> lobbies) {
        Lobby lobby = lobbies.get(client.currentLobbyIndex);
        lobby.playersInLobby--;
        lobby.removePlayer(client.name);
        client.isPlaying = false;
    }

}
