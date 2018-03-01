import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class headers {
    int  start;
    int end;
    String seq;
    String payload;
    String packetend;

    public headers(){

    }

    public headers(int start, int end, String seq, String payload, String packetend){
    this.start = start;
    this.end = end;
    this.packetend = packetend;
    this.payload = payload;
    this.seq = seq;
    }

    public static String hexToBinary(String hex) {
        int i = Integer.parseInt(hex, 16);
        String bin = Integer.toBinaryString(i);
        return bin;
    }

    public  String stuffer(String binary){
        if(binary.length() < 8){
            int increase = 8 - binary.length();
            for(int i =0;i<increase;i++){
                binary  = "0" + binary;
            }
        }
    return binary;
    }

}
