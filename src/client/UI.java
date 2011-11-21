package client;

abstract class UI {
	public Runner r;
	public UI(Runner r){
		this.r = r;
	}
	public abstract void draw();
	public abstract void keyPressed();
	public abstract void keyReleased();
	public abstract void mousePressed();
	public abstract void mouseReleased();
	public abstract void show();
	public abstract void hide();
}
