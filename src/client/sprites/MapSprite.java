package client.sprites;

import org.newdawn.slick.Graphics;

import physics.Props.Prop;
import physics.actors.Actor;
import physics.map.Map;
import client.ui.Loop;

/*the map*/
public class MapSprite implements Sprite {
	Loop l;
	public MapSprite(Loop l) {
		this.l = l;
	}
	
	@Override
	public void draw(Graphics g, Actor a) {
		for (Prop pr : ((Map)a).props) {
			if (pr != null) {
				Sprite s = l.skin.get(pr.getImage());
				s.draw(g, pr);
			}
		}
	}

	@Override
	public void draw(Graphics pg, float x, float y, float max) {}

	@Override
	public void draw(Graphics g, Prop pr) {}
}
