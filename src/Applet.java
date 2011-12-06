
import client.Client;
import processing.core.PApplet;
/*Runs tha client in a way to make it easy to write a processing sketch*/
public class Applet extends PApplet {
	private static final long serialVersionUID = 1L;
	Client c;
	/*Gooey methods*/
	public void quit() { c.quit(); }
	public void resume() { c.resume(); }
	public void connect() { c.connect(); }
	public void exit() { super.exit();}
	public void inv() { c.inv(); }
	public void reset() { setup(); }
	
	public void setup() {
		size(800, 600, JAVA2D);
		c = new Client(this);
	}
	public void draw() {
		
	}
	public void keyPressed() {
		c.keyPressed();
	}
	public void keyReleased() {
		c.keyReleased();
	}
	public void mousePressed() {
		c.mousePressed();
	}
	public void mouseReleased() {
		c.mouseReleased();
	}
	public static void main(String[] args) {
		PApplet.main(new String[] {"--present", "Applet"});
	}
}
