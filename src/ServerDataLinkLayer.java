public class ServerDataLinkLayer {
    String startflag;
    String endflag;
    String sequence;

    public String getStartflag() {
        return startflag;
    }

    public String getEndflag() {
        return endflag;
    }

    public String getSequence() {
        return sequence;
    }

    public String getPayload() {
        return payload;
    }

    public String getPacketend() {
        return packetend;
    }

    String payload;
    String packetend;
    int FRAME_SIZE  =480;

    public ServerDataLinkLayer(String frame){
//   start seq error payload endofbit endflag
        startflag = frame.substring(0,8);
        sequence = frame.substring(8,16);
//        payload = frame.substring(16,496);
//        packetend = frame.substring(496,504);
//        endflag = frame.substring(504,512);
    }


    public String unStuff(){
        int counter=0;
        String save = "";
        for(int i=0;i< this.payload.length();i++)
        {
            if(this.payload.charAt(i) == '1')
            {
                counter++;
                save = save + this.payload.charAt(i);
            }
            else
            {
                save = save + this.payload.charAt(i);
                counter = 0;
            }
            if(counter == 5)
            {
                if((i+2)!=this.payload.length())
                    save = save + this.payload.charAt(i+2);
                else
                    save=save + '1';
                i=i+2;
                counter = 1;
            }
        }
        return save;
    }

}
