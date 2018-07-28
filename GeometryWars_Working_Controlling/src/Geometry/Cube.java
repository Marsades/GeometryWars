package Geometry;
import processing.core.*;

public class Cube extends Shape {
	PVector P;
	PApplet parent;
	float size;
	public void draw(){
		parent.pushMatrix();
		parent.translate(P.x, P.y, P.z);
		parent.box(size);
		parent.popMatrix();
	}
}
