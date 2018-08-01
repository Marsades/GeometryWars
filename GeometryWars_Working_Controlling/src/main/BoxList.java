package main;
import java.util.*;
import java.math.*;
import Geometry.TerrainGenerator2d;
import Math.Quaternion;
import processing.core.*;
import shortestPathImplement.NavCoord;
import units.Unit;

public class BoxList {
	GeometryWars parent;
	public ArrayList<PVector> List = new ArrayList<>();
	//Physics
	private float mass = 1;
	private PVector origin;
	private PVector velocity;
	
	private PVector angSpeed; //Omega vector
	private PVector targetAngSpeed;
//	private float rotationAngle = 0; 
	private Quaternion orientation; //Theta/orientation unit-Quaternion telling the rotation of the coordinate system
//	private Quaternion targetOrientation;
	
	public NavCoord navCoord;
	
	private ArrayList<Unit> unitList;
	
	private final PVector[] Vects = { new PVector(1, 0, 0), new PVector(0, 1, 0), new PVector(0, 0, 1),
			new PVector(-1, 0, 0), new PVector(0, -1, 0), new PVector(0, 0, -1) };

	//Variables:
	private PVector p;
	int cursorX;
	int cursorY;
	int cursorZ;
	float sideLength;
	TerrainGenerator2d Terrain = new TerrainGenerator2d(5);
	String name;

	BoxList(GeometryWars par, float sideLength, PVector origin, String argName) {
		this.parent = par;
		this.origin = origin;
		this.velocity = new PVector(0, 0, 0);
		// this.rotation = rotation;
		this.List.add(new PVector(0, 0, 0));
		this.sideLength = sideLength;
		this.cursorX = 0;
		this.cursorY = 0;
		this.cursorZ = 0;
		angSpeed = new PVector(
					(float) (0.00*(2*Math.random() - 1.0)),
					(float) (0.00*(2*Math.random() - 1.0)),
					(float) (0.00*(2*Math.random() - 1.0))
							);
		targetAngSpeed = angSpeed;
		orientation = (new Quaternion(1,
				(float) (0*(2*Math.random() - 1.0)), 
				(float) (0*(2*Math.random() - 1.0)),
				(float) (0*(2*Math.random() - 1.0)))).normalize();
//		targetOrientation = orientation;
		name = argName;
		navCoord = new NavCoord(parent, sideLength);
		unitList = new ArrayList<Unit>();
	}

	BoxList(GeometryWars par, float sideLength) {
		this(par, sideLength, new PVector(0, 0, 0), "Default");
	}

	public BoxList(GeometryWars par, float sideLength, String argName) {
		this(par, sideLength, new PVector(0, 0, 0), argName);
	}

	public Quaternion getOrientation() {
		return this.orientation.copy();
	}
	void add(int xAdd, int yAdd, int zAdd) {
		p = new PVector(xAdd, yAdd, zAdd);
		if (!List.contains(p)) {
			List.add(p);
		}
		navCoord.addCube(p.copy().mult(sideLength));
	}

	public void draw() {
//		rotationAxis.add(v);
		parent.pushMatrix();
		parent.pushStyle();
//		parent.noFill();
		parent.fill(parent.color(255, 255, 255));
		parent.stroke(parent.color(255,255, 0));

		parent.translate(origin.x, origin.y, origin.z);
		
		update();
//		System.out.println(orientation.toString());
		parent.applyMatrix(orientation.toRotationMatrix());
		
		for (int i = 0; i < List.size(); i++) {
			p = List.get(i);
			parent.pushMatrix();
			parent.translate(p.x * sideLength, p.y * sideLength, p.z * sideLength);
//			parent.box(sideLength);
//			drawTerrains();

			parent.popMatrix();
		}
		for (int i = 0; i < Vects.length; i++) {
			PVector markerPos = Vects[i].copy().mult(sideLength*0.5f);
			parent.pushMatrix();
			parent.fill(parent.color(127 * Vects[i].x + 128, 127 * Vects[i].y + 128, 127 * Vects[i].z + 128));
//			parent.translate();
			parent.text(i, markerPos.x, markerPos.y, markerPos.z);
//			parent.box(10);
			parent.popMatrix();
		}
		
		for (Iterator<Unit> it = unitList.iterator(); it.hasNext(); ) {
			Unit unit = it.next();
			unit.draw();
		}
		
		navCoord.draw();
		
		parent.popStyle();
		parent.popMatrix();
	}

	private void update() {
		Quaternion qDot = Quaternion.star(angSpeed).rightMult(orientation).scalarMult(0.5f);
		orientation = orientation.add(qDot).normalize();
//		targetOrientation = targetOrientation.add(qDot).normalize();
		
		PVector angSpeedDiff = targetAngSpeed.copy().sub(angSpeed);
//		Quaternion orientationDiff = targetOrientation.rightMult(orientation.conjugate());
//		Quaternion change = orientationDiff.pow(0.2f);
//		float angleDiff = (float) (2*Math.acos(orientationDiff.W));
		
		this.origin = PVector.add(this.origin, velocity.copy());
		
//		System.out.println(this.name + "'s rotation: " + orientation.toString());
//		System.out.println("Angle difference: "+ angleDiff);
		
		if (angSpeedDiff.mag() > 0.00001f) {
			System.out.println("Applying torque: " + angSpeedDiff.toString());
			applyTorque(angSpeedDiff.div(100f));
		}
//		if (angleDiff > 0.0001f) {
//			System.out.println("Change in orientation: " + change.toString());
//			orientation = orientationDiff.pow(0.2f).rightMult(orientation);
//		}
	}

	public void applyTorque(PVector torque) {
		angSpeed = PVector.add(angSpeed, torque);
	}
	public void applyForce(PVector force) {
		velocity = PVector.add(velocity, force.div(mass));
	}

	public PVector get(int i) {
		return List.get(i);
	}

	public int indexOf(PVector v) {
		return List.indexOf(v);
	}

	public void drawTerrains() {

		parent.translate(-sideLength / 2, -sideLength / 2, sideLength / 2);

		drawTerrain(sideLength, sideLength / 4, 4);

		parent.rotateY(PConstants.HALF_PI);
		drawTerrain(sideLength, sideLength / 4, 4);

		parent.rotateX(PConstants.HALF_PI);
		drawTerrain(sideLength, sideLength / 4, 4);

		parent.rotateY(PConstants.HALF_PI);
		drawTerrain(sideLength, sideLength / 4, 4);

		parent.rotateZ(PConstants.HALF_PI);
		drawTerrain(sideLength, sideLength / 4, 4);

		parent.rotateZ(PConstants.PI);
		drawTerrain(sideLength, sideLength / 4, 4);
	}
	public void drawTerrain(float size, float vertSize, int M) {
		parent.noStroke();
		// parent.stroke(parent.color(0,0,0));
		parent.fill(parent.color(255, 255, 255));
		float step = 1.0f / (float) M;
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < M; j++) {
				parent.fill(parent.color(255, 255 * (1 - Terrain.eval(new float[] { i * step, j * step })), 0));
				parent.beginShape(PConstants.TRIANGLE_FAN);
				parent.vertex((float) size * i * step, (float) size * j * step,
						(float) Terrain.eval(new float[] { i * step, j * step }) * vertSize);
				parent.vertex((float) size * (i + 1) * step, (float) size * j * step,
						(float) Terrain.eval(new float[] { (i + 1) * step, j * step }) * vertSize);
				parent.vertex((float) size * (i + 1) * step, (float) size * (j + 1) * step,
						(float) Terrain.eval(new float[] { (i + 1) * step, (j + 1) * step }) * vertSize);
				parent.vertex((float) size * i * step, (float) size * (j + 1) * step,
						(float) Terrain.eval(new float[] { i * step, (j + 1) * step }) * vertSize);
				parent.endShape();
			}
		}
	}

	public PVector getOrigin() {
		return origin.copy();
	}

	public PVector getAngSpeed() {
		return angSpeed.copy();
	}

	public void setAngSpeed(PVector A) {
		angSpeed = A.copy();
	}

	public void setTargetAngSpeed(PVector target) {
		targetAngSpeed = target.copy();
		System.out.println("Target set to: " + target.toString());
	}

	public void addUnit(Unit e) {
		unitList.add(e);
	}
//	public void setTargetOrientation(Quaternion c) {
//		targetOrientation = c.normalize();
//		System.out.println("Target orientation set to: " + c.toString());
//	}
}