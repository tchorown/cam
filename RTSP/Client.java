/* ------------------
   Client
   usage: java Client [Server hostname] [Server RTSP listening port] [Video file requested]
   ---------------------- */
/** Obtained from http://www.csee.umbc.edu/~pmundur/courses/CMSC691C/lab5-kurose-ross.html
  *
  */
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class Client {

  //GUI
  //----
  JFrame f = new JFrame("Client");
  JButton setupButton = new JButton("Setup");
  JButton playButton = new JButton("Play");
  JButton pauseButton = new JButton("Pause");
  JButton tearButton = new JButton("Teardown");
  JPanel mainPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  JLabel iconLabel = new JLabel();
  ImageIcon icon;


  //RTP variables:
  //----------------
  DatagramPacket rcvdp; //UDP packet received from the server
  DatagramSocket RTPsocket; //socket to be used to send and receive UDP packets
  static int RTP_RCV_PORT = 25000; //port where the client will receive the RTP packets
  
  Timer timer; //timer used to receive data from the UDP socket
  byte[] buf; //buffer used to store data received from the server 
 
  //RTSP variables
  //----------------
  //rtsp states 
  final static int INIT = 0;
  final static int READY = 1;
  final static int PLAYING = 2;

  //rtsp message types
  final static int SETUP = 3;
  final static int PLAY = 4;
  final static int PAUSE = 5;
  final static int TEARDOWN = 6;

  static int state; //RTSP state == INIT or READY or PLAYING
  Socket RTSPsocket; //socket used to send/receive RTSP messages
  //input and output stream filters
  static BufferedReader RTSPBufferedReader;
  static BufferedWriter RTSPBufferedWriter;
  static String VideoFileName; //video file to request to the server
  int RTSPSeqNb = 0; //Sequence number of RTSP messages within the session
  int RTSPid = 0;

  final static String CRLF = "\r\n";

  public Client(){
    f.getContentPane().add(icon, BorderLayout.CENTER);

  // JPanel mainPanel = new JPanel();
  // JPanel buttonPanel = new JPanel();
  // JLabel iconLabel = new JLabel();
  // ImageIcon icon;

    f.pack();
    f.setVisible(true);
  }

  private void send_RTSP_request(int message)
  {
    try{
      String msg ="";
      if (message == SETUP){
        msg= "SETUP";
      }else if(message == PLAY){
        msg= "PLAY";
      }else if(message == PAUSE){
        msg= "PAUSE";
      }else if(message == TEARDOWN){
        msg= "TEARDOWN";
      }
      RTSPBufferedWriter.write(msg+" move.Mjpeg "+"RTSP/1.0"+CRLF);
      RTSPBufferedWriter.write("CSeq: "+RTSPSeqNb+CRLF);

      if (message == SETUP){
        RTSPBufferedWriter.write("Transport: RTP/UDP; client_port= 25000"+CRLF);
      }else{
        RTSPBufferedWriter.write("Session: "+RTSPid+CRLF);
      }
      RTSPBufferedWriter.flush();
      //System.out.println("RTSP Server - Sent response to Client.");
    }
    catch(Exception ex)
      {
        System.out.println("Exception caught: "+ex);
        System.exit(0);
      }
  }


  // //------------------------------------
  // //Parse RTSP Response
  // //------------------------------------
  // private int parse_RTSP_response()
  // {
  //   int request_type = -1;
  //   try{
  //     //parse request line and extract the request_type:
  //     String RequestLine = RTSPBufferedReader.readLine();
  //     //System.out.println("RTSP Server - Received from Client:");
  //     System.out.println(RequestLine);

  //     StringTokenizer tokens = new StringTokenizer(RequestLine);
  //     String request_type_string = tokens.nextToken();

  //     //convert to request_type structure:
  //     if ((new String(request_type_string)).compareTo("SETUP") == 0)
  //       request_type = SETUP;
  //     else if ((new String(request_type_string)).compareTo("PLAY") == 0)
  //       request_type = PLAY;
  //     else if ((new String(request_type_string)).compareTo("PAUSE") == 0)
  //       request_type = PAUSE;
  //     else if ((new String(request_type_string)).compareTo("TEARDOWN") == 0)
  //       request_type = TEARDOWN;

  //     if (request_type == SETUP)
  // {
  //   //extract VideoFileName from RequestLine
  //   VideoFileName = tokens.nextToken();
  // }

  //     //parse the SeqNumLine and extract CSeq field
  //     String SeqNumLine = RTSPBufferedReader.readLine();
  //     System.out.println(SeqNumLine);
  //     tokens = new StringTokenizer(SeqNumLine);
  //     tokens.nextToken();
  //     RTSPSeqNb = Integer.parseInt(tokens.nextToken());
  
  //     //get LastLine
  //     String LastLine = RTSPBufferedReader.readLine();
  //     System.out.println(LastLine);

  //     if (request_type == SETUP)
  // {
  //   //extract RTP_dest_port from LastLine
  //   tokens = new StringTokenizer(LastLine);
  //   for (int i=0; i<3; i++)
  //     tokens.nextToken(); //skip unused stuff
  //   RTP_dest_port = Integer.parseInt(tokens.nextToken());
  // }
  //     //else LastLine will be the SessionId line ... do not check for now.
  //   }
  //   catch(Exception ex)
  //     {
  // System.out.println("Exception caught: "+ex);
  // System.exit(0);
  //     }
  //   return(request_type);
  // }
  public static void main(String argv[]) {
    Client c = new Client();

  }


}






