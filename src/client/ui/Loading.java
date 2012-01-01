package client.ui;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import TWLSlick.BasicTWLGameState;


public class Loading extends BasicTWLGameState {
	Image logo;
	Color bg;
	Loop l;
	public final static int id = 4;
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}
	
	public Loading(Loop l) {
		this.l = l;
		try {
			logo = new Image("images/loading.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		bg = Color.black;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sg)
			throws SlickException {
		
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sg, Graphics g)
			throws SlickException {
		g.setBackground(bg);
		g.drawImage(logo, (gc.getWidth() / 2f) - (logo.getWidth()/2), 
				(gc.getHeight() / 2f) - (logo.getHeight() / 2));		
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sg, int dt)
			throws SlickException {
		if (l.skin.next()) {
			sg.addState(new Menu(l));
			sg.addState(new Botmode(l));
			sg.addState(new Ghostmode(l));
			sg.addState(new Godmode(l));
			sg.enterState(Menu.id);
		}
	}
}