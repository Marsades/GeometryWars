package units;

import main.BoxList;
import main.GeometryWars;
import processing.core.PVector;

public class Bullet extends Unit {

	float height = 2;
	public Bullet(GeometryWars par, BoxList boxlist, int initBox, int initSide, PVector pos, float dir) {
		super(par, boxlist, initBox, initSide, pos);
		this.dir = dir;
	}

	@Override
	public void drawModel() {
		PVector H = Z.copy().mult(height);
		moveForwards();
		parent.translate(H.x, H.y, H.z);
		parent.box(1);
	}

}
