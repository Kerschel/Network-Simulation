import java.io.*;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Server {

    public static final int PORT = 6500;
    public static ArrayList <String> sequenceList = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        ServerSocket s = new ServerSocket(PORT);

        System.out.println("Started: " + s);
//        while(true) {
            Socket socket = s.accept();

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connection accepted: " + socket);
            String frame = "";
            String message = "";
            String hex; //Used to store the hexadecimal representation of the payload received from the client.
            String outputFileName = "output.txt";//The filename of the output file where we will write the data received from the client.
            PrintWriter outputData = new PrintWriter(new FileOutputStream(new File(outputFileName), true));
            int error=0;
            int i =1;
            int j = 1;
            String packet = "";
            while (!frame.equals("break")) {

                error = error +1;
                frame = inFromClient.readLine();
                if (frame != null && !frame.equals("break")) {
                    ServerDataLinkLayer data = new ServerDataLinkLayer(frame);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//                    8 bits first for start bit then 8 for sequence #
                    String remainder = frame.substring(24);
                    String Crc = frame.substring(16,24);

                    String endOfPacket = remainder.substring(remainder.indexOf(data.getStartflag()));
                    endOfPacket = String.valueOf(endOfPacket.charAt(endOfPacket.length()-1));

//                  Extract the payload from the frame
                    String payload = remainder.substring(0,remainder.indexOf(data.getStartflag()));
//                  Unstuff payload now
                    payload = new DataLinkLayer().unStuff(payload);

                    serverLog(i,j,1);
                    j++;
                    String inputData = payload + Crc;
                    if(new CRC().validateData(inputData ) && ((error%8) !=0)){ // if it is true


                        if(endOfPacket.equals("0") && sequenceList.indexOf(data.getSequence()) < 0){ // so if that sequence was never sent before
                            message += payload;
                            sequenceList.add(data.getSequence());
                        }
                        else if(endOfPacket.equals("1") && sequenceList.indexOf(data.getSequence()) <0){ // So here we have the entire packet together
                            i++;
//                            Packet received
                            serverLog(j,i,3);
                            message += payload;
                            // so if that sequence was never sent before
                            sequenceList.add(data.getSequence());
//                        end of packet
//                            TODO: this is the message that you will have to change to hex to store in the file: Completed
                            hex = new BigInteger(message, 2).toString(16);//Convert the binary message received from the client to hexadecimal
                            outputData.println(hex);//output the hexadecimal message to the output file.
                            System.out.println(message + " Sequence " + data.getSequence());
                            message ="";
                            sequenceList.clear();
                            j=1;

                        }
                        else if(sequenceList.indexOf(data.getSequence()) > 0){//duplicate packet or frame sent
                            serverLog(i,j,4);
                        }
//                        ACK sent
                        serverLog(i,j,5);
                        out.writeBytes(data.getSequence() + "\n");
                    }else {
                        if (!new CRC().validateData(inputData )) { // wrong crc calculated so want them to resend
                            serverLog(i, j, 2);
                            out.writeBytes("00010011" + "\n");
                        }
                        else{//everything is well just want to simulate a error
                            serverLog(i, j, 5);
                            out.writeBytes("00010011" + "\n");
                        }
                    }




                }
            }
            socket.close();
            outputData.close();
        }
//    }



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
            log.println(time + "Entire Packet:" + packet + " Received");
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
