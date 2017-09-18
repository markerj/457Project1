import java.io.*;
import java.net.*;
import java.nio.*;

public class Server {

 	public static void main (String [] args ) throws IOException {
       	//getting user input
        boolean validFile = false;
		boolean validPortNum = false;
        int portNum = 0;
        String ipAddr = null;
        Console cons = System.console();

        // check for valid port number
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

       	String clientSentence;
       	BufferedReader inFromClient = null;
       	FileInputStream fis = null;
       	BufferedInputStream bis = null;
       	OutputStream os = null;
       	ServerSocket servsock = null;
       	Socket sock = null;
		try{	    
      		servsock = new ServerSocket(portNum);
      		while (true) {
        		System.out.println("Waiting for connection");
        		try {
          			sock = servsock.accept();
          			System.out.println("Connection established: " + sock);
	  				inFromClient = new BufferedReader(new InputStreamReader(sock.getInputStream()));
          			clientSentence = inFromClient.readLine();
          			System.out.println("Client is requesting file: " + clientSentence);
	  				inFromClient.close();
	  				sock = servsock.accept(); 
	 
          			File myFile = new File (clientSentence);
	  				// check if myFile.exists() here?
	  				if(!myFile.exists() || myFile.isDirectory()){
	  					System.out.println("File does not exist");
	  				}
	  				else{
	  					byte [] byteArray  = new byte [(int)myFile.length()];
          				fis = new FileInputStream(myFile);
          				bis = new BufferedInputStream(fis);
          				bis.read(byteArray,0,byteArray.length);
          				os = sock.getOutputStream();
	
          				System.out.println("Sending " + clientSentence);
          				os.write(byteArray,0,byteArray.length);
          				os.flush();
          				System.out.println("File sent");
	  				}
	  
                 
			
        		}
 				finally {
          			bis.close();
          			os.close();
          			sock.close();
        		}
      		}
    	}
    	finally {
       		servsock.close();
    	}
 	}
}

