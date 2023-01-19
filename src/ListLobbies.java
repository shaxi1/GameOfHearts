import java.util.List;

/**
 * This class is used to list all the lobbies that are currently available
 */
public class ListLobbies {
    public List<Lobby> lobbies;

    public ListLobbies(List<Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    /**
     * It returns a string that contains the names of all the players in all the lobbies
     *
     * @return A string containing the names of the players in each lobby.
     */
    public String getLobbiesString() {
        StringBuilder lobbiesString = new StringBuilder("Lobbies: " + "\n");
        for (int i = 0; i < lobbies.size(); i++) {
            lobbiesString.append("\nLobby number ").append(i);
            if (lobbies.get(i).gameStarted)
                lobbiesString.append(" (ingame)");
            lobbiesString.append(":\n");

            for (int j = 0; j < lobbies.get(i).playerNames.length; j++) {
                lobbiesString.append(j).append(". ");
                if (lobbies.get(i).playerNames[j] != null)
                    lobbiesString.append(lobbies.get(i).playerNames[j]);

                lobbiesString.append("\n");
            }
        }

        return lobbiesString.toString();
    }
}
