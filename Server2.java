import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

public class Server2
{

 	public static void main (String [] args ) throws IOException
 	{
     
        boolean validFile = false;
		boolean validPortNum = false;
        int portNum = 0;
        String ipAddr = null;
       	ServerSocket servsock = null;
       	Socket sock = null;
	
		try
		{
			portNum = getPort();	
 			servsock = new ServerSocket(portNum);
			System.out.println("Waiting for connection");
      		while (true)
      		{
        		try
        		{		
          			sock = servsock.accept();
					new ServerThread(sock).start();
        		}
				catch(IOException e)
				{
				}
        	}	
      	}
     	finally
     	{
			if (servsock != null)
				servsock.close();
		}
    	
 	}
 	
 	private static int getPort()
 	{
		int port = 0;
		boolean valid = false;
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter a port number: ");

		while (!valid)
		{
			try
			{
				port = Integer.parseInt(input.readLine());
				if (port > 1024 && port < 65536)
				{
					valid = true;
				}
				else
				{
					System.out.println("Invalid Port. \nEnter a port number: ");
				}
			}
			catch (NumberFormatException e)
			{
				System.out.println("Invalid Port. \nEnter a port number: ");
			}
			catch (IOException e)
			{
				System.out.println("Invalid Port. \nEnter a port number: ");
			}
		}
		System.out.println("connecting to port: " + port);
		return port;
	}

}

class ServerThread extends Thread
{

    Socket sock = null;
    
    public ServerThread(Socket clientSocket)
    {
       this.sock = clientSocket;
    }

    public void run()
    {
		System.out.println("Connection established: " + sock);
		String clientSentence;
        BufferedReader inFromClient = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
		try
		{
			try
			{
                  
				inFromClient = new BufferedReader(new InputStreamReader(sock.getInputStream()));  
				DataOutputStream outToClient = new DataOutputStream(sock.getOutputStream());
         		clientSentence = inFromClient.readLine();
				System.out.println("Client said: " + clientSentence);
				outToClient.writeBytes(clientSentence + '\n');

				String fileToSend = clientSentence.substring(5,clientSentence.length());
				
				if (clientSentence.length() > 4)
				{
					if (clientSentence.substring(0, 4).toLowerCase().equals("send"))
					{					
           				File myFile = new File (fileToSend);
                        // check if myFile.exists() 
						if(!myFile.exists() || myFile.isDirectory())
						{
							System.out.println("File does not exist");
							//outToClient.writeBytes("File does not exist");
							outToClient.writeBytes("File does not exist");
							outToClient.flush();
							
						}
                        else
                        {
							byte [] byteArray  = new byte [(int)myFile.length()];
							fis = new FileInputStream(myFile);
							bis = new BufferedInputStream(fis);
							bis.read(byteArray,0,byteArray.length);
							os = sock.getOutputStream();
							os.write(byteArray,0,byteArray.length);
							os.flush();
							System.out.println("File sent");
						}
				 
					}
					
					//send list of filenames
					else if(clientSentence.substring(0, 4).toLowerCase().equals("list"))
					{
						String list = "";
						File dir = new File(".");
						File[] filesList = dir.getCanonicalFile().listFiles();
						for(int i = 0; i < filesList.length; i++)
						{
							if(filesList[i].isFile())
							{
								list += (filesList[i].getName() + " ");
							}
						}
						outToClient.writeBytes(list);
						outToClient.flush();
						System.out.println("Sent list of files to Client");
					}
				}
			}
			finally
			{					
				if (bis != null)
					bis.close();
				if (os != null)
					os.close();
				if (sock != null)
					sock.close();
			}
		}
		catch(IOException e)
		{
		}
		catch(Exception e)
		{
		}
	}
}   
