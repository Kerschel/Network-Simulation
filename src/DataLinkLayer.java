
import java.math.BigInteger;

public class DataLinkLayer {


    String startflag;
    String endflag;
    String seq;

    public void setPayload(String payload) {
        this.payload = payload;
    }

    String payload;
    String endOfPacket;
    int FRAME_SIZE  =480;

    public DataLinkLayer(String start, String end, String seq, String payload, String packetend){
    this.startflag = stuffer(hexToBinary(start));
    this.endflag = stuffer(hexToBinary(end));
    this.endOfPacket = stuffer(Integer.toBinaryString(Integer.valueOf(packetend)));
    this.payload = payload;
        System.out.println(this.payload.length());
//    if(this.payload.length() <(60*8)){//making into 60 bytes
//        while(this.payload.length() != FRAME_SIZE)
//            this.payload = "0" + this.payload;
//        }
    this.payload = BitStuff();
    this.seq = stuffer(Integer.toBinaryString(Integer.valueOf(seq)));
    }

    public String BitStuff(){
        int counter =0;
        String save = "";
        for(int i=0;i<this.payload.length();i++)
        {

            if(this.payload.charAt(i) == '1')
            {
                counter++;
                save +=  this.payload.charAt(i);
            }
            else
            {
                save += this.payload.charAt(i);
                counter = 0;
            }
            if(counter == 5)
            {
                save += '0' ;
                counter = 0;
            }
        }
        return save;
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
        return startflag + seq + payload  + endflag + endOfPacket;
    }

    public static String CalculateError(String payload){
        int mid = payload.length()/2;
        String[] parts = {payload.substring(0, mid),payload.substring(mid)};


        int num1 =  Integer.parseInt(parts[1],2);
        int num2 =  Integer.parseInt(parts[0],2);

        return Integer.toBinaryString(num1+num2);
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
        return endOfPacket;
    }

}
