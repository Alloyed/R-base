package client;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import controlP5.ControlP5XMLElement;


public class Settings extends HashMap<String, Object> {
	/*
	 * Retrieve settings by doing settings.get("name").
	 * 
	 * To make a new setting, Copy the idom in Main that I'm using for all the other settings, 
	 *   then write in a setting in Settings.xml. Defaults don't get saved right now.
	 * 
	 * I know no better way of doing this, short of embedding lua or something
	 */
	private static final long serialVersionUID = 7180483327057889051L;
	String file;
	Settings(String s) 
	{
		super();
		file = s;
		ControlP5XMLElement x = new ControlP5XMLElement(); //This is just NanoXML renamed
		try {
			FileReader f = new FileReader(file);
			x.parseFromReader(f);
			@SuppressWarnings("unchecked")
			Enumeration<ControlP5XMLElement> e = x.enumerateChildren();
			while (e.hasMoreElements()) {
				ControlP5XMLElement x2 = e.nextElement();
				String name = x2.getName();
				String type = (String)x2.getAttribute("type");
				String value = x2.getContent();
				if (type.equals("class java.lang.Boolean"))
					put(name, Boolean.parseBoolean(value));
				else if (type.equals("class java.lang.Int"))
					put(name, Integer.parseInt(value));
				else if (type.equals("class java.lang.Float"))
					put(name, Float.parseFloat(value));
				else if (type.equals("class java.lang.Double"))
					put(name, Double.parseDouble(value));
				else 
					put(name, value);
			}
			System.out.println("Settings read.");
			
		} catch (IOException e) {
			System.out.println("Settings could not be read, using defaults.");
			put("ROTATE_FORCE", new Boolean(true));
		}
	}
	
	public void save()
	{
		ControlP5XMLElement top = new ControlP5XMLElement();
		top.setName("R-base");
		for (Map.Entry<String,Object> e: entrySet()) {
			ControlP5XMLElement child = new ControlP5XMLElement();
			child.setName(e.getKey());
			child.setAttribute("type", e.getValue().getClass());
			child.setContent(e.getValue().toString());
			top.addChild(child);
		}
		System.out.println(top.toString());
		try {
			FileWriter w = new FileWriter(file);
			top.write(w);
			w.close();
			System.out.println("Settings Saved");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Settings not saved.");
		}
	}
}
