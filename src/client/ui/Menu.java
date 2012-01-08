package client.ui;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import client.sprites.*;
import physics.Console;
import physics.Team;
import TWLSlick.BasicTWLGameState;
import de.matthiasmann.twl.Button;

/**
 * The main menu. 
 * 
 * @author kyle
 *
 */
public class Menu extends BasicTWLGameState {
	Image logo;
	String team = "";
	Color bg;
	Loop l;
	public final static int id = 0;
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}
	
	/*Gooey methods*/
	public void connect() {
//		r.net.connect(r.settings.IP, r.settings.PORT);
	}
	/*
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
	*/
	public Menu(Loop l) {
		this.l = l;
		bg = Color.black;
		logo = ((ImageSprite)l.skin.get("logo")).sprite;
//		group = "menu";
		//logo = (PImage) ((ImageSprite)r.skin.get("logo")).sprite;
		//ControlP5 gooey = r.gooey;
		//Settings settings = r.settings;
		
		//ControlGroup m = gooey.addGroup(group, 0, 0);
		//gooey.begin(m);
		
		//btn("resume").setPosition(30, 30).setSize(100, 30);
		//btn("quit").setPosition(30, 70).setSize(100, 30);		
		//btn("connect").setPosition(670, 120).setSize(100, 30);		
		//gooey.addButton("reset").setPosition(30, 110).setSize(100, 30);
		
		//gooey.addListener(new listener(r));
		//ListBox l = gooey.addListBox("", 670, 190, 100, 500);
		//l.setHeight(400);
		//l.setBarHeight(20);
		//l.setItemHeight(30);
		//l.moveTo(m);
		//l.addItems(r.servers);
		
		//gooey.end(m);
		//gooey.addControllersFor("/settings", settings);
		//gooey.moveTo(m, settings);
		
		/*Ah well, we can't have everything we want.
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
	*/
	}
	
//	@Override
//	public void draw() {

//	}

//	@Override
//	public void keyPressed() {
//		if (p.key == PConstants.ESC)
//			lastMode.show();
//	}

	
	public void setTeam(Team t) {
//		team = Team.get(t);
//		ControlP5 gooey = r.gooey;
//		Skin skin = r.skin;
		//bg = skin.getColor("bg"+team);
		//gooey.setColorBackground(skin.getColor("menu" + team));
		//gooey.setColorForeground(skin.getColor("menu" + team + "-over"));
		//gooey.setColorActive(    skin.getColor("menu" + team + "-pressed"));
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sg)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sg, Graphics g)
			throws SlickException {
		g.setBackground(bg);
		g.drawImage(logo, (gc.getWidth() / 2f) - (logo.getWidth()/2), 
				(gc.getHeight() / 2f) - (logo.getHeight() / 2));		
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sg, int dt)
			throws SlickException {
		if (toResume)
			sg.enterState(1);
		if (toQuit)
			gc.exit();
		l.update(dt);
	}
	
	public boolean toResume = false, toQuit = false;
	private Button resume, quit, connect;

    @Override
    protected void createRootPane() {
        super.createRootPane();

        resume = new Button("Resume");
        resume.addCallback(new Runnable() {
            public void run() {
                toResume = true;
            }
        });
        rootPane.add(resume);
        
        quit = new Button("Quit");
        quit.addCallback(new Runnable() {
            public void run() {
                toQuit = true;
            }
        });
        rootPane.add(quit);
        
        connect = new Button("Connect");
        connect.addCallback(new Runnable() {
            public void run() {
                l.net.connect("localhost", l.net.TCP, l.net.UDP);
            }
        });
        rootPane.add(connect);
    }
    
    //btn("resume").setPosition(30, 30).setSize(100, 30);
  	//btn("quit").setPosition(30, 70).setSize(100, 30);		
  	//btn("connect").setPosition(670, 120).setSize(100, 30);
    @Override
    protected void layoutRootPane() {
        resume.adjustSize();
        resume.setPosition(30, 30);
        
        quit.adjustSize();
        quit.setPosition(30, 70);
        
        connect.adjustSize();
        connect.setPosition(670, 120);
    }
    
    @Override
	public void keyPressed(int key, char c) {
		Console.dbg.println(key);
	}
}
