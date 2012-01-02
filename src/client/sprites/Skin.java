package client.sprites;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.util.ResourceLoader;

import physics.Console;
import physics.actors.Actor;
//import processing.core.PApplet;

import client.ui.Loop;


public class Skin {
	//TODO: non-image promises
	interface Promise {
		public void fulfill();
	}
	
	class ImagePromise implements Promise {
		public String key, file;
		public ImagePromise(String k, String f) {
			key = k; file = f;
		}
		public void fulfill() {
			sprites.put(key, new ImageSprite(c, file));
		}
	}
	
	class SoundPromise implements Promise {
		public String key, file;
		public SoundPromise(String k, String f) {
			key = k; file = f;
		}

		public void fulfill() {
			try {
				sounds.put(key, new Sound(file));
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	LinkedList<Promise> toLoad;
	Map<String, Sprite>  sprites;
	Map<String, Sound> sounds;
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
		sprites = new HashMap<String, Sprite>();
		sounds  = new HashMap<String, Sound>();
		toLoad  = new LinkedList<Promise>();
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
				} else if (f.getName().endsWith(".ogg") || f.getName().endsWith(".wav")) {
					String s = f.getName().split("\\.")[0];
					Console.out.println("Sound" + s);
					toLoad.add(new SoundPromise(s, f.toString()));
				}
			}
		}
	}
	
	/**
	 * Returns the next resource
	 * @return true if there are no more resources to load, false is not
	 */
	public boolean next() {
		Promise p = toLoad.poll();
		p.fulfill();
		return toLoad.isEmpty();
	}
	
	/**
	 * Gets a visual representation of something
	 * @param str the type of thing, including modifiers
	 * @return the Sprite of the thing
	 */
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
	
	/**
	 * Gets a visual representation of an thing
	 * @param str The thing
	 * @return the Sprite of the thing
	 */
	public Sprite get(Actor a) {
		return get(a.getImage());
	}
	
	/**
	 * Draws an Actor onto Graphics g
	 * @param g A graphics context
	 * @param a The actor to draw
	 */
	public void draw(Graphics g, Actor a) {
		get(a).draw(g, a);
	}
	
	/**
	 * Plays a sound effect
	 * @param name the sound effects name
	 */
	public void play(String name) {
		Sound s = sounds.get(name);
		if (s != null) {
			s.play();
		}
	}
	
	/**
	 * Plays a sound effect in a certain spot in the world.
	 * Absolute coordinates only. 
	 * If you want a sound with a reference point, you'll need to set it up yourself
	 * @param name the sound
	 * @param spot the spot where it will be played
	 */
	public void playAt(String name, Vec2 spot) {
		Sound s = sounds.get(name);
		if (s != null) {
			s.playAt(spot.x, spot.y, 0);
		}
	}
	
	/**
	 * Plays the song, stopping the current song if necessary
	 * @param name the name of the song
	 */
	public void setSong(String name) {
		//TODO
	}
	
	/**
	 * Returns a color from colors.txt
	 * @param s the name of the color
	 * @return the color
	 */
	public Color getColor(String s) {
		Color i = colors.get(s);
		if ( i == null ) {
			Console.dbg.println("WARNING: color " + s + " doesn't exist. Using black instead.");
			colors.put(s, Color.black);
		}
		return i;
	}
}
