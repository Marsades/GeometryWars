package main;
import processing.core.*;

public class PosCursor {
	PApplet parent;
	BoxList B;
	boolean selected;
	float faceX;
	float faceY;
	float L;
	float speed;
	float dir;
	int side;
	int curBox;
	int R;
	float e;
	private final PVector[] Vects = { new PVector(1, 0, 0), new PVector(0, 1, 0), new PVector(0, 0, 1),
			new PVector(-1, 0, 0), new PVector(0, -1, 0), new PVector(0, 0, -1) };
	PVector Pos;
	PVector P;
	PVector X;
	PVector Y;

	PosCursor(PApplet par, float sideLength, BoxList boxlist, int initBox) {
		parent = par;
		R = 10;
		L = sideLength;
		faceX = L / 2;
		faceY = L / 2;
		side = 0;
		speed = 1.1f;
		e = 1f;
		B = boxlist;
		curBox = initBox;
		selected = false;
		P = new PVector(0, 0, 0);
		X = new PVector(0, 0, 0);
		Y = new PVector(0, 0, 0);
	}

	void moveX(float xAdd) {
		faceX += xAdd * PApplet.cos(dir);
		faceY += xAdd * PApplet.sin(dir);
		side = edgeCheck(side);
	}

	void moveY(float yAdd) {
		faceX += yAdd * PApplet.cos(dir + PApplet.PI / 2);
		faceY += yAdd * PApplet.sin(dir + PApplet.PI / 2);
		side = edgeCheck(side);
	}

	private PVector getPos() {
		return Pos;
	}

	public void draw() {
		Pos = cubeTrans(curBox, side, faceX / L, faceY / L);
		parent.pushMatrix();
		// translate(B.get(curBox).x*L, B.get(curBox).y*L, B.get(curBox).z*L);
		parent.applyMatrix(B.getOrientation().toRotationMatrix());
		parent.translate(Pos.x, Pos.y, Pos.z);
		parent.noFill();
		if (selected) {
			parent.stroke(parent.color(0, 255, 0));
		} else {
			parent.stroke(parent.color(255, 0, 0));
		}
		parent.box(R * 1.1f);
		parent.popMatrix();
		// translate(-x*L,-y*L,-z*L);
	}

	void turnSelected() {
		selected = !selected;
	}

	/*
	 * PVector cubeTrans(int Box, int side, float x, float y) { P =
	 * Vects[side].copy(); X = Vects[(side+1) % 6].copy(); PVector.cross(P, X, Y); Y
	 * = PVector.mult(Y, (y-0.5)*L); P = PVector.mult(P, L/2); X = PVector.mult(X,
	 * (x-0.5)*L); P.add(X); P.add(Y); return P; }
	 */
	PVector cubeTrans(int box, int side, float x, float y) {
		PVector P = PVector.mult(B.get(box), L);
		P = PVector.add(P, Vects[side].copy().mult(L / 2));
		X = Vects[(side + 1) % 6].copy();
		Y = Vects[side].cross(X);
		P.add(PVector.mult(X, (x - 0.5f) * L));
		P.add(PVector.mult(Y, (y - 0.5f) * L));
		return P;
	}

	int edgeCheck(int side) {
		int newside = side;
		int i;
		boolean xOverL = faceX > L;
		boolean xUnderZero = faceX < 0;
		boolean yOverL = faceY > L;
		boolean yUnderZero = faceY < 0;
		boolean isEven = side % 2 == 0;
		switch (side % 2) {
		case 0:
			if (faceX > L) {
				newside = (side + 1) % 6;
				// To check whether a box "above" the side we want to move to is present:
				i = B.indexOf(PVector.add(B.get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 4) % 6;
					// From odd when x too high
					oddXTooHigh();
					break;
				}
				// To check whether a box besides the current box is present:
				i = B.indexOf(PVector.add(B.get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					faceX = e;
					newside = side;
				} else {
					// If no boxes are present
					evenXTooHigh();
				}
				break;
			} else if (faceX < 0) {
				newside = (side + 4) % 6; // side+4 = side-2 (mod 6)
				// To check whether a box "above" the box we want to move to is present:
				i = B.indexOf(PVector.add(B.get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 1) % 6;
					// From odd when x too low
					oddXTooLow(); // Works for 0 -> 1
					break;
				}
				// To check whether a box besides the current box is present:
				i = B.indexOf(PVector.add(B.get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					faceX = L - e;
					newside = side;
				} else {
					evenXTooLow();
				}
				break;
			} else if (faceY > L) {
				newside = (side + 2) % 6;
				// To check whether a box "above" the box we want to move to is present:
				i = B.indexOf(PVector.add(B.get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 5) % 6;
					// From odd when y too low
					evenYTooHigh();
					break;
				}
				// To check whether a box besides the current box is present:
				i = B.indexOf(PVector.add(B.get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					faceY = e;
					newside = side;
				} else {
					evenYTooHigh();
				}
				break;
			} else if (faceY < 0) {
				newside = (side + 5) % 6; // side+5 = side-1 (mode 6)
				// To check whether a box "above" the box we want to move to is present:
				i = B.indexOf(PVector.add(B.get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 2) % 6;
					// From odd when y too high
					evenYTooLow();
					/*
					 * faceY = L-faceX; faceX = L-e; dir += -PI/2;
					 */
					break;
				}
				// To check whether a box besides the current box is present:
				i = B.indexOf(PVector.add(B.get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					faceY = L - e;
					newside = side;
				} else {
					evenYTooLow();
				}
				break;
			}
			newside = side;
		case 1:
			if (faceX > L) {
				newside = (side + 1) % 6;
				// To check whether a box "above" the box we want to move to is present:
				i = B.indexOf(PVector.add(B.get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 4) % 6;
					// From odd when y too high
					evenXTooHigh(); // Works for 5 -> 3
					break;
				}
				// To check whether a box besides the current box is present:
				i = B.indexOf(PVector.add(B.get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					faceX = e;
					newside = side;
				} else {
					oddXTooHigh();
				}
				break;
			} else if (faceX < 0) {
				newside = (side + 4) % 6; // side+4 = side-2 (mode 6)
				// To check whether a box "above" the box we want to move to is present:
				i = B.indexOf(PVector.add(B.get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 1) % 6;
					// From odd when y too high
					evenXTooLow();
					break;
				}
				// To check whether a box besides the current box is present:
				i = B.indexOf(PVector.add(B.get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					faceX = L - e;
					newside = side;
				} else {
					oddXTooLow();
				}
				break;
			} else if (faceY > L) {
				newside = (side + 5) % 6; // side+5 = side-1 (mode 6)
				// To check whether a box "above" the box we want to move to is present:
				i = B.indexOf(PVector.add(B.get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 2) % 6;
					// From odd when y too high
					// evenYTooHigh();
					faceY = faceX;
					faceX = L - e;
					dir += +PApplet.PI / 2;
					break;
				}
				// To check whether a box besides the current box is present:
				i = B.indexOf(PVector.add(B.get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					faceY = e;
					newside = side;
				} else {
					oddYTooHigh();
				}
				break;
			} else if (faceY < 0) {
				newside = (side + 2) % 6;
				// To check whether a box "above" the box we want to move to is present:
				i = B.indexOf(PVector.add(B.get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 5) % 6;
					// evenYTooLow();
					faceY = faceX;
					faceX = e;
					dir += PApplet.PI / 2;
					break;
				}
				// To check whether a box besides the current box is present:
				i = B.indexOf(PVector.add(B.get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					faceY = L - e;
					newside = side;
				} else {
					oddYTooLow();
				}
				break;
			}
			break;
		default:
			newside = side;
			break;
		}
		return newside;
	}

	void oddXTooHigh() {
		faceX = L - faceY;
		faceY = e;
		dir += PApplet.PI / 2;
	}

	void oddXTooLow() {
		faceX = faceY;
		faceY = e;
		dir += -PApplet.PI / 2;
	}

	void oddYTooHigh() {
		faceY = faceX;
		faceX = L - e;
		dir += PApplet.PI / 2;
	}

	void oddYTooLow() {
		faceY = faceX;
		faceX = e;
		dir += PApplet.PI / 2;
	}

	void evenXTooHigh() {
		faceX = faceY; // Correct
		faceY = L - e; // Correct
		dir += -PApplet.PI / 2;
	}

	void evenXTooLow() {
		faceX = L - faceY; // Correct
		faceY = L - e; // Correct
		dir += PApplet.PI / 2;
	}

	void evenYTooHigh() {
		faceY = L - faceX;
		faceX = e;
		dir += -PApplet.PI / 2;
	}

	void evenYTooLow() {
		faceY = L - faceX;
		faceX = L - e;
		dir += -PApplet.PI / 2;
	}
}