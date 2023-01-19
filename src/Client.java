import java.net.Socket;

/**
 * A Client object is a representation of a client that is connected to the server
 */
public class Client {
    public String name;
    Socket socket;
    public int currentLobbyIndex;
    public Boolean isPlaying;

    public Client(Socket socket) {
        this.socket = socket;
        this.isPlaying = false;
        this.currentLobbyIndex = -1;
    }


}
