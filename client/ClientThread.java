import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClientThread extends Thread {
	int menuSelection;
	String hostName;
	private static Socket socket = null;
	
	ClientThread(String hostName, int menuSelection) {
		this.menuSelection = menuSelection;
		this.hostName = hostName;
	}

	public void run() {
		PrintWriter out = null;
        BufferedReader in = null;
        try
        {
                //creates a new Socket object and names it socket.
                //Establishes the socket connection between the client & server
                //name of the machine & the port number to which we want to connect
                socket = new Socket(hostName, 4000);
                System.out.print("Establishing connection.");            
                out = new PrintWriter(socket.getOutputStream(), true);//opens a PrintWriter 
                                                                        //on the socket

                //opens a BufferedReader on the socket
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("\nRequesting output for the '" + menuSelection + "' command from " + hostName);
                // send the command to the server
                out.println(Integer.toString(menuSelection));
                out.flush();
                System.out.println("Sent output");
                // read the command from the server
                String outputString = "hi";
                while (((outputString = in.readLine()) != null) && (!outputString.equals("END_MESSAGE"))) {
                        System.out.println(outputString);
                        System.out.println("Input read");
                }
                System.out.println("closing");
                try {
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                out.close();
                in.close();
                socket.close();

        }
        catch (UnknownHostException e)
        {
                System.err.println("Unknown host: " + e);
                System.exit(1);
        }
        catch (IOException e)
        {
                e.printStackTrace();
        }
		
	}

}
