package main;
import processing.core.*;

public class Marker {
	PApplet parent;
	PVector Pos;
	int col;

	Marker(PApplet par, PVector initPos, int c) {
		parent = par;
		Pos = initPos;
		col = c;
	}

	public void draw() {
		parent.pushMatrix();
		parent.translate(Pos.x, Pos.y, Pos.z);
		parent.fill(col);
		parent.stroke(col);
		parent.box(1);
		parent.popMatrix();
	}
}