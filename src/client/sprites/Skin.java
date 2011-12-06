package client.sprites;

import java.io.File;
import java.util.HashMap;

import physics.Console;
import physics.actors.Actor;

import client.Client;


public class Skin {
	public HashMap<String, Sprite> sprites;
	
	public Skin(Client p) {
		sprites = new HashMap<String, Sprite>();
		File dir = new File(p.p.sketchPath + "/data/images/"+p.settings.SKIN_FOLDER+"/");
		if(dir.exists())
			for (File f : dir.listFiles())
				if(f.getName().endsWith(".png") || f.getName().endsWith(".svg"))
					sprites.put(f.getName().split("\\.")[0], new ImageSprite(p, f.toString()));
		//Special cases
		sprites.put("none", new EmptySprite());
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
}
