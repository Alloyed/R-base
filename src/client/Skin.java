package client;

import java.io.File;
import java.util.HashMap;

public class Skin {
	public HashMap<String, Sprite> sprites;
	
	public Skin(Runner p, Settings s) {
		sprites = new HashMap<String, Sprite>();
		File dir = new File("data/images/"+s.SKIN_FOLDER+"/");
		
		if(dir.exists())
			for (File f : dir.listFiles())
				sprites.put(f.getName(), new Sprite(p, f.toString()));

	}
}
