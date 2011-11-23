package physics;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Console {
	static DataOutputStream out = new DataOutputStream(System.out);
	
	public static void setDest(OutputStream dest) {
		out = new DataOutputStream(dest);
	}
	
	public static boolean println(String s) {
		try {
			out.writeChars(s);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static boolean print(String s) {
		try {
			out.writeChars(s);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
