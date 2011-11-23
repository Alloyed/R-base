package client;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Enumeration;

import controlP5.ControlElement;

import nanoxml.XMLElement;

/*Client settings*/
public class Settings {
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
	
	public boolean USE_OPENGL = false;
	
	@ControlElement (x=30,y=260, properties = {"label=skin", "width=60", "listen=true"})
	public String SKIN_FOLDER = "skin";
	
	private String file;

	/* Note: No absolute filenames. */
	public Settings(String s) {

		file = "data/" + s;
		XMLElement x = new XMLElement();
		try {
			FileReader f = new FileReader(file);
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
			System.out.println("Settings read.");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Settings could not be read, using defaults.");
		}
	}

	public void save() {
		XMLElement top = new XMLElement();
		top.setName("R-base");
		for (Field f : this.getClass().getDeclaredFields()) {
			if (f.getName() != "file") { // 1 exception
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
			FileWriter w = new FileWriter(file);
			top.write(w);
			w.close();
			System.out.println("Settings Saved");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Settings not saved.");
		}
	}
}
