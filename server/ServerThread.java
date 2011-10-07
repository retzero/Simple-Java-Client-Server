import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;


public class ServerThread extends Thread {
	Socket client = null;
	
	public ServerThread(Socket client) {
		this.client = client;
	}
	
	public void run() {
		System.out.print("Accepted connection. ");

		try {
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			System.out.print("Reader and writer created. ");

			String inString = in.readLine();
				System.out.println("Read command " + inString);

				String outString = CommandExecutor.run(inString);
				System.out.println("Server sending: " + outString);
				out.print(outString);
			out.flush();

			out.close();
			in.close();
			client.close();
			System.out.println("Output closed.");

		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
}
