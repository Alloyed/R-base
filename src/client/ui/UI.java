package client.ui;

import processing.core.PApplet;
import client.Client;

public abstract class UI {
	public Client r;
	public PApplet p;
	public String group;
	public UI(Client r){
		this.r = r;
		p = r.p;
	}
	public abstract void draw();
	public abstract void keyPressed(int keycode);
	public abstract void keyReleased(int keycode);
	public abstract void mousePressed();
	public abstract void mouseReleased();
	public void show() {
		r.currentMode.hide();
		r.gooey.getGroup(group).show();
		r.currentMode = this;
	}
	public void hide() {
		r.gooey.getGroup(group).hide();
	}
}
