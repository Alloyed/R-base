package newNet;

import physics.Console;
import physics.Team;

public class Message {
	String from;
	//Me love you
	long time;
	Team room;
	String body;
	
	public Message(String message) {
		from = "DSD";
		body = message;
	}
	
	public void print() {
		Console.chat.println(from + " : " + body);
	}
}
