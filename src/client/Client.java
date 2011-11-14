package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import processing.core.PApplet;

public class Client {
	DataOutputStream out;
	DataInputStream in;
	
	Client(String ip, int port) throws IOException {
		Socket s = new Socket(ip, port);
		out = new DataOutputStream(s.getOutputStream());
		in = new DataInputStream(System.in);
	}
	
	void start() {
		PApplet.main(new String[] { "--present", "--hide-stop", "client.Runner" });
	}
	
	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		Client c = new Client(in.next(), 9001);
		c.start();
	}
}
