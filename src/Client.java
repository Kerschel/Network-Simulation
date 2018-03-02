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


            DataOutputStream out =new DataOutputStream( socket.getOutputStream());
            Frame frame = new Frame("7E","7E","1","8f db 9b ab 20 7d 26 86 57 77 7a 76 97 27 f2 23","1");
            out.writeBytes(frame.getSendablePacket());
//        int chops = 2;
//        int remainde = 45;
//
//        String payload = "8f db 9b ab 20 7d 26 86 57 77 7a 76 97 27 f2 23 " +
//                "8b 80 77 a4 c5 0e f2 77 0d 33 5d 0b 72 c1 0a 64 " +
//                "7d 21 af 39 f9 2c 42 28 16 f2 72 f1 68 36 e8 4e " +
//                "28 48 16 ac 79 13 95 4c 6f 8d 8e d1 99 20 ";
//
//        for(int i=1;i<=chops;i++){
//
//        }
//            String TextToCode = "Infornation Technology";
//            out.println(TextToCode.getBytes());
//            out.println("END");
        }
        catch (Exception e)
        {

        } finally {
            System.out.println("closing...");
            socket.close();
        }
    }

}

