import java.io.BufferedReader;

public class ClientRecieveMsgThread extends Thread{
    BufferedReader serverReader;

    public ClientRecieveMsgThread(BufferedReader serverReader) {
        this.serverReader = serverReader;
    }

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
