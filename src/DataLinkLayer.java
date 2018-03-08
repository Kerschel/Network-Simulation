
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
    String crc = " ";
    int FRAME_SIZE  =480;

    public DataLinkLayer(String start, String end, String seq, String payload, String packetend){
    this.startflag = stuffer(hexToBinary(start));
    this.endflag = stuffer(hexToBinary(end));
    this.endOfPacket = stuffer(Integer.toBinaryString(Integer.valueOf(packetend)));
    this.payload = payload;
    CRC crc = new CRC();
    this.crc = crc.computeCRC(payload);
        System.out.println(this.payload.length());
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

    public void FlipBit(){
        char[] characters = crc.toCharArray();
        int rand = (int)(Math.random() * crc.length());
            if(characters[rand] == '0')
                characters[rand] ='1';
            else characters[rand] = '0';

            this.crc = new String (characters);
    }

    public String returnFrame(){
        System.out.println(crc);
        return startflag + seq +crc+ payload  + endflag + endOfPacket;
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



public DataLinkLayer(){

}

    public String unStuff(String payload){
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
}
