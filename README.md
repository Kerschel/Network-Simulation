# Network-Simulation
Simulates how a network sends packets over TCP protocol. All the layers are simulated with a hex file being used to represent the packets .

The purpose of this is to emulate how communications at the data link layer work with
respect to Positive Acknowledgement with Retransmission (PAR). Java code was written for a client and server processes that send and receive frames. The responsibilities
of these two processes include: byte stuffing, error detection and the PAR protocol with a
timeout mechanism that causes a frame retransmission when frames are not promptly acknowledged. 

# Client
Initially, the client process calls the physical layer to establish a connection with the server. Once
a connection has been established, the client network layer reads in one packet at a time from the
input file. The client network layer sends a packet to the client data link layer. The client data
link layer receives packets from the network layer and converts them to frames. The client data
link layer gives frames to the client physical layer to be sent via TCP. Upon receiving each
packet from the network layer, the client data link layer splits the packet into payloads. The data
link layer builds each frame as follows:
- put the payload in the frame
- deposit the proper contents into the end-of-packet byte
- compute the value of the error-detection byte and put it in the frame
- byte-stuff all of the above bytes
- start a timer
- send the frame (including the framing bytes) to the physical layer.

The client physical layer sends the constructed frame as an actual TCP message to the physical
layer of the server process. The client data link layer then waits to receive an ACK frame. If the
ACK frame is received successfully before the timer expires, the client sends the next frame of
the packet or gets the next packet from the network layer. If the ACK frame is received in error,
record the event in the log and continue the data link layer as if the ACK was never received.
If the timer expires, the client retransmits the frame. The client records significant events in a log
file client.log. 

# Server
The server begins by waiting for the establishment of a connection from a client. Once the
connection is established, the server data link layer cycles between receiving a frame from the
physical layer, reassembling the packet and possibly sending the packet up to the network layer,
and sending an ACK frame back to the client via the server physical layer. There is no need for a
timer at the server. Note: the end-of-packet byte is used to indicate the last frame of a
packet. When the client closes the connection to the server, the server terminates.
The server data link layer has to unstuff frames and check for an error in the error-detection
byte. If the received data frame is in error, the server records the event and waits to receive
another frame from the client. The server data link layer checks received frames for duplicates
and reassembles frames into packets and sends one packet at a time to the network layer where
they are written to server.out. Note – the server needs to send an ACK when a duplicate frame is
received due to possibly damaged ACKs. The server records significant events including frame
received, frame received in error, duplicate frame received, ACK sent, and packet sent to the
network layer in server.log.
