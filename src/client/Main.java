package client;

/**
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
			ResourceLoader.addResourceLocation(
					new FileSystemLocation(new File("data")));
			l = new Loop(gc);
			gc.setFullscreen(l.settings.FULLSCREEN);
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
