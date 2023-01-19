import java.io.BufferedReader;

/**
 * This class reads a line from the server and prints it to the console
 */
public class ClientRecieveMsgThread extends Thread{
    BufferedReader serverReader;

    public ClientRecieveMsgThread(BufferedReader serverReader) {
        this.serverReader = serverReader;
    }

    /**
     * This function reads a line from the server and prints it to the console.
     */
    public void run() {
        try {
            while (true) {
                String msg = serverReader.readLine();
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
