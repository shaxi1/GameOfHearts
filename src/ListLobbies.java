import java.util.List;

public class ListLobbies {
    public List<Lobby> lobbies;

    public ListLobbies(List<Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    public String getLobbiesString() {
        StringBuilder lobbiesString = new StringBuilder("Lobbies: " + "\n");
        for (int i = 0; i < lobbies.size(); i++) {
            lobbiesString.append("\nLobby number ").append(i).append("\n");
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
