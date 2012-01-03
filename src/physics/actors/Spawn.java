package physics.actors;

import physics.Stage;

/**
 * TODO: All of this.
 * @author kyle
 *
 */
public class Spawn extends Prop {
	float time = 0;
	public Class<? extends Actor> type;
	public void makeBody() {
		fd.isSensor = true;
	}
	
	public void spawn() {
		
	}
	
	public void force() {
		time += Stage.frame;
	}
}
