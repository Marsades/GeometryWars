package GraphHandling;
import java.io.*;
import java.util.*;

public class Graph {
	int N;
	private LinkedList<Edge> graphList;
	private final static double inf = Double.MAX_VALUE;

	// Shortest path
	double[][] distances;
	int[][] paths;

	Graph() {
		graphList = new LinkedList<>();
//		graphList.add(new Edge(0, 1, 1));
//		graphList.add(new Edge(1, 2, 1));
//		graphList.add(new Edge(2, 3, 1));
//		graphList.add(new Edge(2, 4, 1));
	}

	public void buildGraph(double[][] adjMatrix) {
		N = adjMatrix.length;
		graphList = new LinkedList<Edge>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (i != j && adjMatrix[i][j] != inf) {
					Edge E = new Edge(i, j, adjMatrix[i][j]);
					if ( !graphList.contains(E) ) {
						graphList.add(E);
					}
				}
			}
		}
	}

	public void buildAdjMatrix(int V, LinkedList<Edge> L) {
		double[][] out = new double[V][V];
		for (int i = 0; i < V; i++) {
			for (int j = 0; j < V; j++) {
				if (i == j)
					out[i][j] = 0;
				else
					out[i][j] = Double.MAX_VALUE;
			}
		}
		for (Edge E : L) {
			out[E.from][E.to] = E.weight;
		}

		distances = out;
	}

	public int getSize() {
		return graphList.size();
	}

	void add(int a, int b, int weight) {
		graphList.add(new Edge(a, b, weight));
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
					System.out.print(cur + " ");
				}
			}
			System.out.println();
		}
	}
	// Floyd-Warshall algorithm of shortest path
	public void AllPairShortestPath(double[][] W) {
		// Initialization;
		int N = W.length;
		double[][] Out = new double[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				paths[i][j] = j;
			}
		}
		// printMatrix("Init next: ", Out[1]);
		for (int k = 0; k < N; k++) {
			for (int i = 0; i < N; i++) {
				// int M = Integer.MAX_VALUE;
				for (int j = 0; j < N; j++) {
					if (Out[i][k] != Integer.MAX_VALUE && Out[k][j] != Integer.MAX_VALUE
							&& Out[i][j] > Out[i][k] + Out[k][j]) {
						Out[i][j] = Out[i][k] + Out[k][j];
						paths[i][j] = paths[i][k];
					}
				}
			}
		}
		// printMatrix("Output: ", Out);
		// printMatrix("Final next: ", Out[1]);
		distances = Out;
	}

	public LinkedList<Integer> buildPath(int a, int b) {
		LinkedList<Integer> Path = new LinkedList<Integer>();
		Path.add(a);
		int next = paths[a][b];
		while (next != b) {
			Path.add(next);
			next = paths[next][b];
		}
		Path.add(b);
		return Path;
	}

	public void printRoute(int a, int b) {
		if (a <= N && b <= N) {
			System.out.println("Length: " + paths[a][b]);
			LinkedList<Integer> L = buildPath(a, b);
			Iterator<Integer> it = L.iterator();
			while (it.hasNext()) {
				System.out.print(it.next() + ", ");
			}
		} else {
			System.out.println("No such vertices exists.");
		}
	}

	public void longestMinimumRoute() {
		int M = Integer.MAX_VALUE;
		int indexX = 0;
		int indexY = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (M > paths[i][j]) {
					M = paths[i][j];
					indexX = i;
					indexY = j;
				}
			}
		}
		printRoute(indexX, indexY);
	}
}

class Edge {
	public int from;
	public int to;
	public double weight;

	public Edge(int f, int t, double w) {
		from = f;
		to = t;
		weight = w;
	}
}
