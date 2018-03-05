import java.io.*;
import java.net.*;

public class Server {

    public static final int PORT = 6500;

    public static void main(String[] args) throws IOException {
        int start = 0x7E;
        int end = 0x7E;

        ServerSocket s = new ServerSocket(PORT);

        System.out.println("Started: " + s);
        while(true) {
            Socket socket = s.accept();

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connection accepted: " + socket);
            String frame = "";
            int error=0;
            while (!frame.equals("break")) {
                error = error +1;
                frame = inFromClient.readLine();
                if (frame != null && !frame.equals("break")) {
                    ServerDataLinkLayer data = new ServerDataLinkLayer(frame);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//                    8 bits first for start bit then 8 for sequence #
                    System.out.println(frame.substring(16));
//                    System.out.println(data.unStuff().length());
                    out.writeBytes(data.getSequence() + "\n");
                }
            }
        }
    }
}
//   start seqnum error payload endofbit endflag
