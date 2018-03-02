
import java.math.BigInteger;

public class Frame {
    String  startflag;
    String endflag;
    String seq;
    String payload;
    String packetend;


    public Frame(String start, String end, String seq, String payload, String packetend){
    this.startflag = hexToBinary(start);
    this.endflag = hexToBinary(end);
    this.packetend = packetend;
    this.payload = hexToBinary(payload);
    if(this.payload.length() <60){
        this.payload = stuffer(payload);
    }
    this.seq = seq;
    }


    public static String hexToBinary(String hex) {
        hex = hex.replace(" ","");
        return new BigInteger(hex, 16).toString(2);
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

    public String getSendablePacket(){
        return startflag + seq + payload + packetend + endflag;
    }
//   public static void main(String[] args){
//       String payload = "8b8077a4c50ef2770d335d0b72c10a64";
//
//           System.out.println(stuffer(hexToBinary(payload)));
//
//   }
}
