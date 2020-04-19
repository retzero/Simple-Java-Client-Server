package com.example.hyokeun.asr.SpeechToText;

import com.example.hyokeun.asr.SpeechToText.Dto.SpeechToTextControlMessageDto;
import com.example.hyokeun.asr.SpeechToText.Dto.SpeechToTextResponseDto;
import com.example.hyokeun.asr.SpeechToText.Dto.SpeechToTextReturnedWordsDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.google.gson.JsonParser;

import java.util.Date;

//import org.codehaus.jackson.map.ObjectMapper; 
import com.fasterxml.jackson.databind.ObjectMapper;



/*
 * Individual ServerThread listens for the client to tell it what command to run, then
 * runs that command and sends the output of that command to the client
 *
 */
public class ServerThread extends Thread {
	private final static long CONTEXT_MESSAGE_HEADER = 0x0000FFFFL;
	
	Socket client = null;
	
	private InputStream m_sock_in = null;
	private OutputStream m_sock_out = null;
	
	PrintWriter output;
	BufferedReader input;
	private SpeechToTextControlMessageDto controlMsgDto = null;
	private long len = 0;
	
	public ServerThread(Socket client) throws IOException {
		this.client = client;
		this.controlMsgDto = new SpeechToTextControlMessageDto();
	}


	// little endian
	private byte[] intToByteArray(int value) {
		byte[] byteArray = new byte[4];
		byteArray[3] = (byte) (value >> 24);
		byteArray[2] = (byte) (value >> 16);
		byteArray[1] = (byte) (value >> 8);
		byteArray[0] = (byte) (value);
		return byteArray;
	}

	private int byteArrayToInt(byte bytes[]) {
		if(bytes != null && bytes.length >= 4){
			return ((((int) bytes[3] & 0xff) << 24) |
				(((int) bytes[2] & 0xff) << 16) |
				(((int) bytes[1] & 0xff) << 8) |
				(((int) bytes[0] & 0xff)));
		} else {
			return 0;
		}
	}

	private byte[] recvBytes(int len) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer;
		int total_length = 0;
		while (len != total_length && !client.isClosed()) {

			buffer = new byte[len];
			int temp_length = -1;
			try {
				if(!client.isInputShutdown()) {
					temp_length = m_sock_in.read(buffer);
				}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			if(temp_length==-1){
				break;
			}
			if (temp_length > 0) {
				total_length += temp_length;
				baos.write(buffer, 0, temp_length);
			}
		}
		return baos.toByteArray();
	}

	// recv a 32-bit int
	private int recvInt32() throws IOException {
		byte[] b32 = this.recvBytes(4);
		int i32 = byteArrayToInt(b32);
		return i32;
	}

	private void sendBytes(byte[] buf, int len, boolean flush) throws IOException {
		if(!client.isClosed()) {
			m_sock_out.write(buf, 0, len);
			if (flush) {
				m_sock_out.flush();
			}
		}
	}

	private void sendInt32(int n, boolean flush) throws IOException {
		this.sendBytes(intToByteArray(n), 4, flush);
	}

	public int recvLength() throws IOException {
		int len = (int)((long)(this.recvInt32()) & CONTEXT_MESSAGE_HEADER);
		return len;
	}
	
	public void recvControlMsg() throws IOException {
		int len = this.recvLength();
		JsonParser parser = new JsonParser();
		byte[] clientData = this.recvBytes(len);
		String decoded = new String(clientData, "UTF-8");
		System.out.println(decoded);
		// TODO: Fill into this.controlMsgDto
	}

	private void recvAudioData() throws IOException {
		int totalLength = 0;
		int len = this.recvLength();
		while (len > 0) {
			totalLength += len;
			byte[] clientData = this.recvBytes(len);
			len = this.recvLength();
		}
		System.out.println("Total Audio data length: " + totalLength);
	}
	
	public void sendSttResult() {
		SpeechToTextResponseDto dtos = new SpeechToTextResponseDto();

		Date date = new Date();
		long time = date.getTime();
		//String returnStr = "Hello " + time;
		
		dtos.setCancel(false);
		dtos.setMemoryFailure(false);
		dtos.setNSample(1);

		ArrayList<SpeechToTextReturnedWordsDto> pitches = new ArrayList<SpeechToTextReturnedWordsDto>();
		ArrayList<String> words = new ArrayList<String>();
		words.add("Hello");
		words.add("World");
		words.add(" " + time);
		SpeechToTextReturnedWordsDto rwd = new SpeechToTextReturnedWordsDto();
		rwd.setConfidence(50);
		rwd.setExtHypothesis("OK1");
		rwd.setHypothesis("OK2");
		rwd.setWord(words);
		rwd.setFinal(true);
		pitches.add(rwd);

		dtos.setNbest(pitches);

		ObjectMapper Obj = new ObjectMapper();
		//Obj.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		String jsonStr = null;
		try { 
			jsonStr = Obj.writeValueAsString(dtos); 
			System.out.println(jsonStr); 
		} 
		catch (IOException e) { 
			e.printStackTrace(); 
		}
		System.out.println("Jackson string: " );
		System.out.println(jsonStr);

		byte b1[] = jsonStr.getBytes();

		try {
			this.sendInt32(b1.length, false);
			this.sendBytes(b1, b1.length, true);
		}
		catch (IOException e) {
			e.printStackTrace(); 
		}
	}
	

	public void run() {
		System.out.print("Accepted connection. ");

		try {
			// open a new PrintWriter and BufferedReader on the socket
			/*
			output = new PrintWriter(client.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			*/
			
			try {
				this.m_sock_in = client.getInputStream();
				this.m_sock_out = client.getOutputStream();
				System.out.print("Reader and writer created. ");
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			String inString;

			this.recvControlMsg();
			this.recvAudioData();
			
			this.sendSttResult();

			/*
		        while  ((inString = input.readLine()) == null);
			System.out.println("Read command " + inString);

			// run the command using CommandExecutor and get its output
			String outString = CommandExecutor.run(inString);
			System.out.println("Server sending result to client");
			try {
				Thread.sleep(3*1000);
			}
			catch (InterruptedException e) {
				System.out.println("Sleep failed...");
			}
			// send the result of the command to the client
			output.println(outString);
			*/
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			// close the connection to the client
			try {
				client.close();
			}
			catch (IOException e) {
				e.printStackTrace();	
			}			
			System.out.println("Output closed.");
		}

	}
}

