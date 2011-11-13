package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/* TODO: handle events, respond to the client. */

public class ServerThread extends Thread {
	Socket client;
	DataOutputStream	streamOut;
	DataInputStream		streamIn;
	
	ServerThread(Socket s) throws IOException {
		client = s;
		streamOut = new DataOutputStream(client.getOutputStream());
		streamIn = new DataInputStream(client.getInputStream());
	}
	
	public void run() {
		while(true) {
			try {
				getEvent();
			} catch (IOException e) {
				System.err.println("Error reading events from" + client.getInetAddress());
			}
		}
	}
	
	void getEvent() throws IOException {
		String s = streamIn.readUTF();
		handleEvent(s);
	}
	
	void handleEvent(String s) {
		// TODO: this stuff
	}
}
