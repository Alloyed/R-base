package physics;

import org.jbox2d.common.Vec2;

import physics.actors.Actor;

/*A bundle of all the data needed to create an actor*/
public class AddDef {
	public int id = -1; // <0 are invalid and are regened
	public String type; //Can't send Classes over the net dumbfuck!
	public Team team = Team.NUETRAL;
	public Vec2 size = new Vec2(0, 0);
	public Vec2 pos =  new Vec2(0, 0);
	public AddDef() {}
	public AddDef(Class<? extends Actor> type, 
			Vec2 size, 
			Vec2 pos) {
		this.type = type.getCanonicalName();
		this.size.set(size);
		this.pos.set(pos);
	}
	
}
