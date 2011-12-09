package newNet;

import java.lang.reflect.Method;

import physics.Stage;
import physics.actors.Actor;

/*RPC is remote procedure call*/
public class RPC {
	int id;
	int stamp; //For storing
	String method;
	Object[] args;
	
	public RPC(Actor a, String method, Object[] args) {
		id = a.id;
		this.method = method;
		this.args = args;
	}
	
	public RPC(Stage s, String method, Object[] args) {
		id = -1;
		this.method = method;
		this.args = args;
	}
	
	public Object call(Stage s) {
		Object obj = s.get(id);
		Class<?>[] partypes = new Class[args.length];
		for(int i = 0; i < args.length; i++)
			partypes[i] = args[i].getClass();
		
		try {
			Method m = obj.getClass().getDeclaredMethod(method, partypes);
			Object o = m.invoke(obj, args);
			return o;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
