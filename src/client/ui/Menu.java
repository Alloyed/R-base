package client.ui;

import client.*;
import client.sprites.*;

import controlP5.*;
import physics.Team;
import processing.core.*;

/* I've tried to move all those callback methods into here. 
 * It didn't work. 
 */
public class Menu extends UI {
	
	PImage logo;
	public UI lastMode;
	String team = "";
	int bg;
	
	/*Gooey methods*/
	public void connect() {
		r.net.connect(r.settings.IP, r.settings.PORT);
	}
	
	class listener implements ControlListener {
		Client r;
		public listener(Client r) {this.r = r;}
		@Override
		public void controlEvent(ControlEvent e) {
			if (e.isGroup()) {
				String s = r.servers[(int)e.getGroup().getValue()];
				Textfield t = (Textfield)r.gooey
						.getController("IP",r.settings);
				t.setValue(s);
			}
		}
	}
	
	public Controller btn(String s) {
		return r.gooey.addButton(s)
				.setColorBackground(r.skin.getColor(s))
				.setColorForeground(r.skin.getColor(s+"-over"))
				.setColorActive(r.skin.getColor(s+"-pressed"));
	}
	
	public Menu(Client r) {
		super(r);
		group = "menu";
		logo = (PImage) ((ImageSprite)r.skin.get("logo")).sprite;
		ControlP5 gooey = r.gooey;
		Settings settings = r.settings;
		
		ControlGroup m = gooey.addGroup(group, 0, 0);
		gooey.begin(m);
		
		btn("resume").setPosition(30, 30).setSize(100, 30);
		btn("quit").setPosition(30, 70).setSize(100, 30);		
		btn("connect").setPosition(670, 120).setSize(100, 30);		
		gooey.addButton("reset").setPosition(30, 110).setSize(100, 30);
		
		gooey.addListener(new listener(r));
		ListBox l = gooey.addListBox("", 670, 190, 100, 500);
		l.setHeight(400);
		l.setBarHeight(20);
		l.setItemHeight(30);
		l.moveTo(m);
		l.addItems(r.servers);
		
		gooey.end(m);
		gooey.addControllersFor("/settings", settings);
		gooey.moveTo(m, settings);
		
		//Ah well, we can't have everything we want.
		((Textfield)gooey.getController("/settings/IP"))
			.setValue(settings.IP);
		((Textfield)gooey.getController("/settings/PORT"))
			.setValue(settings.PORT);
		((Textfield)gooey.getController("/settings/USERNAME"))
			.setValue(settings.USERNAME);
		((Textfield)gooey.getController("/settings/WINDOW_WIDTH"))
			.setValue(settings.WINDOW_WIDTH);
		((Textfield)gooey.getController("/settings/WINDOW_HEIGHT"))
			.setValue(settings.WINDOW_HEIGHT);
		((Textfield)gooey.getController("/settings/SKIN_FOLDER"))
			.setValue(settings.SKIN_FOLDER);
		setTeam(Team.get(settings.team));
	}
	
	@Override
	public void draw() {
		p.background(bg);
		p.imageMode(PConstants.CENTER);
		p.image(logo, p.width / 2f, p.height / 2f);
	}

	@Override
	public void keyPressed() {
		if (p.key == PConstants.ESC)
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
		lastMode = r.currentMode;
		super.show();
		p.cursor();
	}
	
	public void setTeam(Team t) {
		team = Team.get(t);
		ControlP5 gooey = r.gooey;
		Skin skin = r.skin;
		bg = skin.getColor("bg"+team);
		gooey.setColorBackground(skin.getColor("menu" + team));
		gooey.setColorForeground(skin.getColor("menu" + team + "-over"));
		gooey.setColorActive(    skin.getColor("menu" + team + "-pressed"));
	}
	
}
