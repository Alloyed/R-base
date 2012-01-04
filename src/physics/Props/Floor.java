package physics.Props;


public class Floor extends Prop {
	
	@Override
	public void makeFixture() {
		super.makeFixture();
		baseImage = "floor";
		fd.isSensor = true;
	}
}
