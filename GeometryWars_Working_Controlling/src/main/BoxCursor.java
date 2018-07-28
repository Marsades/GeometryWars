package main;

import processing.core.*;

class BoxCursor {
	PApplet parent;
	BoxList boxList;
	int x;
	int y;
	int z;
	float L;
	boolean selected;

	BoxCursor(PApplet par, BoxList cur, float sideLength) {
		parent = par;
		this.boxList = cur;
		x = 0;
		y = 0;
		z = 0;
		L = sideLength;
		selected = true;
	}

	void move(int xAdd, int yAdd, int zAdd) {
		x += xAdd;
		y += yAdd;
		z += zAdd;
	}

	public void draw() {
		parent.pushMatrix();
		PVector Pos = boxList.getOrigin();
		parent.translate(Pos.x, Pos.y, Pos.z);
		parent.applyMatrix(boxList.getOrientation().toRotationMatrix());
		parent.translate(x * L, y * L, z * L);
		parent.noFill();
		if (selected) {
			parent.stroke(parent.color(0, 255, 0));
		} else {
			parent.stroke(parent.color(255, 0, 0));
		}
		parent.box(L * 1.1f);
		parent.popMatrix();
		// translate(-x*L,-y*L,-z*L);
	}

	void turnSelected() {
		selected = !selected;
	}
}
