import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AtomicInteger numThreads = new AtomicInteger(0);
		// the list of threads is kept in a linked list
		ArrayList<Thread> list = new ArrayList<Thread>();
		
		try {
			ServerSocket socket = new ServerSocket(4000);
			System.out.println("Server listening on port 4000");

			while(true) {
				Socket client = socket.accept();
				Thread thrd = new Thread(new ServerThread(client));
				list.add(thrd);
				thrd.start();
				numThreads.incrementAndGet();
				System.out.println("Thread " + numThreads.get() + " started.");

			}
		}
		catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
}