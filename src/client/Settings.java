package client;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Enumeration;

import controlP5.ControlElement;
import physics.Console;
import processing.core.PApplet;

import nanoxml.XMLElement;

/*Client settings*/
public class Settings {
	/* Keys
	 * TODO: make UP or W valid entries
	 */
	public Integer 
		UP           = 87,
		DOWN         = 83,
		LEFT         = 65,
		RIGHT        = 68,
		USE          = 69,
		CHAT         = 84,
		SCREENSHOT   = 114;
	@ControlElement (x=30,y=220, properties = {"label=width", "width=40", "listen=true"})
	public String WINDOW_WIDTH = "800";
	@ControlElement (x=80,y=220, properties = {"label=height", "width=40", "listen=true"})
	public String WINDOW_HEIGHT = "600";
	
	@ControlElement (x=650,y=70, properties={"width=80", "listen=true"})
	public String IP = "localhost";
	@ControlElement (x=740,y=70, properties={"width=30", "listen=true"})
	public String PORT = "9001";
	
	@ControlElement (x=650,y=30, properties={"width=120"})
	public String USERNAME="Player";
	@ControlElement (x=100,y=260,label="Point at cursor?", 
			properties={"height=20","width=20"})
	public boolean ROTATE_FORCE = true;
	
	@ControlElement (x=30,y=260, properties = {"label=skin", "width=60", "listen=true"})
	public String SKIN_FOLDER = "skin";
	@ControlElement (x=30,y=310, properties = {"label=Team?", "min=0", "max=1", "width=60", "listen=true"})
	public int team = 0;
	@ControlElement (x=30,y=330,label="Fixed timestep?", 
			properties={"height=20","width=20"})
	public boolean FIXED_TIMESTEP = false;
	
	private PApplet p;
	

	/* Note: No absolute filenames. */
	public Settings(PApplet p) {
		this.p = p;
		XMLElement x = new XMLElement();
		try {
			Reader f = p.createReader("data/ClientSettings.xml");
			x.parseFromReader(f);
			@SuppressWarnings("unchecked")
			Enumeration<XMLElement> e = x.enumerateChildren();
			while (e.hasMoreElements()) {
				XMLElement x2 = e.nextElement();
				String name = x2.getName();
				String type = x2.getStringAttribute("type");
				String tmp = x2.getContent();
				Object value;
				if (type.equals("class java.lang.Boolean"))
					value = Boolean.parseBoolean(tmp);
				else if (type.equals("class java.lang.Integer"))
					value = Integer.parseInt(tmp);
				else if (type.equals("class java.lang.Float"))
					value = Float.parseFloat(tmp);
				else if (type.equals("class java.lang.Double"))
					value = Double.parseDouble(tmp);
				else
					value = tmp;
				getClass().getField(name).set(this, value);
			}
			f.close();
			Console.out.println("Settings read.");
		} catch (Exception e) {
			e.printStackTrace();
			Console.out.println("Settings could not be read, using defaults.");
		}
	}

	public void save() {
		XMLElement top = new XMLElement();
		top.setName("R-base");
		for (Field f : this.getClass().getDeclaredFields()) {
			if (!f.getName().equals("p")) { // 1 exception
				XMLElement child = new XMLElement();
				child.setName(f.getName());
				try {
					child.setAttribute("type", f.get(this).getClass());
					child.setContent(f.get(this).toString());
				} catch (Exception e) {
					;
				}
				top.addChild(child);
			}
		}

		try {
			Writer w = p.createWriter("data/ClientSettings.xml");
			top.write(w);
			w.close();
			Console.out.println("Settings Saved");
		} catch (IOException e1) {
			e1.printStackTrace();
			Console.out.println("Settings not saved.");
		}
	}
}
