package com.example.hyokeun.asr;

import com.example.hyokeun.asr.SpeechToText.ServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


/* 
 * Main server class. This class includes main(), and is the class that listens
 * for incoming connections and starts ServerThreads to handle those connections
 *
 */
public class SpeechToTextService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AtomicInteger numThreads = new AtomicInteger(0);
		// the list of threads is kept in a linked list
		ArrayList<Thread> list = new ArrayList<Thread>();

		int port_no_to_use = 15432;
		if (args.length != 0) {
			port_no_to_use = Integer.parseInt(args[0]);
		}
		
		try {
			// listen for incoming connections on port 15432
			ServerSocket socket = new ServerSocket(port_no_to_use);
			System.out.println("\nServer listening on port " + port_no_to_use);

			// loop (forever) until program is stopped
			while(true) {
				// accept a new connection
				Socket client = socket.accept();
				// start a new ServerThread to handle the connection and send
				// output to the client
				Thread thrd = new Thread(new ServerThread(client));
				list.add(thrd);
				thrd.start();
				numThreads.incrementAndGet();
				System.out.println("\nThread " + numThreads.get() + " started.");

			}
		}
		catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
}

