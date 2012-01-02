package physics;

public class Message {
	String origin;
	Team room;
	String body;
	
	public Message() {
		
	}
	
	public void write() {
		Console.chat.println(origin + " : " + body);
	}
}
