import java.io.*;
import java.nio.*;
import java.io.ByteArrayOutputStream;
import java.net.*;
import java.nio.channels.*;
public class Client2
{

	public static void main (String [] args ) throws IOException
	{
    
		boolean exit = false;
 		String fileName = null;
		String sentence;
		String serverSentence;
    	int bytesRead;
    	int current = 0;

		InputStream is = null;
    	DataOutputStream outToServer = null;
    	FileOutputStream fos = null;
    	BufferedOutputStream bos = null;
    	Socket sock = null;
		BufferedReader inFromUser = null;
    	BufferedReader inFromServer = null;	
    	try
    	{
			int port = getPort();
			String ip = getIP();
			sock = new Socket(ip, port);
			System.out.println("Connection to server: " + sock);
			while (!exit)
			{
				outToServer = new DataOutputStream(sock.getOutputStream());
				inFromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				inFromUser = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Commands: exit, send <filename>, listFiles");
				sentence = inFromUser.readLine();
				if (sentence.equals("exit"))
				{
					exit = true;
				}	
				outToServer.writeBytes(sentence + '\n');
				serverSentence = inFromServer.readLine();
				System.out.println("Server Respone: " + serverSentence);
				if (sentence.length() > 4)
				{
					if (sentence.substring(0, 4).toLowerCase().equals("send"))
					{
						String recieve = inFromServer.readLine();
						if(recieve.equals("File does not exist"))
						{
							System.out.println("File does not exist");
							sock = new Socket(ip, port);
						}
						else
						{
							fileName = sentence.substring(5,sentence.length());
      						byte [] byteArray  = new byte [6500000];
      						is = sock.getInputStream();
							fos = new FileOutputStream("downloaded." + sentence.substring((sentence.length()-3),sentence.length()));
      
      						bos = new BufferedOutputStream(fos);
      						bytesRead = is.read(byteArray,0,byteArray.length);
      						current = bytesRead;
	
							while(bytesRead > -1)
							{
	 							bytesRead = is.read(byteArray, current, (byteArray.length-current));
          						if(bytesRead >= 0)
          						{
	   								current += bytesRead;
            					}	
        					}	
      						bos.write(byteArray, 0 , current);
      						bos.flush();
      						System.out.println("File " + fileName + " downloaded");
							sock = new Socket(ip, port);
						}
						
    				}
    				else if(sentence.substring(0, 4).toLowerCase().equals("list"))
    				{
    					String list = inFromServer.readLine();
    					String[] splitList = list.split(" ");
    					System.out.println("---------------------");
    					for(int i = 0; i < splitList.length; i++)
    					{
    						System.out.println(splitList[i]);
    					}
    					System.out.println("---------------------");
    					sock = new Socket(ip, port);
    				}
    			}
    			else
    			{
    				System.out.println("That is not a known command");
    				sock = new Socket(ip, port);
    			}
    		}
		}
    	finally
    	{
			if (fos != null)
				fos.close();
			if (bos != null)
				bos.close();
			if (sock != null)
				sock.close();
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

	private static String getIP()
	{
		String ipAddr = "";
		boolean validIPAddr = false;
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter an IP address: ");
		while (validIPAddr== false)
		{
			try
			{
				ipAddr = input.readLine();
	        	String[] pieces = ipAddr.split("\\.");
				
				for(String s: pieces)
				{
					int i = Integer.parseInt(s);
					if((i < 0) || i > 255){
						System.out.println("IP address parts cannot be smaller than 0 or larger than 255. \nEnter an IP Address: ");
						break;
					}
				}
				
				if(ipAddr == null || ipAddr.isEmpty())
				{
					System.out.println("IP address is empty. \nEnter an IP Address: ");
				}
				else if(pieces.length != 4)
				{
					System.out.println("IP address does not contain correct number of parts. \nEnter an IP Address: ");
				}
				
				else if(ipAddr.endsWith("."))
				{
					System.out.println("IP addresses cannot end with a period. \nEnter an IP Address: ");
				}
				
				else
				{
					validIPAddr = true;
				}


			}
			catch (NumberFormatException e)
			{
				System.out.println("Invalid IP. \nEnter an IP address: ");
			}
			catch (IOException e)
			{
				System.out.println("Invalid IP. \nEnter an IP address: ");
			}
		}
		return ipAddr;
	}
} 
