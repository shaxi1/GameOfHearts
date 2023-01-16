import java.net.Socket;

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
