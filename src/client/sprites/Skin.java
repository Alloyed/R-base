package client.sprites;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.ResourceLoader;

import physics.Console;
import physics.actors.Actor;
//import processing.core.PApplet;

import client.ui.Loop;


public class Skin extends Thread {
	//TODO: non-image promises
	class ImagePromise {
		public String key, file;
		public ImagePromise(String k, String f) {
			key = k; file = f;
		}
	}
	
	LinkedList<ImagePromise> toLoad;
	Map<String, Sprite>  sprites;
	HashMap<String, Color> colors;
	Loop c;
	
	public Skin(Loop c) {
		//Sprites
		this.c = c;
		String path = "images/"+c.settings.SKIN_FOLDER+"/";
		//colors
		colors = new HashMap<String, Color>();
		InputStream colorFile = ResourceLoader
				.getResourceAsStream(path+"colors.txt");
		Scanner sc = new Scanner(colorFile);
		while (sc.hasNext()) {
				String s = sc.nextLine();
				String[] split = s.split(":");
				if (s.length() > 0 && s.charAt(0) != '#' && split.length == 2) {
					Color i = null;
					if (split[1].charAt(0) == '#') { //Hex
						String hex = split[1].substring(1);
						int a = 255;
						if (hex.length() == 8)
							a = Integer.parseInt(hex.substring(6, 8), 16);
						int r = Integer.parseInt(hex.substring(0, 2), 16);
						int g = Integer.parseInt(hex.substring(2, 4), 16);
						int b = Integer.parseInt(hex.substring(4, 6), 16);
						i = new Color(r, g, b, a);			
					} else if (split[1].charAt(0) == '$') { //var
						i = getColor(split[1].substring(1));
					} else { //int
						int j = Integer.parseInt(split[1]);
						i = new Color(j, j, j);
					}
					colors.put(split[0], i);
				}
		}
		try {colorFile.close();} catch (IOException e) {e.printStackTrace();}
		
		//Sprites
		sprites = Collections.synchronizedMap(new HashMap<String, Sprite>());
		toLoad = new LinkedList<ImagePromise>();
		//Special cases
		sprites.put("none", new EmptySprite());
		sprites.put("map", new MapSprite(c));
		sprites.put("prop", new RectSprite(c,getColor("wall")));
		sprites.put("floor", new RectSprite(c,getColor("bg")));
		sprites.put("floor-blue", new RectSprite(c,getColor("bg-blue")));
		sprites.put("floor-orange", new RectSprite(c,getColor("bg-orange")));
		
		//c.font = p.createFont("uni05_53.ttf",8,false);
		//p.textFont(c.font);
		
		File dir = new File("data/" + path);
		if(dir.exists()) {
			for (File f : dir.listFiles()) {
				if(f.getName().endsWith(".png") || f.getName().endsWith(".svg") || f.getName().endsWith(".svgz")) {
					String s = f.getName().split("\\.")[0];
					toLoad.add(new ImagePromise(s, f.toString()));
				}
			}
		}
	}
	
	/**
	 * Loads the next resource. returns true as soon as it's done
	 */
	public boolean next() {
		ImagePromise p = toLoad.poll();
		sprites.put(p.key, new ImageSprite(c, p.file));
		return toLoad.isEmpty();
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
	
	public void draw(Graphics g, Actor a) {
		get(a).draw(g, a);
	}
	
	public Color getColor(String s) {
		Color i = colors.get(s);
		if ( i == null ) {
			Console.dbg.println("WARNING: color " + s + " doesn't exist. Using black instead.");
			colors.put(s, Color.black);
		}
		return i;
	}
}
