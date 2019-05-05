import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    public static String hash;
    public static Socket userSocket;
    public static DataInputStream userInput;
    public static DataOutputStream userOutput;
    private static final int BASE = 36;
    private static final int PORT = 8000;
    private static final int MAX_SIZE = 8;
    private static final int RANGE_SIZE = 1000000;
    public static void main(String[] args) throws IOException {
        Generator generator = new Generator(BASE , MAX_SIZE , RANGE_SIZE);
        ServerSocket server = new ServerSocket(PORT);

        userSocket = server.accept();

        userInput = new DataInputStream(userSocket.getInputStream());
        userOutput = new DataOutputStream(userSocket.getOutputStream());

        hash = userInput.readUTF();

        while(true) {
            System.out.println("[PENDING]: Waiting for Connection");
            Thread client = new ClientHandler(server, generator);
            client.start();
        }
    }

    public static void finish() throws IOException {
        userSocket.close();
        userInput.close();
        userOutput.close();
        System.exit(0);
    }
}