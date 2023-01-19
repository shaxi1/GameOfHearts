import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

/**
 * This class is responsible for handling messages
 */
public class MessageHandler {
    final int maxPlayers = 4;

    public MessageHandler() {

    }

    /**
     * If the message is a server command handle it, otherwise leave it to the handlePlayCommand method
     *
     * @param message the message sent by the client
     * @param client the client that sent the message
     * @param lobbies a list of all the lobbies
     * @param clients a vector of all clients connected to the server
     * @param clientWrite the PrintWriter object of the client that sent the message
     */
    public void handleCommand(String message, Client client, List<Lobby> lobbies, Vector<Client> clients, PrintWriter clientWrite) throws IOException {
        if (message == null)
            return;

        if (message.startsWith("/join")) {
            String[] messageParts = message.split(" ");
            if (messageParts.length != 2) {
                clientWrite.println("Invalid command!");
                return;
            }

            int lobbyIndex = Integer.parseInt(messageParts[1]);
            if (lobbyIndex >= lobbies.size()) {
                clientWrite.println("Lobby does not exist!");
                return;
            }

            Lobby lobby = lobbies.get(lobbyIndex);
            if (lobby.playersInLobby == maxPlayers) {
                clientWrite.println("Lobby is full!");
                return;
            }

            if (client.currentLobbyIndex != -1) {
                clientWrite.println("You are already in a game!");
                return;
            }

            client.currentLobbyIndex = lobbyIndex;
            lobby.addPlayer(client.name, clientWrite);
            sendRefreshedLobbiesString(lobbyIndex, lobbies, client.name);
            clientWrite.println("You have joined lobby " + lobbyIndex + "!");
            clientWrite.println("Waiting for other players to join...");
        } else if (message.startsWith("/leave_lobby")) {
            if (client.currentLobbyIndex == -1) {
                clientWrite.println("You are not in a lobby!");
                return;
            }

            sendRefreshedLobbiesString(client.currentLobbyIndex, lobbies, null);
            leaveLobby(client, lobbies);
            clientWrite.println("You left the lobby!");
        } else if (message.startsWith("/quit")) {
            clientWrite.println("Goodbye!");

            if (client.currentLobbyIndex != -1) {
                sendRefreshedLobbiesString(client.currentLobbyIndex, lobbies, null);
                leaveLobby(client, lobbies);
            }
            // remove client from clients vector by name
            for (int i = 0; i < clients.size(); i++) {
                if (clients.get(i).name.equals(client.name)) {
                    clients.remove(i);
                    break;
                }
            }
        } else if (message.startsWith("/say")) {
            // client sends a msg to global chat
            String[] messageParts = message.split(" ");
            if (messageParts.length < 2) {
                clientWrite.println("Invalid command, enter message!");
                return;
            }

            StringBuilder msg = new StringBuilder();
            for (int i = 1; i < messageParts.length; i++) {
                msg.append(messageParts[i]).append(" ");
            }
            msg = new StringBuilder(msg.substring(0, msg.length() - 1));
            for (int i = 0; i < clients.size(); i++) {
                Socket socket = clients.get(i).socket;
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                if (client.name.equals(clients.get(i).name))
                    writer.println("You said: " + msg);
                else
                    writer.println(client.name + ": " + msg);
            }
        } else if (!message.startsWith("play")) {
            clientWrite.println("Invalid command!");
        }
    }

    /**
     * It removes the player from the lobby and sets the client's isPlaying flag to false
     *
     * @param client The client that is leaving the lobby
     * @param lobbies A list of all the lobbies.
     */
    private void leaveLobby(Client client, List<Lobby> lobbies) {
        Lobby lobby = lobbies.get(client.currentLobbyIndex);
        client.currentLobbyIndex = -1;
        lobby.removePlayer(client.name);
        client.isPlaying = false;
    }

    /**
     * If the client is in a lobby, then play the card that the client specified
     *
     * @param message the message that the client sent to the server
     * @param client The client that sent the message
     * @param lobbies a list of all the lobbies
     * @param clients a vector of all the clients connected to the server
     * @param clientWrite the PrintWriter object that is used to send messages to the client
     */
    public void handlePlayCommand(String message, Client client, List<Lobby> lobbies, Vector<Client> clients, PrintWriter clientWrite) {
        if (message == null)
            return;

        if (message.startsWith("play")) {
            if (client.currentLobbyIndex == -1) {
                clientWrite.println("You are not in a lobby!");
                return;
            }

            // get card name from message
            String[] messageParts = message.split(" ");
            if (messageParts.length != 3) {
                clientWrite.println("Invalid command, should be play <name suit>!");
                return;
            }

            String cardSymbol = messageParts[1];
            String cardSuit = messageParts[2];

            Card card = new Card(cardSuit, cardSymbol);

            Lobby lobby = lobbies.get(client.currentLobbyIndex);
            String playMsg = lobby.playCard(client.name, card);
            clientWrite.println(playMsg);
        }
    }

    /**
     * It takes a list of lobbies, and sends a string representation of that list to all players in a specific lobby,
     * except for one player
     *
     * @param lobbyIndex The index of the lobby that the player is in.
     * @param lobbies The list of lobbies
     * @param skipName The name of the player who sent the message. We don't want to send the message back to them.
     */
    private void sendRefreshedLobbiesString(int lobbyIndex, List<Lobby> lobbies, String skipName) {
        ListLobbies listLobbies = new ListLobbies(lobbies);
        String lobbiesString = listLobbies.getLobbiesString();

        Lobby lobby = lobbies.get(lobbyIndex);
        for (int i = 0; i < lobby.playersInLobby; i++) {
            if (lobby.playerNames[i] != null && lobby.playerNames[i].equals(skipName))
                continue;
            if (lobby.clientWriter[i] != null)
                lobby.clientWriter[i].println(lobbiesString);
        }

        System.out.println(lobbiesString);
    }
}
