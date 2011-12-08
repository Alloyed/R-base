package client.sprites;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import physics.Console;
import physics.actors.Actor;
import processing.core.PApplet;

import client.Client;


public class Skin {
	HashMap<String, Sprite>  sprites;
	HashMap<String, Integer> colors;
	PApplet p;
	public Skin(Client c) {
		//Sprites
		this.p = c.p;
		sprites = new HashMap<String, Sprite>();
		String path = c.p.sketchPath + "/data/images/"+c.settings.SKIN_FOLDER+"/";
		File dir = new File(path);
		if(dir.exists())
			for (File f : dir.listFiles())
				if(f.getName().endsWith(".png") || f.getName().endsWith(".svg"))
					sprites.put(f.getName().split("\\.")[0], new ImageSprite(c, f.toString()));
		//Special cases
		sprites.put("none", new EmptySprite());
					
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
	}
	
	public Sprite get(String str) {
		Sprite s = sprites.get(str);
		if (s == null) {
			Console.dbg.print("WARNING: sprite " + str + " doesn't exist. ");
			s = sprites.get(str.split("-")[0]);
			if (s == null) {
				Console.dbg.println("Using default instead.");
				s = sprites.get("box");
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
			i = p.color(0);
			colors.put(s, i);
		}
		return i;
	}
}
