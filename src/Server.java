import java.io.*;
import java.net.*;

public class Server {

    public static final int PORT = 6500;

    public static void main(String[] args) throws IOException {
        int start = 0x7E;
        int end = 0x7E;

        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Started: " + s);


        while (true) {
            Socket socket = s.accept();
            try {
                System.out.println("Connection accepted: " + socket);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()));

                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                String frame = in.readLine();
                System.out.println("Echoing: " + frame);
//                String starting = frame.substring(0,7);
//                String seq = frame.substring(8,15);
//                String error = frame.substring(16,23);
//                String payload = frame.substring(24,63);
//                out.println(str);


            } finally {
                System.out.println("closing...");
                socket.close();
            }

        }

    }

//   start seqnum error payload endofbit endflag
}