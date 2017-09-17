import java.io.*;
import java.nio.*;
import java.io.ByteArrayOutputStream;
import java.net.*;


public class Client {

//  public final static int SOCKET_PORT = 13267;      
//  public final static String SERVER = "127.0.0.1";  

  public static void main (String [] args ) throws IOException {
    //getting user input   
	
	boolean validPortNum = false;
        boolean validIpAddr = false;
        int portNum = 0;
        String ipAddr = null;
        Console cons = System.console();

        //check for valid port number
        while(validPortNum == false) {
        portNum = Integer.parseInt(cons.readLine("Enter a port number: "));
        if(portNum >= 1024 && portNum <= 65535){
        validPortNum = true;
        }
        else{
        validPortNum = false;
        System.out.println("Invalid port number");
        }
        }
	//check for valid ip address
	ipAddr = cons.readLine("Enter an IP address: ");

    String sentence;
    int bytesRead;
    int current = 0;
    DataOutputStream outToServer = null;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    Socket sock = null;
    sentence = cons.readLine("Enter the file name to retrieve: ");
    try {
	//connect to server 
        sock = new Socket(ipAddr, portNum);
	//writing filename client wishes to retrieve from server
	outToServer = new DataOutputStream(sock.getOutputStream());
 	outToServer.writeBytes(sentence);
	outToServer.close();
	//reconnect to server.. can't have both dataoutputstream and fileoutputstream active on the same socket.
      sock = new Socket(ipAddr, portNum); 
      byte [] byteArray  = new byte [6500000];
      InputStream is = sock.getInputStream();
      fos = new FileOutputStream("riverdownloaded.jpg");
      bos = new BufferedOutputStream(fos);
      bytesRead = is.read(byteArray,0,byteArray.length);
      current = bytesRead;
	
	while(bytesRead > -1) {
	 bytesRead = is.read(byteArray, current, (byteArray.length-current));
          if(bytesRead >= 0) {
	   current += bytesRead;
             }	
            }	
      bos.write(byteArray, 0 , current);
      bos.flush();
      System.out.println("File " + sentence
          + " downloaded");
    }
    finally {
      fos.close();
      bos.close();
      sock.close();
    }
  }

} 
