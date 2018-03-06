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

    }



}
