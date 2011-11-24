package physics;

import java.io.PrintStream;

public class Console {
	public static PrintStream out = System.out;
	
	public static void setDest(PrintStream dest) {
		out = dest;
	}
}
