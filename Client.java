import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;

class Client {
    private final static int PORT = 8000;
    private final static String HOST = "127.0.0.1";
    public static void main(String[] arg) {
        try {
            Cracker passCrack = new Cracker();

            Socket socket = new Socket(HOST , PORT);
            System.out.println("[SUCCESS]: Connected to Server");

            DataInputStream clientInput = new DataInputStream(socket.getInputStream());
            DataOutputStream clientOutput = new DataOutputStream(socket.getOutputStream());

            String hash = clientInput.readUTF();
            System.out.println("hash:  " + hash);

            while(true) {
                clientOutput.writeUTF("REQUEST_RANGE");

                String received = clientInput.readUTF();
                String[] msg = received.split(",");
                long start = Long.parseLong(msg[0]);
                long end = Long.parseLong(msg[1]);
                int textSize = Integer.parseInt(msg[2]);

                System.out.println("start: "  + Long.toString(start));
                System.out.println("end:   "  + Long.toString(end));
                System.out.println("size: " + Long.toString(textSize));

                String pass = passCrack.brute_force(start, end, textSize, hash);
                if(pass != null) {
                    System.out.println("[SUCCESS]: Password cracked successfully.\nDisconnecting...");
                    String message = "FOUND," + pass;
                    clientOutput.writeUTF(message);
                    break;
                }
            }

            socket.close();
            clientInput.close();
            clientOutput.close();
        } catch (SocketException | EOFException e) {
            System.out.println("[FAILED]: Could not connect to server (Reason: Server is closed)");
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}