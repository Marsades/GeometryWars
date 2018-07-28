package shortestPathImplement;

public class PathMatrixPair {

	public int[][] route;
	public float[][] distances;
	public int length;

	public PathMatrixPair(int length, int[][] route, float[][] distances) {
		this.length = route.length;
		this.route = route;
		this.distances = distances;
	}

	public PathMatrixPair(int N) {
		this.length = N;
		this.route = new int[N][N];
		this.distances = new float[N][N];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				this.route[i][j] = j;
				if (i != j) {
					this.distances[i][j] = Float.MAX_VALUE;
				} else {
					this.distances[i][j] = 0f;
				}
			}
		}
	}

	public String toString() {
		String S = new String();
		for (int i = 0; i < distances.length; i++) {
			for (int j = 0; j < distances.length; j++) {
				if (distances[i][j] != Float.MAX_VALUE) {
					S += String.format("%8.2f ", distances[i][j]);
				} else {
					S += "infinite ";
				}
			}
			S += "\n";
		}
		return S;
	}

	public PathMatrixPair clone() {
		return new PathMatrixPair(this.length, this.route, this.distances.clone());
	}
	public void setDistances(float[][] D) {
		for (int i = 0; i < D.length; i++) {
			for (int j = 0; j < D[0].length; j++) {
				this.distances[i][j] = D[i][j];
			}
		}
	}

	public PathMatrixPair appendVector(float[] dist) {
		float[][] newDistances = new float[this.length + 1][this.length + 1];
		int[][] newRoute = new int[this.length + 1][this.length + 1];
		for (int i = 0; i < this.length; i++) {
			for (int j = 0; j < this.length; j++) {
				newDistances[i][j] = this.distances[i][j];
				newRoute[i][j] = this.route[i][j];
			}
		}
		for (int i = 0; i < this.length; i++) {
			newDistances[i][this.length] = dist[i];
			newRoute[i][this.length] = this.length;
			newDistances[this.length][i] = dist[i];
			newRoute[this.length][i] = i;
		}

		newDistances[this.length][this.length] = 0f;
		newRoute[this.length][this.length] = this.length;

		return new PathMatrixPair(length + 1, newRoute, newDistances);
	}
	// printRoute(){
	//
	// }
	// printDistances(){
	//
	// }
	//
}
