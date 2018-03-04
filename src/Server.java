import java.io.*;
import java.net.*;

public class Server {

    public static final int PORT = 6500;

    public static void main(String[] args) throws IOException {
        int start = 0x7E;
        int end = 0x7E;

        ServerSocket s = new ServerSocket(PORT);

        System.out.println("Started: " + s);
        Socket socket = s.accept();

        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
        System.out.println("Connection accepted: " + socket);

        while (true) {
            String frame = inFromClient.readLine();
//            System.out.println(frame);
            DataLinkLayer data = new DataLinkLayer(frame);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            System.out.println(data.getSeq());
            out.writeBytes(data.getSeq() + "\n");
        }
    }
}
//   start seqnum error payload endofbit endflag
