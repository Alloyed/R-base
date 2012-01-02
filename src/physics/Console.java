package physics;

import java.io.PrintStream;

/**
 * A redirectable series of output streams.
 * We aren't just overriding System.out etc. 
 * because then we would take everyone elses errors along with it.
 * 
 * @author kyle
 *
 */

public class Console {
	public static PrintStream out = System.out;  //Main messages
	public static PrintStream dbg = System.err;  //Debugging messages
	public static PrintStream chat = System.out; //Chat window
	
	public static void setDest(PrintStream dest) {
		out = dest;
		dbg = dest;
		chat = dest;
	}
	
	public static void chat(String origin, long time, String message) {
		chat.println(origin+" : "+message);
	}
}
