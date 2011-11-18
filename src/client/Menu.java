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
	public Menu(Runner r) {
		super(r);
		logo = r.loadImage("logo2.png");
		r.imageMode(PConstants.CENTER);
		r.gooey.controller("resume").setLabel("start");
	}
	
	@Override
	public void draw() {
		r.background(r.color(25, 50, 50));
		r.image(logo, r.width / 2f, r.height / 2f);
	}

	@Override
	public void keyPressed() {
		// TODO Auto-generated method stub
		
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
		r.gooey.show();
		r.cursor();
		r.currentMode = this;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		r.gooey.hide();
		r.noCursor();
	}

}
