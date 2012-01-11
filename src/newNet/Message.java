package newNet;

import physics.Console;
import physics.Team;
/**
 * A chat message.
 * TODO: team rooms, non-chat messages(The server ones should get there own window), timestamps
 * @author kyle
 *
 */
public class Message {
	String from;
	//Me love you
	long time;
	Team room;
	String body;
	public Message() {
		
	}
	
	public Message(String player, String message) {
		from = player;
		body = message;
	}
	
	public void print() {
		Console.chat.println(from + " : " + body);
	}
}
