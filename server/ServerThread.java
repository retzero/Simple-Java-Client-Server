import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * Individual ServerThread listens for the client to tell it what command to run, then
 * runs that command and sends the output of that command to the client
 *
 */
public class ServerThread extends Thread {
	Socket client = null;
	
	public ServerThread(Socket client) {
		this.client = client;
	}
	
	public void run() {
		System.out.print("Accepted connection. ");

		try {
			// open a new PrintWriter and BufferedReader on the socket
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			System.out.print("Reader and writer created. ");

			String inString;
			// read the command from the client
		        while  ((inString = in.readLine()) == null);
			System.out.println("Read command " + inString);

			// run the command using CommandExecutor and get its output
			String outString = CommandExecutor.run(inString);
			System.out.println("Server sending: " + outString);
			// send the result of the command to the client
			out.print(outString);
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			// close the connection to the client
			out.close();
			in.close();
			client.close();
			System.out.println("Output closed.");
		}

	}
}
