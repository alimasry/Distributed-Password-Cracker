import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

class ClientHandler extends Thread {
    private Socket socket;
    private Generator generator;
    private DataInputStream serverInput;
    private DataOutputStream serverOutput;

    public ClientHandler(ServerSocket server , Generator generator) throws IOException {
        this.socket = server.accept();
        this.serverInput = new DataInputStream(socket.getInputStream());
        this.serverOutput = new DataOutputStream(socket.getOutputStream());
        this.generator = generator;
    }

    @Override
    public void run() {
        String received;
        long prevRange = 0;
        try {
            serverOutput.writeUTF(Server.hash);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                received = serverInput.readUTF();
                String[] msg = received.split(",");
                if (msg[0].equals("REQUEST_RANGE")) {
                    long range = generator.next_range();
                    if(Generator.mRangeMap.containsKey(prevRange))
                        Generator.mRangeMap.remove(prevRange);
                    prevRange = range;
                    String message = Long.toString(Math.max(0, range - generator.mRangeSize)) + ',' + Long.toString(range) + ',' + Integer.toString(generator.mTextSize);
                    serverOutput.writeUTF(message);
                }
                else if (msg[0].equals("FOUND")) {
                    System.out.println("[SUCCESS]: Password Found! \nPassword:   " + msg[1]);
                    Server.userOutput.writeUTF(msg[1]);
                    Server.finish();
                }
            } catch (SocketException | EOFException e) {
                try {
                    this.cancel();
                } catch (IOException err) {
                    err.printStackTrace();
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel() throws IOException {
        this.socket.close();
        this.serverOutput.close();
        this.serverInput.close();
    }
}