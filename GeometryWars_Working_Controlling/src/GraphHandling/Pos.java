package GraphHandling;
import processing.core.PVector;

public class Pos {

	public int box;
	public int side;
	public PVector localCoord;

	public Pos(int initBox, int initSide, PVector initCoord) {
		box = initBox;
		side = initSide;
		localCoord = initCoord;
	}
	public PVector getPosition(){
		return localCoord;
	}
}
