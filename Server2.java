import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

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

    //  	String clientSentence;
      // 	BufferedReader inFromClient = null;
       	//FileInputStream fis = null;
       //	BufferedInputStream bis = null;
       //	OutputStream os = null;
       	ServerSocket servsock = null;
       	Socket sock = null;
	
	
		try{	  
  //		 ServerSocketChannel servsock = ServerSocketChannel.open();
    //            servsock.bind(new InetSocketAddress(portNum));
 		servsock = new ServerSocket(portNum);
	//	}
	//	catch(IOException e) {}

      		while (true) {
        		System.out.println("Waiting for connection");
        		try {
//		SocketChannel sock = servsock.accept();
//		TcpServerThread t = new TcpServerThread(sock);		
          		sock = servsock.accept();
			new ServerThread(sock).start();


        		}
			//new ServerThread(sock).start();
			catch(IOException e) {}

// 				finally {
        //  			bis.close();
          //			os.close();
          //			sock.close();
       //   servsock.close();

        	}	
      		}
    //	}
    	finally {
      	//servsock.close();
    	}
 	}
}

class ServerThread extends Thread {
    //prot
     Socket sock = null;
//SocketChannel sock;
//TcpServerThread(SocketChannel channel) {
//	sock = channel;

    public ServerThread(Socket clientSocket) {
       this.sock = clientSocket;
//System.out.println("Connection established: " + sock);

    }

    public void run() {
	System.out.println("Connection established: " + sock);

	String clientSentence;
        BufferedReader inFromClient = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;

	try{
                   //       System.out.println("Connection established: " + sock);
  //                              inFromClient = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    //                            clientSentence = inFromClient.readLine();
      //                          System.out.println("Client is requesting file: " + clientSentence);
        //                        inFromClient.close();

//}
//catch(IOException e) {}
                                //sock = servsock.accept(); 
         //
           //                     	File myFile = new File (clientSentence);
           				File myFile = new File ("something.pdf");
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

                                        System.out.println("Sending... ");
                                        os.write(byteArray,0,byteArray.length);
                                        os.flush();
        

    		                        System.out.println("File sent");
					bis.close();
			       		os.close();
      					//sock.close();

}
	
}
//finally { 
//	bis.close();
//	os.close();
//	sock.close();

//}
catch(IOException e) {
 //	bis.close();
  //      os.close();
//    	sock.close();
       }
//catch(IOException e){}
}}   
