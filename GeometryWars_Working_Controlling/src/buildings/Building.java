package buildings;
import processing.core.*;
import Geometry.*;

public abstract class Building {
	boolean underConstruction;
	public int health;
	int timer;
	int side;
	int box;
	PVector P;
	Shape shape;
	public void draw() {
		shape.draw();
	}
	
}
