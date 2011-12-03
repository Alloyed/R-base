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
		File dir = new File("data/images/"+p.settings.SKIN_FOLDER+"/");
		
		if(dir.exists())
			for (File f : dir.listFiles())
				if(f.getName().endsWith(".png") || f.getName().endsWith(".svg"))
					sprites.put(f.getName().split("\\.")[0], new ImageSprite(p, f.toString()));
		sprites.put("none", new EmptySprite());
	}
	
	public Sprite get(Actor a) {
		Sprite s = sprites.get(a.getImage());
		if (s == null) {
			Console.dbg.print("WARNING: sprite " + a.getImage() + " doesn't exist. ");
			s = sprites.get(a.baseImage);
			if (s == null) {
				Console.dbg.println("Using default instead.");
				s = sprites.get("box");
			} else {
				Console.dbg.println("Using base instead.");
			}
			sprites.put(a.getImage(),s);
		}
		return s;
	}
}
