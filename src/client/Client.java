package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class Client {
	DataOutputStream out;
	DataInputStream in;
	
	Client(String ip, int port) throws IOException {
		Socket s = new Socket(ip, port);
		out = new DataOutputStream(s.getOutputStream());
		in = new DataInputStream(System.in);
	}
	
}
