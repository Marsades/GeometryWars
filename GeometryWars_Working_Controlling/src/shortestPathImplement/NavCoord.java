package shortestPathImplement;

import java.util.*;

import main.GeometryWars;
import processing.core.PVector;

public class NavCoord {
	// Internal distances between edges of one cube
	PathMatrixPair matPairConstant;
	ArrayList<SurfPoint> curPoints2;
	
	// The current edges and distances between points of one position and another 
	PathMatrixPair matPair;
	ArrayList<SurfPoint> curPoints;
	SurfPoint start;
	SurfPoint end;

	GeometryWars parent;
	float sideLength;
	LinkedList<Integer> currentPath;
	int i;
	int resolution = 3;

	public NavCoord(GeometryWars par, float sideL) {
		this.parent = par;
		this.sideLength = sideL;
		curPoints2 = cubeEdge(resolution, new PVector(0,0,0));
		curPoints = new ArrayList<SurfPoint>();
		curPoints.addAll(curPoints2);
		matPair = createConnectionMatrix(curPoints2);
		matPairConstant = AllPairShortestPath(matPair);
		
		start = curPoints2.get(0);
		end = curPoints2.get(1);
		updateStartEndPair(start, end);
		
		// printMatrix("Initial", matPair.distances);
		// printMatrix("Shortest route", matPairConstant.route);
		
		printRoute(matPair.route, 3, 5);
		currentPath = getRoute(matPair.route, 3, 5);
		i = 0;
	}

	public void getFace() {
		
	}
	public void draw() {
		i += 1;
//		System.out.print("i = " + i);
		Iterator<SurfPoint> it1 = curPoints.iterator();
		while (it1.hasNext()) {
			SurfPoint p = it1.next();
			parent.pushMatrix();
			parent.translate(p.pos.x, p.pos.y, p.pos.z);
			parent.box(3);
			parent.popMatrix();
		}
		int[] face0 = new int[] { 3, 4, 9, 10 };
		for (int i = 0; i < face0.length; i++) {
			SurfPoint p = curPoints2.get(face0[i]);
			parent.pushMatrix();
			parent.translate(p.pos.x, p.pos.y, p.pos.z);
			parent.fill(0, 255, 0);
			parent.box(4);
			parent.popMatrix();
		}
//		System.out.print(", CP-size: " + currentPath.size());
		Iterator<Integer> it2 = currentPath.iterator();
		int a = it2.next();
		int b;
		while (it2.hasNext()) {
			b = it2.next();
			PVector A = curPoints.get(a).pos;
			PVector B = curPoints.get(b).pos;
			parent.line(A.x, A.y, A.z, B.x, B.y, B.z);
			a = b;
		}
//		System.out.println();
	}

	public ArrayList<SurfPoint> cubeEdge(int K, PVector origin) {
		ArrayList<SurfPoint> squarePointList = new ArrayList<>();
		float L = sideLength/2;
		for (int i = 1; i <= K; i++) {
			float t = i * 2 * L / (K + 1) - L;
			squarePointList.add(new SurfPoint(new PVector( L,  L, t).add(origin), new int[] { 0, 1 }));
			squarePointList.add(new SurfPoint(new PVector(t,  L,  L).add(origin), new int[] { 1, 2 }));
			squarePointList.add(new SurfPoint(new PVector( L, t,  L).add(origin), new int[] { 0, 2 }));

			squarePointList.add(new SurfPoint(new PVector(-L,  L, t).add(origin), new int[] { 1, 3 }));
			squarePointList.add(new SurfPoint(new PVector(-L, -L, t).add(origin), new int[] { 3, 4 }));
			squarePointList.add(new SurfPoint(new PVector( L, -L, t).add(origin), new int[] { 4, 0 }));

			squarePointList.add(new SurfPoint(new PVector(t, -L,  L).add(origin), new int[] { 2, 4 }));
			squarePointList.add(new SurfPoint(new PVector(t, -L, -L).add(origin), new int[] { 4, 5 }));
			squarePointList.add(new SurfPoint(new PVector(t,  L, -L).add(origin), new int[] { 5, 1 }));

			squarePointList.add(new SurfPoint(new PVector(-L, t,  L).add(origin), new int[] { 2, 3 }));
			squarePointList.add(new SurfPoint(new PVector(-L, t, -L).add(origin), new int[] { 3, 5 }));
			squarePointList.add(new SurfPoint(new PVector( L, t, -L).add(origin), new int[] { 5, 0 }));
		}
		return squarePointList;
	}

	public PathMatrixPair createConnectionMatrix(ArrayList<SurfPoint> P) {
		PathMatrixPair tempPair = new PathMatrixPair(P.size());
		for (int i = 0; i < curPoints2.size() - 1; i++) {
			SurfPoint a = curPoints2.get(i);
			for (int j = i + 1; j < curPoints2.size(); j++) {
				SurfPoint b = curPoints2.get(j);
				TreeSet<Integer> z = (TreeSet<Integer>) b.faceList.clone();
				z.retainAll(a.faceList);
				if (!z.isEmpty()) {
					float d = a.pos.dist(b.pos);
					if (tempPair.distances[i][j] > d) {
						tempPair.distances[i][j] = d;
						tempPair.distances[j][i] = d;
					}
				}
			}
		}
		return tempPair;
	}

	public PathMatrixPair appendToMatrix(PathMatrixPair M, SurfPoint a) {
		float[] distA = new float[curPoints.size()];
		
		PathMatrixPair newM = M.clone();
		
		for (int i = 0; i < curPoints.size(); i++) {
			SurfPoint P = curPoints.get(i);
			TreeSet<Integer> zA = (TreeSet<Integer>) P.faceList.clone();
			zA.retainAll(a.faceList);
			if (!zA.isEmpty()) {
				distA[i] = P.pos.dist(a.pos);
			} else {
				distA[i] = Float.MAX_VALUE;
			}
		}
		newM = AllPairShortestPath( newM.appendVector(distA) );
		return newM;
	}

	public void addCube(PVector newPos) {
		curPoints2.addAll(cubeEdge(resolution, newPos));
		curPoints = (ArrayList<SurfPoint>) curPoints2.clone();
		removeRedundant(curPoints);
		matPairConstant = createConnectionMatrix(curPoints2); 
		matPair = matPairConstant.clone();
	}
	
	private void removeRedundant(ArrayList<SurfPoint> curPoints2) {
		HashSet<Integer> removeArray = new HashSet<Integer>();
		for (int i = 0; i < curPoints2.size() - 1; i++) {
			SurfPoint a = curPoints2.get(i);
			for (int j = i + 1; j < curPoints2.size(); j++) {
				SurfPoint b = curPoints2.get(j);
				TreeSet<Integer> z = (TreeSet<Integer>) b.faceList.clone();
				z.retainAll(a.faceList);
				if (!z.isEmpty()) {
					float d = a.pos.dist(b.pos);
					if (d < 0.0001) {
						removeArray.add(j);
					}
				}
			}
		}
		for (Integer integer : removeArray) {
			curPoints.remove(integer);
		}
	}

	public static PathMatrixPair AllPairShortestPath(PathMatrixPair W) {
		// Initialization;
		int N = W.length;
		printMatrix("Input distances: ", W.distances);
		printMatrix("Input routes: ", W.route);
		PathMatrixPair output = (PathMatrixPair) W.clone();

		for (int k = 0; k < N; k++) {
			for (int i = 0; i < N; i++) {
				// int M = Integer.MAX_VALUE;
				for (int j = 0; j < N; j++) {
					if (output.distances[i][k] != Float.MAX_VALUE && output.distances[k][j] != Float.MAX_VALUE
							&& output.distances[i][j] > output.distances[i][k] + output.distances[k][j]) {
						output.distances[i][j] = output.distances[i][k] + output.distances[k][j];
						output.route[i][j] = output.route[i][k];
					}
				}
			}
		}
		printMatrix("Output distances: ", output.distances);
		printMatrix("Output routes: ", output.route);
		return output;
	}

	public void updateStartEndPair(SurfPoint A, SurfPoint B) {
		start = A;
		end = B;
		curPoints = (ArrayList<SurfPoint>) curPoints2.clone();
		matPair = appendToMatrix(matPairConstant.clone(), start);
		curPoints.add(start);
		matPair = appendToMatrix(matPair, end);
		curPoints.add(end);
		
//		System.out.println("Updating Start and end point: ");
//		printMatrix("Input distances: ", matPair.distances);
//		printMatrix("Input routes: ", matPair.route);
		matPair = AllPairShortestPath(matPair);
//		printMatrix("Output distances: ", matPair.distances);
//		printMatrix("Output routes: ", matPair.route);
		
		currentPath = getRoute(matPair.route, matPair.length - 2, matPair.length - 1);
		System.out.println(currentPath.toString());
	}

	// Finds a route by constructing a -> b => a -> z1 -> b => a -> z1 -> z2 -> b
	// etc.
	public LinkedList<Integer> getRoute(int[][] shortestRoute, int a, int b) {
		printMatrix("Routes Mat: ", shortestRoute);
		LinkedList<Integer> path = new LinkedList<>();
		path.addFirst(a);
		int z = a;
		while (z != b) {
			z = shortestRoute[z][b];
			path.addLast(z);
		}
		return path;
	}

	// Finds a route by constructing a -> b => a -> z1 -> b => a -> z1 -> z2 -> b
	// etc.
	public void printRoute(int[][] shortestRoute, int a, int b) {
		LinkedList<Integer> path = getRoute(shortestRoute, a, b);
		System.out.println(path.toString());
	}

	public static void printMatrix(String title, int[][] y) {
		int N = y.length;
		System.out.println(title);
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				int cur = y[i][j];
				if (cur == Integer.MAX_VALUE) {
					System.out.print("i ");
				} else {
					System.out.print(String.format("%02d ", cur));
				}
			}
			System.out.println();
		}
	}

	public static void printMatrix(String title, float[][] y) {
		int N = y.length;
		System.out.println(title);
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				float cur = y[i][j];
				if (cur == Float.MAX_VALUE) {
					System.out.print("infinite ");
				} else {
					System.out.print(String.format("%8.2f ", cur));
				}
			}
			System.out.println();
		}
	}
}
