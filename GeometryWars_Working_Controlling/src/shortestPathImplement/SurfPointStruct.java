package shortestPathImplement;

import java.util.*;

import Math.MatrixTest;

public class SurfPointStruct {

	ArrayList<SurfPoint> list;
	ArrayList<ArrayList<Integer>> facePointList; // Lists for what points are adjacent to each face i.
	ArrayList<HashSet<Integer>> pointFaceList; // Lists of what faces are adjacent to point i.
	MatrixTest test;
	
	public SurfPointStruct() {
		facePointList = new ArrayList<ArrayList<Integer>>();
		pointFaceList = new ArrayList<HashSet<Integer>>();
	}

	public SurfPoint getPoint(int i) {
		return list.get(i);
	}

	public ArrayList<SurfPoint> getFace(int i) {
		ArrayList<SurfPoint> outputList = new ArrayList<SurfPoint>();
		Iterator<Integer> it = facePointList.get(i).iterator();
		while (it.hasNext()) {
			Integer integer = (Integer) it.next();
			outputList.add(list.get(integer));
		}
		return outputList;
	}

	public HashSet<Integer> getAdjacentFaces(int i) {
		return pointFaceList.get(i);
	}
}
