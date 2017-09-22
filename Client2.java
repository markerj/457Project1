import java.io.*;
import java.nio.*;
import java.io.ByteArrayOutputStream;
import java.net.*;
import java.nio.channels.*;
public class Client {

	public static void main (String [] args ) throws IOException {
   	 //getting user input   
	
	boolean validPortNum = false;
        boolean validIpAddr = false;
        int portNum = 0;
        String ipAddr = null;
        Console cons = System.console();
	
        //check for valid port number
        while(validPortNum == false) {
        	try{
        		portNum = Integer.parseInt(cons.readLine("Enter a port number: "));
        	}
        	catch(NumberFormatException e){
        		System.out.println("You may have entered a non integer string");
        	}
        	if(portNum >= 1024 && portNum <= 65535){
        		validPortNum = true;
        	}
        	else{
        		validPortNum = false;
        		System.out.println("Invalid port number");
        	}
        }
		//check for valid ip address
		while(validIpAddr == false){
			ipAddr = cons.readLine("Enter an IP address: ");
			try{
				String[] pieces = ipAddr.split("\\.");
				
				for(String s: pieces){
					int i = Integer.parseInt(s);
					if((i < 0) || i > 255){
						System.out.println("IP address parts cannot be smaller than 0 or larger than 255");
						break;
					}
				}
				
				if(ipAddr == null || ipAddr.isEmpty()){
					System.out.println("IP address is empty");
				}
				else if(pieces.length != 4){
					System.out.println("IP address does not contain correct number of parts");
				}
				
				else if(ipAddr.endsWith(".")){
					System.out.println("IP addresses cannot end with a period");
				}
				
				else{
					validIpAddr = true;
				}
				
			}
			catch(NumberFormatException nfe){
				System.out.println("There was a number format exception");
			}
		}

	String sentence;
    	int bytesRead;
    	int current = 0;
    	DataOutputStream outToServer = null;
    	FileOutputStream fos = null;
    	BufferedOutputStream bos = null;
    	Socket sock = null;
    	//sentence = cons.readLine("Enter the file name to retrieve: ");
	
    	try {

//		SocketChannel sock = SocketChannel.open();
//		sock.connect(new InetSocketAddress(ipAddr, portNum));
	
		while (true) {
		//connect to server 
        	sock = new Socket(ipAddr, portNum);
		System.out.println("Connection to server: " + sock);
		sentence = cons.readLine("Enter the file name to retrieve: ");

		//writing filename client wishes to retrieve from server
//		outToServer = new DataOutputStream(sock.getOutputStream());
 //		outToServer.writeBytes(sentence);
//		outToServer.close();
		//reconnect to server.. can't have both dataoutputstream and fileoutputstream active on the same socket.
//      		sock = new Socket(ipAddr, portNum); 
      		byte [] byteArray  = new byte [6500000];
      		InputStream is = sock.getInputStream();
      		
		if(sentence.contains("pdf")) {
		fos = new FileOutputStream("downloaded.pdf");
     		}
     		else
      		{
      		fos = new FileOutputStream("downloaded.jpg");
      		}
		
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
      		System.out.println("File " + sentence + " downloaded");
    	}
	}
    	finally {
      		fos.close();
      		bos.close();
      		//sock.close();
    	}
  }

} 
