package client.ui;

import client.Main;

public abstract class UI {
	public Main r;
	public String group;
	public UI(Main r){
		this.r = r;
	}
	public abstract void draw();
	public abstract void keyPressed();
	public abstract void keyReleased();
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
