import com.sun.istack.internal.Nullable;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Formatter;
import java.util.Timer;
import java.util.TimerTask;

public class Client {

    public static int i;
    public static void main(String[] args) throws IOException {

        InetAddress addr = InetAddress.getByName("localhost");

        Socket socket = new Socket(addr, 6500);
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("socket = " + socket);
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        clientLog(1,2,1);
        clientLog(1,2,4);
        while (true) {
            String b = inFromUser.readLine();
            int a = 1;

            boolean send = true;
            int seq = 2;
            for (i = 1; i <= seq; i++) {

                String packetend = "0";
                if (i == seq) {
                    packetend = "1";
                }
//                Gerard put 60 bytes at a time in the payload
                DataLinkLayer dataLinkLayer = new DataLinkLayer("7E", "7E", String.valueOf(i), "ef ce 86 1f d9 27 87 d5 96 ff de 43 52 39 48 52 " +
                        "8b 14 da e6 41 b6 47 da 5e 50 84 67 7d b6 ea 1f " +
                        "37 70 07 3b 2f 56 7c f9 3e f4 2f 8e 80 56 5a 38 " +
                        "7b e1 bd e2 b3 83 0a 20 02 04 7a 4c ", packetend);

                outToServer.writeBytes(dataLinkLayer.returnFrame() + "\n");
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("timer");
//                      resend previous frame because no ACK received
                        try {
                            clientLog(1,i,1);
                            outToServer.writeBytes(dataLinkLayer.returnFrame() + "\n");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }, 2 * 30 * 1000);

//                        Get ACK from server
                String ACK = inFromServer.readLine();
                System.out.println(Integer.parseInt(ACK, 2) + "Hello" + i);

                if (Integer.parseInt(ACK, 2) == i) {
                    timer.cancel();
                    System.out.println("Cancled");
                    send = true;
                } else
//                    so if get wrong ack send = false will resend the same packet at top
                    send = false;


            }


        }

    }


    public static void clientLog(int packet,int frame,int state) throws IOException {
        PrintWriter log =  new PrintWriter(new FileOutputStream(
                new File("client.log"),
                true /* append = true */));

        if(state==1){// frame sentt
            log.println("Frame:" + frame + " Packet:" + packet+ " Sent");
        }
        else if(state == 2){// frame resent
            log.println("Frame:"+frame+ "  Packet:"+ packet + " Resent");
        }
        else if(state ==4){// Packet sent
            log.println("Packet:"+ packet + " Sent");
        }
        else if(state ==4){// Ack Received successfully
            log.println("Frame:"+frame+ " Packet:"+ packet + " ACK received");
        }
        else if(state == 5){//Ack received in error
            log.println("Frame:"+frame+ " Packet:"+ packet + " ACK received in error");
        }
        else if(state ==6){//Timer expired
            log.println("Frame:"+frame+ " Packet:"+ packet + " Timer Expired");
        }
        log.close();
    }
}
