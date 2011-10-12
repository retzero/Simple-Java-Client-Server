import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicLong;


public class ClientThread extends Thread {
	int menuSelection;
	String hostName;
	Socket socket = null;
	AtomicLong totalTime;
	AtomicLong runningThreads;
	boolean printOutput;

	long startTime;
	long endTime;

	ClientThread(String hostName, int menuSelection, AtomicLong totalTime, boolean printOutput, AtomicLong runningThreads) {
		this.menuSelection = menuSelection;
		this.hostName = hostName;
		this.totalTime = totalTime;
		this.printOutput = printOutput;
		this.runningThreads = runningThreads;
	}

	public void run() {
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			startTime = System.currentTimeMillis();

			//creates a new Socket object and names it socket.
			//Establishes the socket connection between the client & server
			//name of the machine & the port number to which we want to connect
			socket = new Socket(hostName, 4000);
			if (printOutput) System.out.print("Establishing connection.");
			//opens a PrintWriter on the socket in autoflush mode
			out = new PrintWriter(socket.getOutputStream(), true);

			//opens a BufferedReader on the socket
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			if (printOutput) System.out.println("\nRequesting output for the '" + menuSelection + "' command from " + hostName);

			// send the command to the server
			out.println(Integer.toString(menuSelection));
			if (printOutput) System.out.println("Sent output");

			// read the output from the server
			String outputString = "";
			while (((outputString = in.readLine()) != null) && (!outputString.equals("END_MESSAGE"))) {
				if (printOutput) System.out.println(outputString);
			}

			endTime = System.currentTimeMillis();
			totalTime.addAndGet(endTime - startTime);

		}
		catch (UnknownHostException e) {
			System.err.println("Unknown host: " + e);
			System.exit(1);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (printOutput) System.out.println("closing");
			try {
				socket.close();
				runningThreads.decrementAndGet();
				System.out.flush();
			}
			catch (IOException e ) {
				System.out.println("Couldn't close socket");
			}
		}

	}

}
