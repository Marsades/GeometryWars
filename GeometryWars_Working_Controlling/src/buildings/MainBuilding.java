package buildings;
import processing.core.*;

public class MainBuilding extends Building {
	PApplet parent;
	
	public MainBuilding(PApplet par){
		parent = par;
	}
	
	public void draw() {
		parent.box(1);
	}
}
