
import java.math.BigInteger;
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Client {
    public static final int FRAME_SIZE = 480;
    public static int i;

    public static void main(String[] args) throws IOException {
        newFile();
        File myFile = new File("input.raw");
        FileInputStream myStream = new FileInputStream(myFile);
        Scanner in = new Scanner(myStream);
        int numPackets = in.nextInt();


        InetAddress addr = InetAddress.getByName("localhost");

        Socket socket = new Socket(addr, 6500);
        System.out.println("socket = " + socket);
        System.out.println("Please wait Sending...");

        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        int error =0;
        for(int j = 1; j <= numPackets; j++){

            frames = readPacket(in);

            boolean send = true;
            int sequence = frames.size(); // The packet chopped up into frames after reading the line from the file.
            for (i = 1; i <= sequence; i++) {
            error ++;
                String payload = frames.get(i-1);

                String packetend = "0";


                clientLog(j, i, 1);
                if(error %3 == 0 && i ==2){
                    i--;// to simulate sending a duplicate packet/frame. It was not specified when this should be done
                }
                if (i == sequence) {
                    packetend = "1";
                }
                DataLinkLayer dataLinkLayer = new DataLinkLayer("7E", "7E", String.valueOf(i),payload, packetend);
                if(error %5 == 0) {dataLinkLayer.FlipBit();}

                outToServer.writeBytes(dataLinkLayer.returnFrame() + "\n");

                Timer timer = new Timer();
                int frameNo = i;
                int finalJ = j;
                String finalPacketend = packetend;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Client timer finish");
//                      resend previous frame because no ACK received
                        System.out.println("Resending...");

                        try {
                            clientLog(finalJ, frameNo, 6);
                            clientLog(finalJ, frameNo, 2);
                            outToServer.writeBytes(new DataLinkLayer("7E", "7E", String.valueOf(i),payload, finalPacketend).returnFrame() + "\n");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                },  5 *  1000); // wait for 5 secs

//                        Get ACK from server
                String ACK = inFromServer.readLine();
//   if ACK was received good cancel the resend of frame
                if (Integer.parseInt(ACK, 2) == i) {
                    timer.cancel();
                    clientLog(j, i, 4);
                } else{
                    //   so if get wrong ack send = false will resend the same packet at top
                    clientLog(j, i, 5);
                    clientLog(j, i, 2);
                    timer.cancel();
                    dataLinkLayer = new DataLinkLayer("7E", "7E", String.valueOf(i),payload, packetend);
                    outToServer.writeBytes(dataLinkLayer.returnFrame() + "\n");
                    String ack = inFromServer.readLine();
                    clientLog(j, i, 4);
                }

            }
            clientLog(j, i, 3);
        }
        System.out.println("Transmission Complete");

        outToServer.writeBytes("break");
socket.close();
    }


    public static void clientLog(int packet, int frame, int state) throws IOException {
        PrintWriter log = new PrintWriter(new FileOutputStream(
                new File("logs/client.log"),
                true /* append = true */));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat time_formatter = new SimpleDateFormat("mm:ss:S");
        String time = time_formatter.format(cal.getTime());
        if (state == 1) {// frame sent
            log.println(time + " Frame:" + frame + " Packet:" + packet + " Sent");
        } else if (state == 2) {// frame resent
            log.println(time + " Frame:" + frame + "  Packet:" + packet + " Resent");
        } else if (state == 3) {// Packet sent
            log.println(time + " Complete Packet:" + packet + " Sending");
        } else if (state == 4) {// Ack Received successfully
            log.println(time + " Frame:" + frame + " Packet:" + packet + " ACK received");
        } else if (state == 5) {//Ack received in error
            log.println(time + " Frame:" + frame + " Packet:" + packet + " ACK received in error");
        } else if (state == 6) {//Timer expired
            log.println(time + " Frame:" + frame + " Packet:" + packet + " Timer Expired");
        }
        log.close();
    }


    public static String hexadeciamlToBinary(String hexadecimal) {
        return new BigInteger(hexadecimal, 16).toString(2);
    }

    public static ArrayList<String> readPacket(Scanner in) {
        int packetSize;
        frames.clear();
        String packetData, binary, currentPayload;

        packetSize = in.nextInt();
        packetData = in.next();

        binary = hexadeciamlToBinary(packetData);
        currentPayload = binary;


        while (binary.length() > 0) {
            if (binary.length() > FRAME_SIZE) {
                currentPayload = binary.substring(0, FRAME_SIZE);
                binary = binary.substring(FRAME_SIZE, binary.length());
            } else {
                currentPayload = binary;
                binary = "";
            }
            frames.add(currentPayload);
        }
        return frames;
    }
    public static ArrayList<String> frames = new ArrayList<>();

    public static void newFile() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("logs/client.log");
        writer.print("");
        writer.close();
    }
}
