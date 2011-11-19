package client;

import java.util.ArrayList;

import controlP5.ControllerInterface;
import processing.core.PConstants;
import processing.core.PImage;

/* I've tried to move all those callback methods into here. 
 * It didn't work.
 */
public class Menu extends UI {
	PImage logo;
	ArrayList<ControllerInterface> mainMenu;
	UI lastMode;
	public Menu(Runner r) {
		super(r);
		logo = r.loadImage("logo2.png");
		r.imageMode(PConstants.CENTER);
	}
	
	@Override
	public void draw() {
		r.background(r.color(25, 50, 50));
		r.image(logo, r.width / 2f, r.height / 2f);
	}

	@Override
	public void keyPressed() {
		if (r.key == r.ESC)
			lastMode.show();
	}

	@Override
	public void keyReleased() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		r.currentMode.hide();
		r.gooey.getGroup("menu").show();
		r.cursor();
		lastMode = r.currentMode;
		r.currentMode = this;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		r.gooey.getGroup("menu").hide();
	}

}
