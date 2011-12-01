package physics;

import java.io.PrintStream;

public class Console {
	public static PrintStream out = System.out;  //Main messages
	public static PrintStream dbg = System.out;  //Debugging messages
	public static PrintStream chat = System.out; //Chat window
	
	public static void setDest(PrintStream dest) {
		out = dest;
		dbg = dest;
		chat = dest;
	}
	
	public static void chat(String origin, long time, String message) {
		chat.println("<"+origin+">"+message);
	}
}
