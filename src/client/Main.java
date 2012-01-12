package client;

/*
 * ROBOTGAME IS SERIOUS BUSINESS
 * 
 * SERIOUS
 * 
 * WITH COMMENTS
 *  
 */

//Util
import java.io.File;
import java.net.URL;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.util.FileSystemLocation;
import org.newdawn.slick.util.ResourceLoader;

import TWLSlick.TWLStateBasedGame;
import client.ui.Loading;
import client.ui.Loop;

/**
 * The main client class.
 * 
 * @author kyle
 *
 */
public class Main extends TWLStateBasedGame {
	Loop l;
	
	public Main(String name) {
		super(name);
	}
	
	@Override
	protected URL getThemeURL() {
			return ResourceLoader.getResource("gui/simple.xml");
	}
	
	
//	private Chat chat;
	
	/* Gooey methods. */
//	public void quit() { p.exit(); }
	
//	public void resume() { menu.lastMode.show(); }
	
	public void connect() {
//		menu.connect();

	}
	
//	public void exit() { dispose(); }
	
//	public void dispose() { settings.save(); }
	
//	public void inv() { ((Botmode)currentMode).inv(); }
	
//	public void health() { ((Botmode)currentMode).health(); }; 
	
	//End Gooey methods
	
	public void initPhysics() {
	//	stage = new Stage();
	//	((Map)stage.addActor(Map.class, -2, Team.NUETRAL, new Vec2(0,0), new Vec2(0,0))).startGame(123l);
	}
	
	//Initializes everything
//	public Client(PApplet p) {
//		this.p = p;
//		p.registerDraw(this);
//		p.registerDispose(this);
//		settings = new Settings(p);
//	}
	/*
	public void setup() {
		// Graphix stuf
		p.background(0);
		p.smooth();
		p.frameRate(60);
		cam = new Camera(p);
		skin = new Skin(this);
		
		if (menu == null) {
			initPhysics();
			
//			 Gooey Stuf
			gooey = new ControlP5(p);
			chat = new Chat(this, 5);
			Console.chat = new PrintStream(chat);
			menu = new Menu(this);
			botmode = new Botmode(this);
			ghostmode = new Ghostmode(this);
			godmode = new Godmode(this);
			for (ControllerInterface c : gooey.getControllerList()) {
				if (c instanceof Textfield)
					((Textfield) c).setAutoClear(false);
			}
			try {
				for (int i = 0; i < ControllIO.getInstance(p).getNumberOfDevices(); ++i) {
					ControllDevice d = ControllIO.getInstance(p).getDevice(i);
					if (!(d.getName().equals("Mouse") || d.getName().equals("Keyboard"))) {
						joypad = d;
						buttons = new boolean[d.getNumberOfButtons()];
						buttonMap = new int[d.getNumberOfButtons()];
						buttonMap[1] = settings.USE;
						break;
					}
				}
			} catch (Exception e) {} //No joypad
			net = new Network(this);
		}
		
		oldtime = System.nanoTime();
		skin.start();
		ghostmode.start(new Vec2(1,1));
		currentMode = ghostmode;
		menu.setTeam(settings.team == 0 ? Team.ORANGE : Team.BLUE);
		menu.show();
	}
	*/

	//Is looped over to draw things
	long time, oldtime;
	float physAccum = 0, netAccum = 0;
/*
	public void draw() {
		if (font != null) {
			p.textFont(font);
			font = null;
		}
		
		if (joypad != null) {
			for (int i = 0; i < joypad.getNumberOfButtons(); ++i) {
				boolean btn = joypad.getButton(i).pressed();
				if (btn != buttons[i]) {
					buttons[i] = btn;
					Console.out.println(i);
					if (btn) {
						keyPressed(buttonMap[i]);
					} else {
						keyReleased(buttonMap[i]);
					}
				}
			}
		}
		
		//Timing
		time = System.nanoTime();
		float frameTime =  time - oldtime;
		frameTime /= 1000000f;
		//if (frameTime > 2500)
		//	frameTime = 2500;
		physAccum += frameTime;
		netAccum  += frameTime;
		oldtime = time;
		
		//Networking
		if (net != null) {
			net.poll();
			while (netAccum >= Connection.frame*1000) {
				net.send();
				netAccum -= Connection.frame*1000;
			}
		}
		
		//Physix
		int i = 0;
		if (settings.FIXED_TIMESTEP) {
			while (physAccum > Stage.frame*1000) {
				for (Actor a: stage.activeActors) {
					a.oldPos = new Vec2(a.b.getWorldCenter());
					a.oldAng = a.b.getAngle();
				}
				stage.step(Stage.frame);
				physAccum -= Stage.frame*1000f;
				i++;
			}
			Actor.alpha = (physAccum / (Stage.frame*1000.0f));
			Actor.alpha = 1;
		} else {
			i = 1;
			Console.out.println(frameTime/1000f + " " + Stage.frame);
			stage.step(frameTime / 1000f);
			Actor.alpha = 1;
		}
		
		//Drawing
		currentMode.draw();
		gooey.draw();
		fps(i);
	}
	
	public void draw(Actor a) {
		skin.get(a).draw(a);
	}
	
	public void fps(int i) {
		p.fill(255);
		p.text("Frames: " + i  +
				", FPS: " + p.frameRate + 
				", Actors: " + stage.activeActors.size(),
				0, 10);
	}
	
	/*
	 * Key pressed/released from processing's side.
	 * All game keys that can also be used by controllers 
	 *  are handled by keyPressed(int keycode) 
	 *  so that I can arbitrarily call it for the joypads.
	 * Chat is left here, 'cause it needs straight keys, 
	 *  and shouldn't respond to a joypad.
	 
	public void keyPressed() {
		if (currentMode != menu || chat.isChatting) {
			chat.keyPressed();
		}
		keyPressed(p.keyCode);	
		if (p.key == ESC) { //Keeps game from stopping at ESC
			p.key = 0;
		}
	}

	public void keyReleased() {
		keyReleased(p.keyCode);
	}
	
	public void keyPressed(int keycode) {
		if (keycode == settings.SCREENSHOT) {
			p.saveFrame("screen-###.png");
			Console.chat.println("\\Screenshot saved.");
		} else if (!chat.isChatting) {
			currentMode.keyPressed(keycode);
		}
	}
	
	public void keyReleased(int keycode) {
		if (!chat.isChatting) {
			currentMode.keyReleased(keycode);
		}
	}
	
	public void mousePressed() {
		currentMode.mousePressed();
	}
	
	public void mouseReleased() {
//		if (!gooey.getGroup(currentMode.group).isMouseOver()) {
//			;
//		}
	}
*/
	@Override
	public boolean closeRequested() {
		l.settings.save();
		return true;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "R-base";
	}
	
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
			ResourceLoader.addResourceLocation(new FileSystemLocation(new File("data")));
			l = new Loop(gc);
			SoundStore.get().setMaxSources(32);
			//WHY.jpg
			//gc.setMouseCursor(new Image("blank.png"), 0, 0);
			addState(new Loading(l));
			enterState(Loading.id);
	}
	
	/**
	 * Entry point to our simple test
	 * 
	 * @param argv The arguments passed in
	 */
	public static void main(String argv[]) {
		try {
			Renderer.setRenderer(Renderer.VERTEX_ARRAY_RENDERER);
			Renderer.setLineStripRenderer(Renderer.QUAD_BASED_LINE_STRIP_RENDERER);
			
			AppGameContainer container = new AppGameContainer(new Main("R-base"));
			container.setDisplayMode(800,600,false);
			
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}