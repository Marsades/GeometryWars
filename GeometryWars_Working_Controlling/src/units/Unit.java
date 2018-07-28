package units;
import main.*;
import GraphHandling.Pos;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public abstract class Unit {
	GeometryWars parent;
	private BoxList B;
	
	// Position 
	int curBox;
	private int side; // between 0-5
	PVector local2DCoord = new PVector(); // It actually works with a PVector for the local coordinates!!
	Pos position;
	
	// Movement
	float speed; //Movement speed
	float rotSpeed;
	float dir;
	
	//Rotation and Position handling:
//	PVector satellitePosition;
//	Quaternion satelliteOrientation;
	
	int newside;
	float R;
	float e;
	float vZ;
	private static final float g = 0.5f;
	private final PVector[] Vects = { new PVector(1, 0, 0), new PVector(0, 1, 0), new PVector(0, 0, 1),
			new PVector(-1, 0, 0), new PVector(0, -1, 0), new PVector(0, 0, -1) };
	PVector globalCoord= new PVector();
	PVector P = new PVector();
	PVector X = new PVector();
	PVector Y = new PVector();
	PVector Z = new PVector();
	PVector forward = X.copy();
	
	public Unit(GeometryWars par, BoxList boxlist, int initBox, int initSide, PVector localPos) {
		parent = par;
		R = 10;
		rotSpeed = 0.05f;
		speed = 5;
		dir = 0;
//		e = 0;
		this.B = boxlist;
		//Local coordinates
		curBox = initBox;
		side = initSide;
		local2DCoord.x = localPos.x;
		local2DCoord.y = localPos.y;
		local2DCoord.z = 0;
		position  = new Pos(curBox, side, local2DCoord);
		vZ = 0;
		
		//Global coordinates
		X = new PVector(0, 0, 0);
		Y = new PVector(0, 0, 0);
		Z = new PVector(0, 0, 0);
		globalCoord = cubeTrans(curBox, getSide(), local2DCoord.x / parent.sideLength, local2DCoord.y / parent.sideLength, local2DCoord.z);
	}
	
	public Unit(GeometryWars par, BoxList boxlist, int initBox) {
		this(par, boxlist, initBox,	0, new PVector(par.sideLength / 2, par.sideLength / 2));
	}
	public Unit(GeometryWars par, BoxList boxlist) {
		this(par, boxlist, 0, 0, new PVector(par.sideLength / 2, par.sideLength / 2));
	}

	public void draw() {
//		System.out.print(local2DCoord.toString());
//		System.out.println(vZ);		
		if( local2DCoord.z > 0 || vZ > 0) {
			local2DCoord.z += vZ;
			vZ -= g;
		} else {
			local2DCoord.z = 0;
			vZ = 0;
		}
		globalCoord = cubeTrans(curBox, getSide(), local2DCoord.x / parent.sideLength, local2DCoord.y / parent.sideLength,local2DCoord.z );
		parent.fill(parent.color(0, 0, 0));
		parent.pushMatrix();
		PVector boxOrigin = B.getOrigin(); 
		parent.translate(boxOrigin.x, boxOrigin.y, boxOrigin.z);
		parent.applyMatrix(getB().getOrientation().toRotationMatrix());
		drawSide();
		parent.popMatrix();
	}

	private void drawSide() {
		parent.pushMatrix();
		parent.translate(globalCoord.x, globalCoord.y, globalCoord.z);
		int s = getSide() % 2 == 0 ? 1 : -1;
		switch (getSide() % 3) {
		case 0:
			parent.rotateX(s * dir);
			break;
		case 1:
			parent.rotateY(-s * dir);
			break;
		case 2:
			parent.rotateZ(s * dir);
			break;
		}
		this.drawModel();
		parent.popMatrix();
	}

	public abstract void drawModel();
	
	public void moveForwards() {
		PVector p = getLocalDirection().mult(speed);
//		p.div(abs(xMove)+abs(yMove)).mult(speed); //Using 1-norm
//		p.div(PApplet.max(PApplet.abs(xMove), PApplet.abs(yMove))).mult(speed); // Using infinity/supremum-norm.
		local2DCoord.x += p.x;
		local2DCoord.y += p.y; 
		newside = edgeCheck(getSide(), local2DCoord.copy() );
		if (newside < 0) {
			System.out.println("This should never be reached");
			newside = newside + 6;
		}
		if (newside != getSide()) {
//			PApplet.println("Side shift from: " ,  side, newside);
//			printPos();
			side = newside;
		}
		globalCoord = cubeTrans(curBox, getSide(), local2DCoord.x / parent.sideLength, local2DCoord.y / parent.sideLength,local2DCoord.z );
	}
	public void transport(BoxList newB) {
		this.B = newB;
	}

	public void teleport(int i, int j, float x, float y) {
		curBox = i;
		side = j;
		local2DCoord.x = x;
		local2DCoord.y = y;
	}

	boolean isSideOccupied(int side) {
		int i = getB().indexOf(PVector.add(getB().get(curBox), Vects[side]));
		if (i != -1) {
			return true;
		}
		return false;
	}

	/*
	 * switch(side){ case 0: int i = B.indexOf(new PVector(curBox.x, curBox.y+1,
	 * curBox.z)); if (i != -1) { //There is a box in moving direction faceX = e;
	 * curBox = i; return side; } case 2: int i = B.indexOf(new PVector(curBox.x,
	 * curBox.y+1, curBox.z)); if (i != -1) { //There is a box in moving direction
	 * faceX = e; curBox = i; return side; } case 4: int i = B.indexOf(new
	 * PVector(curBox.x, curBox.y+1, curBox.z)); if (i != -1) { //There is a box in
	 * moving direction faceX = e; curBox = i; return side; } }
	 */
	PVector cubeTrans(int box, int side, float x, float y, float z) {
		PVector P = PVector.mult(getB().get(box), parent.sideLength);
		P = PVector.add(P, Vects[side].copy().mult(parent.sideLength / 2));
		X = Vects[(side + 1) % 6].copy();
		Y = Vects[side].cross(X);
		Z = Vects[side];
		P.add(PVector.mult(X, (x - 0.5f) * parent.sideLength));
		P.add(PVector.mult(Y, (y - 0.5f) * parent.sideLength));
		P.add(PVector.mult(Z, z));
		// P.add(PVector.mult(Z, parent.boxList.heightScale * parent.boxList.Terrain.eval(new float[] {x,y}))); // Moving along Z-height terrain
		return P;
	}

	public PVector getGlobalPos() {
		return globalCoord.copy();
	}
	public PVector getLocalPos() {
		return local2DCoord.copy();
	}

	public PVector getNormal() {
		return Vects[getSide()].copy();
	}

	public PVector getGlobalDirection() {
		return PVector.add(X.copy().mult(PApplet.cos(dir)), Y.copy().mult(PApplet.sin(dir)));
	}
	public PVector getLocalDirection() {
		return new PVector(PApplet.cos(dir), PApplet.sin(dir));
	}

	public void setDir(float f) {
		this.dir = f;
	}

	public int getSide() {
		return side;
	}

	public void jump() {
		vZ += 10f;
	}
	
	public void turn(boolean left) {
		if (left) {
			dir += rotSpeed;
			if (dir > PConstants.PI)
				dir += -PConstants.TWO_PI;
		} else {
			dir -= rotSpeed;
			if (dir < -PConstants.PI)
				dir += PConstants.TWO_PI;
		}
	}

	public void printPos() {
		PApplet.println();
		PApplet.print("Box: ");
		PApplet.print(curBox);
		PApplet.print(", Side: ");
		PApplet.print(getSide());
		PApplet.print(", [ ");
		PApplet.print(local2DCoord.x);
		PApplet.print(", ");
		PApplet.print(local2DCoord.y);
		PApplet.println(" ]");
	}

	private int edgeCheck(int side, PVector oldCoord) {
		int newside = side;
		int i;
		PVector newCoord = oldCoord.copy();
//		boolean xOverL = faceX > L;
//		boolean xUnderZero = faceX < 0;
//		boolean yOverL = faceY > L;
//		boolean yUnderZero = faceY < 0;
//		boolean isEven = side % 2 == 0;
		switch (side % 2) {
		case 0: // If the current side is an even integer (0,2,4) then do these actions
			if (newCoord.x > parent.sideLength) {
				newside = (side + 1) % 6;
				// To check whether a box "above" the side we want to move to is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 4) % 6;
					// From odd when x too high
					newCoord = oddXTooHigh(oldCoord);
					break;
				}
				// To check whether a box besides the current box is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					newCoord.x = oldCoord.x - parent.sideLength; 
					newside = side;
				} else {
					// If no boxes are present
					newCoord = evenXTooHigh(oldCoord);
				}
				break;
			} else if (newCoord.x < 0) {
				newside = (side + 4) % 6; // side+4 = side-2 (mod 6)
				// To check whether a box "above" the box we want to move to is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 1) % 6;
					// From odd when x too low
					newCoord = oddXTooLow(oldCoord); // Works for 0 -> 1
					break;
				}
				// To check whether a box besides the current box is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					newCoord.x = parent.sideLength - (-oldCoord.x);
					newside = side;
				} else {
					newCoord = evenXTooLow(oldCoord);
				}
				break;
			} else if (newCoord.y > parent.sideLength) {
				newside = (side + 2) % 6;
				// To check whether a box "above" the box we want to move to is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 5) % 6;
					// From odd when y too low
					newCoord = evenYTooHigh(oldCoord);
					break;
				}
				// To check whether a box besides the current box is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					newCoord.y = oldCoord.y - parent.sideLength;
					newside = side;
				} else {
					newCoord = evenYTooHigh(oldCoord);
				}
				break;
			} else if (newCoord.y < 0) {
				newside = (side + 5) % 6; // side+5 = side-1 (mode 6)
				// To check whether a box "above" the box we want to move to is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 2) % 6;
					// From odd when y too high
					newCoord = evenYTooLow(oldCoord);
					/*
					 * faceY = L-faceX; faceX = L-e; dir += -PI/2;
					 */
					break;
				}
				// To check whether a box besides the current box is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					newCoord.y = parent.sideLength - (-oldCoord.y);
					newside = side;
				} else {
					newCoord = evenYTooLow(oldCoord);
				}
				break;
			}
			newside = side;
		case 1: // If the current side is an odd integer (1,3,5) then do these actions
			if (newCoord.x > parent.sideLength) {
				newside = (side + 1) % 6;
				// To check whether a box "above" the box we want to move to is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 4) % 6;
					// From odd when y too high
					newCoord = evenXTooHigh(oldCoord); // Works for 5 -> 3
					break;
				}
				// To check whether a box besides the current box is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					newCoord.x = oldCoord.x-parent.sideLength;
					newside = side;
				} else {
					newCoord = oddXTooHigh(oldCoord);
				}
				break;
			} else if (newCoord.x < 0) {
				newside = (side + 4) % 6; // side+4 = side-2 (mode 6)
				// To check whether a box "above" the box we want to move to is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 1) % 6;
					// From odd when y too high
					newCoord = evenXTooLow(oldCoord);
					break;
				}
				// To check whether a box besides the current box is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					newCoord.x = parent.sideLength - (-oldCoord.x);
					newside = side;
				} else {
					newCoord = oddXTooLow(oldCoord);
				}
				break;
			} else if (newCoord.y > parent.sideLength) {
				newside = (side + 5) % 6; // side+5 = side-1 (mode 6)
				// To check whether a box "above" the box we want to move to is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 2) % 6;
					// From odd when y too high
					// evenYTooHigh();
					newCoord.y = oldCoord.x;
					newCoord.x = parent.sideLength - (oldCoord.y - parent.sideLength);
					dir += + PConstants.PI / 2;
					break;
				}
				// To check whether a box besides the current box is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					newCoord.y = oldCoord.y-parent.sideLength;
					newside = side;
				} else {
					newCoord = oddYTooHigh(oldCoord);
				}
				break;
			} else if (newCoord.y < 0) {
				newside = (side + 2) % 6;
				// To check whether a box "above" the box we want to move to is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), PVector.add(Vects[side], Vects[newside])));
				if (i != -1) {
					curBox = i;
					newside = (side + 5) % 6;
					// evenYTooLow();
					newCoord.y = oldCoord.x;
					newCoord.x = -oldCoord.y;
					dir += PConstants.PI / 2;
					break;
				}
				// To check whether a box besides the current box is present:
				i = getB().indexOf(PVector.add(getB().get(curBox), Vects[newside]));
				if (i != -1) {
					curBox = i;
					newCoord.y = parent.sideLength - (-oldCoord.y);
					newside = side;
				} else {
					newCoord = oddYTooLow(oldCoord);
				}
				break;
			}
			break;
		default:
			newside = side;
			break;
		}
//		System.out.println("Move:");
//		System.out.println(oldCoord);
//		System.out.println(newCoord);
		local2DCoord = new PVector(newCoord.x, newCoord.y, local2DCoord.z);
//		newCoord.x = faceX;
//		newCoord.y = faceY;
		return newside;
	}

	private PVector oddXTooHigh(PVector oldCoord) {
		float newX = parent.sideLength - oldCoord.y;
		float newY = oldCoord.x - parent.sideLength;
		dir += PConstants.PI / 2;
		return new PVector(newX, newY);
	}

	private PVector oddXTooLow(PVector oldCoord) {
		float newX = oldCoord.y;
		float newY = -oldCoord.x;
		dir += -PConstants.PI / 2;
		return new PVector(newX, newY);
	}

	private PVector oddYTooHigh(PVector oldCoord) {
		float newY = oldCoord.x;
		float newX = parent.sideLength - (oldCoord.y - parent.sideLength);
		dir += PConstants.PI / 2;
		return new PVector(newX, newY);
	}

	private PVector oddYTooLow(PVector oldCoord) {
		float newY = oldCoord.x;
		float newX = -oldCoord.y;
		dir += PConstants.PI / 2;
		return new PVector(newX, newY);
	}

	private PVector evenXTooHigh(PVector oldCoord) {
		float newX = oldCoord.y; // Correct
		float newY = parent.sideLength - (oldCoord.x - parent.sideLength); // Correct
		dir += -PConstants.PI / 2;
		return new PVector(newX, newY);
	}

	private PVector evenXTooLow(PVector oldCoord) {
		float newX = parent.sideLength - oldCoord.y; // Correct
		float newY = parent.sideLength - (-oldCoord.x); // Correct
		dir += PConstants.PI / 2;
		return new PVector(newX, newY);
	}

	private PVector evenYTooHigh(PVector oldCoord) {
		float newY = parent.sideLength - oldCoord.x;
		float newX = oldCoord.y - parent.sideLength; 
		dir += -PConstants.PI / 2;
		return new PVector(newX, newY);
	}

	private PVector evenYTooLow(PVector oldCoord) {
		float newY = parent.sideLength - oldCoord.x;
		float newX = parent.sideLength - (-oldCoord.y); 
		dir += -PConstants.PI / 2;
		return new PVector(newX, newY);
	}

	public BoxList getB() {
		return B;
	}
}