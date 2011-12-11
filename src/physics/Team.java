package physics;

public enum Team {
	ORANGE, NUETRAL, BLUE;
	public static String get(Team t) {
		switch (t) {
			case ORANGE: return "-orange";
			case BLUE: return "-blue";
			default: return "";
		}
	}
	
	public static Team get(int i) {
		switch (i) {
			case 1: return Team.BLUE;
			case -1:return Team.ORANGE;
			default:return Team.NUETRAL;
		}
	}
}
