package GraphHandling;

import processing.core.*;

public class GraphHandler {

	Graph G;

	public void Cube() {
		G = new Graph();
	}

	// public Path Shortest(Pos a, Pos b) {
	//
	// }
	private class Matrix {

		private float xx;
		private float xy;
		private float xz;

		private float yx;
		private float yy;
		private float yz;

		private float zx;
		private float zy;
		private float zz;
		
		private float[] array;
		
		final float[] IDENTITY = {1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };

		Matrix() {
			this(new float[] {1f,0f,0f,0f,1f,0f,0f,0f,1f});
		}

		Matrix(float xx, float xy, float xz, float yx, float yy, float yz, float zx, float zy, float zz) {
			this.xx = xx;
			this.xy = xy;
			this.xz = xz;
			this.yx = yx;
			this.yy = yy;
			this.yz = yz;
			this.zx = zx;
			this.zy = zy;
			this.zz = zz;
			array = new float[] { xx, xy, xz, yx, yy, yz, zx, zy, zz };
		}

		Matrix(float[] values) {
			this.xx = values[0];
			this.xy = values[1];
			this.xz = values[2];
			this.yx = values[3];
			this.yy = values[4];
			this.yz = values[5];
			this.zx = values[6];
			this.zy = values[7];
			this.zz = values[8];
			array = values;
		}

		Matrix(float[][] values) {
			this.xx = values[0][0];
			this.xy = values[0][1];
			this.xz = values[0][2];
			this.yx = values[1][0];
			this.yy = values[1][1];
			this.yz = values[1][2];
			this.zx = values[2][0];
			this.zy = values[2][1];
			this.zz = values[2][2];
			array = new float[] { xx, xy, xz, yx, yy, yz, zx, zy, zz };
		}

		public PVector multiply(PVector vec) {
			return new PVector(this.xx * vec.x + this.xy * vec.y + this.xz * vec.z,
					this.yx * vec.x + this.yy * vec.y + this.yz * vec.z,
					this.zx * vec.x + this.zy * vec.y + this.zz * vec.z);
		}

		private Matrix rotateX(float theta) {
			return new Matrix(
					1f, 0f, 0f,
					0f, (float) Math.cos(theta), - (float) Math.sin(theta),
					0f, (float) Math.sin(theta), + (float) Math.cos(theta));
		}
		private Matrix rotateZ(float theta) {
			return new Matrix(
					(float) Math.cos(theta), (float) -Math.sin(theta), 0f,
					(float) Math.sin(theta), (float) Math.cos(theta), 0f,
					0f, 0f, 1f);
		}
		private Matrix rotateY(float theta) {
			return new Matrix(
					(float) Math.cos(theta), 0f, + (float) Math.sin(theta),
					0f, 1f, 0f,
					(float) -Math.sin(theta), 0f, + (float) Math.cos(theta));
		}
		public float[] val2Arr() {
			float[] f = { xx, xy, xz, yx, yy, yz, zx, zy, zz };
			return f;
		}
		public void arr2Val() {
			this.xx = array[0];
			this.xy = array[1];
			this.xz = array[2];
			this.yx = array[3];
			this.yy = array[4];
			this.yz = array[5];
			this.zx = array[6];
			this.zy = array[7];
			this.zz = array[8];
		}

		// Multiplying two matrices not in-place
		public Matrix multiply(Matrix A, Matrix B) {
			float[] out = new float[9];
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					for (int k = 0; k < 2; k++) {
						out[i+3*j] += A.array[i + 3*k] * B.array[k+3*j];
					} 
				}
			}
			return new Matrix(out);
		}

		public Matrix power(Matrix M, int n) throws Exception {
			if (n<0) throw new Exception("The integer cannot be negative.");
			else if (n == 0) {
//				float[][] f = { { 1f, 0f, 0f }, { 0f, 1f, 0f }, { 0f, 0f, 1f } };
				return new Matrix(IDENTITY);
			}
			else if (n == 1) {
				return M;
			}
			else if ((n % 2) == 1) {
				return multiply(M, power(M, n - 1)); 
			} else {
				return power(multiply(M, M), n/2);
			}
		}
	}
}