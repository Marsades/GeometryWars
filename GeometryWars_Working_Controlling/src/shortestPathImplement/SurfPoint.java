package shortestPathImplement;

import java.util.*;

import processing.core.PVector;

public class SurfPoint {

	public final PVector pos;
//	public final int cube;
//	public final int face;
	
	TreeSet<Integer> faceList = new TreeSet<>();
	public SurfPoint(PVector globalPos, int[] faces){
		this.pos = globalPos;
		for(int i = 0 ; i<faces.length;i++) {
			faceList.add(faces[i]);
		}
	}
}
