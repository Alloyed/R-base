package client;

/*One Giant TODO*/
public class Godmode extends UI {

	public Godmode(Runner r) {
		super(r);
		// TODO Auto-generated constructor stub
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
		r.currentMode.hide();
		r.gooey.getGroup("godhud").show();
		r.cursor();
		r.currentMode = this;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		r.gooey.getGroup("godhud").hide();
	}
	
}
