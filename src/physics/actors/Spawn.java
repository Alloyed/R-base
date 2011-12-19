package physics.actors;

public class Spawn extends Prop {
	public Class<? extends Actor> type;
	public void makeBody() {
		fd.isSensor = true;
	}
	
	public void spawn() {
		
	}

}
