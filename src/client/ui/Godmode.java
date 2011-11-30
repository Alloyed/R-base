package client.ui;

import client.Main;

/*One Giant TODO*/
public class Godmode extends UI {
	
	public Godmode(Main r) {
		super(r);
		group = "godmode";
		r.gooey.addGroup(group, 0, 0);
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		super.show();
		r.cursor();
	}
}
