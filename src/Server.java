import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Server {

    public static final int PORT = 6500;
    public static ArrayList <String> sequenceList = new ArrayList<>();
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
            String message = "";
            int error=0;
            String packet = "";
            while (!frame.equals("break")) {
                error = error +1;
                frame = inFromClient.readLine();
                if (frame != null && !frame.equals("break")) {
                    ServerDataLinkLayer data = new ServerDataLinkLayer(frame);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//                    8 bits first for start bit then 8 for sequence #
                    String remainder = frame.substring(16);

                    String endOfPacket = remainder.substring(remainder.indexOf(data.getStartflag()));
                    endOfPacket = String.valueOf(endOfPacket.charAt(endOfPacket.length()-1));

//                  Extract the payload from the frame
                    String payload = remainder.substring(0,remainder.indexOf(data.getStartflag()));
//                  Unstuff payload now
                    payload = unStuff(payload);

                    if(endOfPacket.equals("0") && sequenceList.indexOf(data.getSequence()) < 0){ // so if that sequence was never sent before
                    message += payload;
                    sequenceList.add(data.getSequence());
                    }
                    else if(endOfPacket.equals("1") && sequenceList.indexOf(data.getSequence()) <0){ // So here we have the entire packet together
                        message += payload;
                         // so if that sequence was never sent before
                        sequenceList.add(data.getSequence());
//                        end of packet
                        System.out.println(message);
                        message ="";
                        sequenceList.clear();
                    }

                    if(error ==8){ //send ack with error. So sending 9 as the seq # to make it look like a error
                        error =0;
                        String bad = data.getSequence();
                        bad = Integer.toBinaryString(9);
                        out.writeBytes(bad + "\n");
                    }
                    else{
                        out.writeBytes(data.getSequence() + "\n");
                    }

                }
            }
        }
    }


    public static String unStuff(String payload){
        int counter=0;
        String save = "";
        for(int i=0;i< payload.length();i++)
        {
            if(payload.charAt(i) == '1')
            {
                counter++;
                save = save + payload.charAt(i);
            }
            else
            {
                save = save + payload.charAt(i);
                counter = 0;
            }
            if(counter == 5)
            {
                if((i+2)!=payload.length())
                    save = save + payload.charAt(i+2);
                else
                    save=save + '1';
                i=i+2;
                counter = 1;
            }
        }
        return save;
    }

    public static void serverLog(int packet, int frame, int state) throws IOException {
        PrintWriter log = new PrintWriter(new FileOutputStream(
                new File("server.log"),
                true /* append = true */));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat time_formatter = new SimpleDateFormat("mm:ss:S");
        String time = time_formatter.format(cal.getTime());
        if (state == 1) {// frame sent
            log.println(time + " Frame:" + frame + " Packet:" + packet + " Received");
        } else if (state == 2) {// frame resent
            log.println(time + " Frame:" + frame + "  Packet:" + packet + " Received in error");
        } else if (state == 3) {// Packet sent
            log.println(time + " Packet:" + packet + " Sent");
        } else if (state == 4) {// duplicate frame
            log.println(time + " Frame:" + frame + " Packet:" + packet + " Duplicate received");
        } else if (state == 5) {//Ack sent
            log.println(time + " Frame:" + frame + " Packet:" + packet + "ACK sent");
//        } else if (state == 6) {//Timer expired
//            log.println(time + " Frame:" + frame + " Packet:" + packet + " Timer Expired");
        }
        log.close();
    }


}

//   start seqnum error payload endofbit endflag
