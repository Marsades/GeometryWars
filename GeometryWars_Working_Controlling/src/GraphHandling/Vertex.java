package GraphHandling;

import processing.core.*;

public class Vertex {
	public Face rightFace;
	public Face leftFace;
	public PVector position;
	Vertex(PVector pos){
		position = pos;
	}
}
