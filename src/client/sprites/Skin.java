package client.sprites;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import physics.Console;
import physics.actors.Actor;
//import processing.core.PApplet;

import client.Client;


public class Skin extends Thread {
	Map<String, Sprite>  sprites;
	HashMap<String, Integer> colors;
	Client c;
//	PApplet p;
	
	public Skin(Client c) {
		//Sprites
		this.c = c;
/*		this.p = c.p;
//		String path = c.p.sketchPath + "/data/images/"+c.settings.SKIN_FOLDER+"/";
		
		//colors
		colors = new HashMap<String, Integer>();
		InputStream colorFile = c.p.createInput(path+"colors.txt");
		for (String s : PApplet.loadStrings(colorFile)) {
				String[] split = s.split(":");
				if (s.length() > 0 && s.charAt(0) != '#' && split.length == 2) {
					Integer i = 0;
					if (split[1].charAt(0) == '#') { //Hex
						String hex = split[1].substring(1);
						int a = 255;
						if (hex.length() == 8)
							a = Integer.parseInt(hex.substring(6, 8), 16);
						int r = Integer.parseInt(hex.substring(0, 2), 16);
						int g = Integer.parseInt(hex.substring(2, 4), 16);
						int b = Integer.parseInt(hex.substring(4, 6), 16);
						i = p.color(a << 24 | r << 16 | g << 8 | b);				
					} else if (split[1].charAt(0) == '$') { //var
						i = getColor(split[1].substring(1));
					} else { //int
						i = p.color(Integer.parseInt(split[1]));
					}
					colors.put(split[0], i);
				}
		}
		try {colorFile.close();} catch (IOException e) {e.printStackTrace();}
		
		//Sprites
		sprites = Collections.synchronizedMap(new HashMap<String, Sprite>());
		//Special cases, rest are handled by thread
		sprites.put("none", new EmptySprite());
		sprites.put("logo", new ImageSprite(c, path+"logo.png"));
		sprites.put("box", new ImageSprite(c, path+"box.svg"));
		sprites.put("prop", new RectSprite(c,getColor("wall")));
		sprites.put("floor", new RectSprite(c,getColor("bg")));
		sprites.put("floor-blue", new RectSprite(c,getColor("bg-blue")));
		sprites.put("floor-orange", new RectSprite(c,getColor("bg-orange")));
	*/
	}
	
	public void run() {
		/*String path = p.sketchPath + "/data/images/"+c.settings.SKIN_FOLDER+"/";
		File dir = new File(path);
		c.font = p.createFont("uni05_53.ttf",8,false);
		p.textFont(c.font);
		if(dir.exists())
			for (File f : dir.listFiles())
				if(f.getName().endsWith(".png") || f.getName().endsWith(".svg")) {
					String s = f.getName().split("\\.")[0];
					sprites.remove(s);
					sprites.put(s, new ImageSprite(c, f.toString()));
				}
		Console.dbg.println("Skins loaded.");
		*/
	}
	
	public Sprite get(String str) {
		Sprite s = sprites.get(str);
		if (s == null) {
			Console.dbg.print("WARNING: sprite " + str + " doesn't exist. ");
			s = sprites.get(str.split("-")[0]);
			if (s == null) {
				Console.dbg.println("Using default instead.");
				s = sprites.get("box");
				if (s == null) {
					Console.dbg.println("Lol nvm, using empty sprite.");
					s = sprites.get("none");
				}
			} else {
				Console.dbg.println("Using base instead.");
			}
			sprites.put(str,s);
		}
		return s;
	}
	
	public Sprite get(Actor a) {
		return get(a.getImage());
	}
	
	public int getColor(String s) {
		Integer i = colors.get(s);
		if ( i == null ) {
			Console.dbg.println("WARNING: color " + s + " doesn't exist. Using black instead.");
		//	i = p.color(0);
			colors.put(s, i);
		}
		return i;
	}
}
