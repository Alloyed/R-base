package client.sprites;

import client.Client;

import physics.actors.Actor;
import physics.actors.Map;
import physics.actors.Prop;
import processing.core.PApplet;
import processing.core.PGraphics;

/*the map*/
public class MapSprite implements Sprite {
	PApplet p;
	Client c;
	public MapSprite(Client c) {
		this.c = c;
		this.p = c.p;
	}
	
	@Override
	public void draw(Actor a) {
		for (Prop pr : ((Map)a).props) {
			if (pr != null) {
				Sprite s = c.skin.get(pr.getImage());
				s.draw(pr);
			}
		}
	}

	@Override
	public void draw(PGraphics pg, float x, float y, float max) {
		;
	}

	@Override
	public void draw(Prop pr) {
		// TODO Auto-generated method stub
		
	}
}
