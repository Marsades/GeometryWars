package Geometry;

import java.math.*;
import java.util.*;
import processing.core.*;

public class TerrainGenerator2d {

	public float[][] weights;
	int Ncoeff;

	public TerrainGenerator2d(int M) {
		Ncoeff = M;
		weights = new float[Ncoeff][Ncoeff];
		for (int i = 0; i < Ncoeff; i++) {
			for (int j = 0; j < Ncoeff; j++) {
				weights[i][j] = (float) (2 * Math.random() - 1);
			}
		}
	}

	public float eval(float[] p) {
		float h = 0;
		for (int i = 0; i < Ncoeff; i++) {
			for (int j = 0; j < Ncoeff; j++) {
				h = (float) (h + Math.sin(Math.PI * (i + 1) * p[0]) * Math.sin(Math.PI * (j + 1) * p[1]) * weights[i][j]
						/ ((i + 1) * (j + 1)));
			}
		}
		return h;
	}

	public float[][] getHeights(int N) {
		float[][] heights = new float[N][N];
		for (int x = 0; x < N; x++) {
			for (int y = 0; y < N; y++) {
				float[] p = { x / N, y / N };
				heights[x][y] = eval(p);
			}
		}
		return heights;
	}
}
