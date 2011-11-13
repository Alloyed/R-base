package server;

/*Client-only settings*/
public class Settings extends common.Settings {

	public Integer WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
	public Boolean ROTATE_FORCE = false;

	public Settings(String s) {
		super(s);
	}
}
