package units;

import main.BoxList;
import main.GeometryWars;

public class Soldier extends Unit {

	public Soldier(GeometryWars par, float sideLength, BoxList boxlist, int initBox) {
		super(par, boxlist, initBox);
		// TODO Auto-generated constructor stub
		this.speed = 2f;
	}

	@Override
	public void drawModel() {
		parent.fill(parent.color(0, 0, 0));
		parent.stroke(parent.color(255, 255, 255));
		parent.box(R);
	}

	public void shoot() {
		Bullet bul = new Bullet(this.parent, this.getB(), this.curBox, this.getSide(), this.getLocalPos(), this.dir);
		this.getB().addUnit(bul);
	}
}
