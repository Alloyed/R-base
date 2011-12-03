package client.ui;

import client.Colors;
import client.Client;
import client.Settings;

import controlP5.ControlEvent;
import controlP5.ControlGroup;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.ListBox;
import controlP5.Textfield;
import processing.core.PConstants;
import processing.core.PImage;

/* I've tried to move all those callback methods into here. 
 * It didn't work. 
 */
public class Menu extends UI {
	
	PImage logo;
	public UI lastMode;
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
	
	public Menu(Client r) {
		super(r);
		group = "menu";
		logo = r.loadImage("logo2.png");
		r.imageMode(PConstants.CENTER);
		ControlP5 gooey = r.gooey;
		Settings settings = r.settings;
		
		ControlGroup m = gooey.addGroup(group, 0, 0);
		gooey.begin(m);
		gooey.addButton("resume").setPosition(30, 30)
				.setSize(100, 30).setColor(Colors.goGreen);
		gooey.addButton("quit").setPosition(30, 70)
				.setSize(100, 30).setColor(Colors.quitRed);
		gooey.addButton("connect").setPosition(670, 120)
				.setSize(100, 30).setColor(Colors.connectOrange);
		gooey.addButton("reset").setPosition(30, 110)
				.setSize(100, 30);
		
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
	}
	
	@Override
	public void draw() {
		r.background(r.color(25, 50, 50));
		r.image(logo, r.width / 2f, r.height / 2f);
	}

	@Override
	public void keyPressed() {
		if (r.key == PConstants.ESC)
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
		r.cursor();
	}
}
