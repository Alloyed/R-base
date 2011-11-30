package client.sprites;

import java.io.File;
import java.util.HashMap;

import client.Main;


public class Skin {
	public HashMap<String, Sprite> sprites;
	
	public Skin(Main p) {
		sprites = new HashMap<String, Sprite>();
		File dir = new File("data/images/"+p.settings.SKIN_FOLDER+"/");
		
		if(dir.exists())
			for (File f : dir.listFiles())
				if(f.getName().endsWith(".png") || f.getName().endsWith(".svg"))
					sprites.put(f.getName().split("\\.")[0], new ImageSprite(p, f.toString()));
		sprites.put("none", new EmptySprite());
	}
}
