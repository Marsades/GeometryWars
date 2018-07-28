package Geometry;
import processing.core.*;

public class Geometry {
	PApplet parent;

	public Geometry(PApplet par) {
		parent = par;
	}

	public void tetrahedron(float size) {
		parent.pushMatrix();
		parent.scale(size / 100);
		parent.beginShape(PApplet.TRIANGLES);
		// The shape vertices:
		/*
		 * parent.vertex(100, 100, 100); parent.vertex(-100, -100, 100);
		 * parent.vertex(-100, 100, -100); parent.vertex(100, -100, -100);
		 * parent.vertex(-100/3, -100/3, -100/3);
		 */
		parent.vertex(100, 100, 100);
		parent.vertex(-100, -100, 100);
		parent.vertex(-100, 100, -100);

		parent.vertex(100, 100, 100);
		parent.vertex(-100, -100, 100);
		parent.vertex(100, -100, -100);

		parent.vertex(100, 100, 100);
		parent.vertex(-100, 100, -100);
		parent.vertex(100, -100, -100);

		parent.vertex(-100, -100, 100);
		parent.vertex(-100, 100, -100);
		parent.vertex(100, -100, -100);

		parent.endShape();
		parent.popMatrix();
	}

	//Creates a Tetrahedron with an extendable side
	public void tetrahedronE(float size, float b) {
		//assume(-0.5 < b < 0.5);
		
		parent.pushMatrix();
		parent.scale(size / 100);
		float a = 2 * b + 1;
		parent.beginShape(PConstants.TRIANGLES);
		// The shape vertices:
		/*
		 * parent.vertex(100, 100, 100); parent.vertex(-100, -100, 100);
		 * parent.vertex(-100, 100, -100); parent.vertex(100, -100, -100);
		 * parent.vertex(-100/3, -100/3, -100/3);
		 */
		parent.vertex(100, 100, 100);
		parent.vertex(-100, -100, 100);
		parent.vertex(-100, 100, -100);

		parent.vertex(100, 100, 100);
		parent.vertex(-100, -100, 100);
		parent.vertex(100, -100, -100);

		parent.vertex(100, 100, 100);
		parent.vertex(-100, 100, -100);
		parent.vertex(100, -100, -100);

		parent.vertex(-100, -100, 100);
		parent.vertex(-100, 100, -100);
		parent.vertex(-a * 100 / 3, -a * 100 / 3, -a * 100 / 3);

		parent.vertex(-100, -100, 100);
		parent.vertex(100, -100, -100);
		parent.vertex(-a * 100 / 3, -a * 100 / 3, -a * 100 / 3);

		parent.vertex(100, -100, -100);
		parent.vertex(-100, 100, -100);
		parent.vertex(-a * 100 / 3, -a * 100 / 3, -a * 100 / 3);

		parent.endShape();
		parent.popMatrix();
	}
	public void box(float size) {
		parent.box(size);
	}
	public void octahedron(float size) {
		parent.pushMatrix();
		parent.scale(size / 100);
		parent.beginShape(PApplet.TRIANGLE_FAN);
		parent.vertex(100,0,0);
		parent.vertex(0,100,0);
		parent.vertex(0,0,100);
		parent.vertex(0,-100,0);
		parent.vertex(0,0,-100);
		parent.vertex(0,100,0);
		parent.endShape();
		
		parent.beginShape(PApplet.TRIANGLE_FAN);
		parent.vertex(-100,0,0);
		parent.vertex(0,100,0);
		parent.vertex(0,0,100);
		parent.vertex(0,-100,0);
		parent.vertex(0,0,-100);
		parent.vertex(0,100,0);
		parent.endShape();
		parent.popMatrix();
	}
	public void pyramid(float size) {
		pyramidE(size, 1);
	}
	public void pyramidE(float size, float b) {
		parent.pushMatrix();
		parent.scale(size / 100);
		parent.beginShape(PApplet.TRIANGLE_FAN);
		parent.vertex(50 * (1-b),0,0);
		parent.vertex(50,50,50);
		parent.vertex(50,50,-50);
		parent.vertex(50,-50,-50);
		parent.vertex(50,-50,50);
		parent.vertex(50,50,50);
		parent.endShape();
		parent.popMatrix();
		
	}

}