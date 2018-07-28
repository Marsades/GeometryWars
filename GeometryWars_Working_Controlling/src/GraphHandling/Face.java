package GraphHandling;

import java.util.ArrayList;

public class Face {
	public ArrayList<Vertex> adjacentVertices = new ArrayList<Vertex>();
	Face(Vertex... verts){
		for (int i = 0; i < verts.length; i++) {
			adjacentVertices.add(verts[i]);
		}
	}
}