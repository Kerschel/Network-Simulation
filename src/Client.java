import java.net.*;
import java.io.*;

public class Client {

    public static void main(String[] args)
            throws IOException {

        InetAddress addr = InetAddress.getByName("localhost");


        System.out.println("addr = " + addr);

        Socket socket = new Socket(addr, 6500);

        try {
            System.out.println("socket = " + socket);
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));


            PrintWriter out =
                    new PrintWriter(
                            new BufferedWriter(
                                    new OutputStreamWriter(
                                            socket.getOutputStream())),true);



            String TextToCode = "Infornation Technology";
            out.println(TextToCode.getBytes());
            out.println("END");
        }
        catch (Exception e)
        {

        } finally {
            System.out.println("closing...");
            socket.close();
        }
    }

}

