import java.io.*;
import java.net.*;
import java.util.Scanner;

public class User {

    private final static int PORT = 8000;
    public static void main(String[] args) throws IOException {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter server IP:");
            String SERVERIP = sc.nextLine();
            System.out.println("Enter password MD5 hash:");
            String hash = sc.nextLine();

            Socket socket = new Socket(SERVERIP, PORT);

            DataInputStream userInput = new DataInputStream(socket.getInputStream());
            DataOutputStream userOutput = new DataOutputStream(socket.getOutputStream());

            userOutput.writeUTF(hash);

            String password = userInput.readUTF();

            System.out.println("Password: " + password);

            sc.close();
            socket.close();
        } catch (SocketException e) {
            System.out.println("[FAILED]: Could not connect to server (Reason: Server is closed)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
