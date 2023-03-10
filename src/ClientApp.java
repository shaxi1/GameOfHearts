import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * It creates a socket, connects to the server, and then creates a thread to read messages from the server and a loop to
 * read messages from the user and send them to the server
 */
public class ClientApp {
    private static final int PORT = 1000;

    public static void main(String[] args) {
        try {
            InetAddress address = InetAddress.getLocalHost();

            Socket socket = new Socket(address, PORT);
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverWriter = new PrintWriter(socket.getOutputStream(), true);

            new ClientRecieveMsgThread(serverReader).start();

            while (true) {
                String msg = new BufferedReader(new InputStreamReader(System.in)).readLine();
                serverWriter.println(msg);

                if (msg.equals("/quit")) {
                    socket.close();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
