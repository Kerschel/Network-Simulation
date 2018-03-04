
import java.math.BigInteger;

public class DataLinkLayer {


    String startflag;
    String endflag;
    String seq;
    String payload;
    String packetend;


    public DataLinkLayer(String start, String end, String seq, String payload, String packetend){
    this.startflag = stuffer(hexToBinary(start));
    this.endflag = stuffer(hexToBinary(end));
    this.packetend = packetend;
    this.payload = hexToBinary(payload);
    if(this.payload.length() <60){
        this.payload = stuffer(payload);
    }
    this.seq = stuffer(Integer.toBinaryString(Integer.valueOf(seq)));
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

    public String returnFrame(){
        return startflag + seq + payload + packetend + endflag;
    }

    public static String CalculateError(String payload){
        int mid = payload.length()/2;
        String[] parts = {payload.substring(0, mid),payload.substring(mid)};


        int num1 =  Integer.parseInt(parts[1],2);
        int num2 =  Integer.parseInt(parts[0],2);

        return Integer.toBinaryString(num1+num2);
    }



    public DataLinkLayer(String frame){
//   start seq error payload endofbit endflag
        this.startflag = frame.substring(0,8);
        this.seq = frame.substring(8,16);

//        String error = frame.substring(16,23);
//        8*60 to know how many bits is the payload
//        String payload = frame.substring(16,8*60);
    }

       public static void main(String[] args){
       String payload = "8b8077a4c50ef2770d335d0b72c10a64";

           System.out.println(CalculateError("0000110100110011010111010000101101110010110000010000101001100100"));

   }

    public String getStartflag() {
        return startflag;
    }

    public String getEndflag() {
        return endflag;
    }

    public String getSeq() {
        return seq;
    }

    public String getPayload() {
        return payload;
    }

    public String getPacketend() {
        return packetend;
    }

}
