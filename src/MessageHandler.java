import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;

public class MessageHandler {
    final int maxPlayers = 4;

    public MessageHandler() {

    }


    public void handleCommand(String message, Client client, List<Lobby> lobbies, Vector<Client> clients, PrintWriter clientWrite) {
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

            if (client.isPlaying) {
                clientWrite.println("You are already in a game!");
                return;
            }

            client.currentLobbyIndex = lobbyIndex;
            lobby.playersInLobby++;
            lobby.addPlayer(client.name, clientWrite);
            clientWrite.println("You have joined lobby " + lobbyIndex + "!");
            clientWrite.println("Waiting for other players to join...");
        } else if (message.startsWith("/leave_lobby")) {
            if (client.currentLobbyIndex == -1) {
                clientWrite.println("You are not in a lobby!");
                return;
            }

            leaveLobby(client, lobbies);
            clientWrite.println("You left the lobby!");
        } else if (message.startsWith("/quit")) {
            clientWrite.println("Goodbye!");

            if (client.currentLobbyIndex != -1)
                leaveLobby(client, lobbies);

            // remove client from clients vector by name
            for (int i = 0; i < clients.size(); i++) {
                if (clients.get(i).name.equals(client.name)) {
                    clients.remove(i);
                    break;
                }
            }
        } else if (!message.startsWith("play")) {
            clientWrite.println("Invalid command!");
        }
    }

    private void leaveLobby(Client client, List<Lobby> lobbies) {
        Lobby lobby = lobbies.get(client.currentLobbyIndex);
        lobby.playersInLobby--;
        lobby.removePlayer(client.name);
        client.isPlaying = false;
    }

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
                clientWrite.println("Invalid command!");
                return;
            }

            String cardName = messageParts[1];
            String cardSuit = messageParts[2];

            Card card = new Card(cardName, cardSuit);

            Lobby lobby = lobbies.get(client.currentLobbyIndex);
            String playMsg = lobby.playCard(client.name, card);
            clientWrite.println(playMsg);
        }
    }
}
