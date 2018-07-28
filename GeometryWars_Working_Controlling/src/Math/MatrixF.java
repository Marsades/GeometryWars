package Math;

public class MatrixF {
	boolean transpose = false;
	private boolean square = false;
	private int nRows; // Height
	private int nCols; // Width
	final float[][] arr;

	public MatrixF(int N) {
		this(N, N);
	}
	
	public MatrixF(int N, int M) {
		this.nRows = N;
		this.nCols = M;
		arr = new float[N][M];
	}

	public MatrixF(float[] input, int nRows, int nCols) {
		this.nRows = nRows;
		this.nCols = nCols;
		float[][] newArr = new float[nRows][nCols];
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				newArr[i][j] = input[i + nRows * j];
			}
		}
		this.arr = newArr;
	}

	public MatrixF(float[][] input) {
		this.arr = input.clone();
		this.nRows = input.length;
		this.nCols = input[0].length;
	}

	public MatrixF(float[][] input, int nRows, int nCols, boolean transpose) {
		this.arr = input.clone();
		this.nRows = input.length;
		this.nCols = input[0].length;
		this.transpose = transpose;
	}

	public MatrixF dag() {
		MatrixF mat = this.clone();
		mat.transpose = !mat.transpose;
		mat.nCols = this.nRows;
		mat.nRows = this.nCols;
		return mat;
	}

	public float get(int i, int j) {
		if (transpose) {
			return arr[j][i];
		} else {
			return arr[i][j];
		}
	}

	public void set(float a, int row, int col) {
		arr[row][col] = a;
	}

	public static MatrixF diagonal(int n, float[] arr) {
		float[][] newArr = new float[n][n];
		for (int i = 0; i < n; i++) {
			newArr[i][i] = arr[i];
		}
		MatrixF mat = new MatrixF(newArr);
		return mat;
	}

	public static MatrixF identity(int n) {
		float[][] newArr = new float[n][n];
		for (int i = 0; i < n; i++) {
			newArr[i][i] = 1;
		}
		MatrixF mat = new MatrixF(newArr);
		return mat;
	}

	public static MatrixF zero(int n) {
		float[][] newArr = new float[n][n];
		MatrixF mat = new MatrixF(newArr);
		return mat;
	}

	public static MatrixF circulant(int n) {
		float[][] newArr = new float[n][n];
		for (int i = 0; i < n - 1; i++) {
			newArr[i][i + 1] = 1;
		}
		newArr[(n - 1)][0] = 1;
		MatrixF mat = new MatrixF(newArr);
		return mat;
	}

	public MatrixF getSubMatrix(int x1, int x2, int y1, int y2) {
		MatrixF mat = new MatrixF(x2 - x1 + 1, y2 - y1 + 1);
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				mat.set(this.get(x, y), x - x1, y - y1);
			}
		}
		return mat;
	}

	public MatrixF setSubMatrix(int x1, int y1, MatrixF A) {
		MatrixF mat = this.clone();
		for (int x = x1; x < x1 + A.getRowDimension(); x++) {
			for (int y = y1; y < y1 + A.getColDimension(); y++) {
				mat.set(A.get(x - x1, y - y1), x, y);
			}
		}
		return mat;
	}

	public static MatrixF reverse(int n) {
		float[] newArr = new float[n * n];
		for (int i = 0; i < n; i++) {
			newArr[i + n * (n - 1 - i)] = 1;
		}
		MatrixF mat = new MatrixF(newArr, n, n);
		return mat;
	}

	public MatrixF compose(MatrixF A) {
		int newRows = this.nRows + A.nRows;
		int newCols = this.nCols + A.nCols;
		MatrixF mat = new MatrixF(newRows, newCols);
		for (int i = 0; i < this.nRows; i++) {
			for (int j = 0; j < this.nCols; j++) {
				mat.set(this.get(i, j), i, j);
			}
		}
		for (int i = 0; i < A.nRows; i++) {
			for (int j = 0; j < A.nCols; j++) {
				mat.set(A.get(i, j), i + this.nRows, j + this.nCols);
			}
		}
		return mat;
	}

	public MatrixF add(MatrixF A) {
		MatrixF mat = this.clone();
		for (int i = 0; i < A.nRows; i++) {
			for (int j = 0; j < A.nCols; j++) {
				mat.set(this.get(i, j) + A.get(i, j), i, j);
			}
		}
		return mat;
	}

	public static MatrixF add(MatrixF A, MatrixF B) {
		MatrixF mat = A.clone();
		for (int i = 0; i < A.nRows; i++) {
			for (int j = 0; j < A.nCols; j++) {
				mat.set(A.get(i, j) + B.get(i, j), i, j);
			}
		}
		return mat;
	}

	public MatrixF mult(float a) {
		MatrixF mat = this.clone();
		for (int i = 0; i < this.nRows; i++) {
			for (int j = 0; j < this.nCols; j++) {
				mat.set(a * this.get(i, j), i, j);
			}
		}
		return mat;
	}

	public static MatrixF mult(MatrixF M, float a) {
		MatrixF mat = (MatrixF) M.clone();
		for (int i = 0; i < M.nRows; i++) {
			for (int j = 0; j < M.nCols; j++) {
				mat.set(a * M.get(i, j), i, j);
			}
		}
		return mat;
	}

	public static MatrixF MatMult(MatrixF... matrixs) {
		MatrixF mat = MatrixF.identity(matrixs[0].nCols);
		for (int i = 0; i < matrixs.length; i++) {
			mat.MatMult(matrixs[i]);
		}
		return mat;
	}

	public MatrixF MatMult(MatrixF A) {
		if (this.nCols != A.nRows) {
			System.out.print("Dimensions doesn't match!");
		}
		MatrixF mat = new MatrixF(this.nRows, A.nCols);
		for (int i = 0; i < mat.nRows; i++) {
			for (int j = 0; j < mat.nCols; j++) {
				float S = 0;
				for (int k = 0; k < A.nRows; k++) {
					S += this.get(i, k) * A.get(k, j);
				}
				mat.set(S, i, j);
			}
		}
		return mat;
	}

	public MatrixF Power(int n) {
		MatrixF mat = MatrixF.identity(this.nCols);
		for (int i = 0; i < n; i++) {
			mat = mat.MatMult(this);
		}
		return mat;
	}

	public MatrixF KroneckerProduct(MatrixF B) {
		MatrixF M = new MatrixF(this.nRows * B.nRows, this.nCols * B.nCols);
		for (int i1 = 0; i1 < B.nRows; i1++) {
			for (int j1 = 0; j1 < B.nCols; j1++) {
				for (int i2 = 0; i2 < this.nRows; i2++) {
					for (int j2 = 0; j2 < this.nCols; j2++) {
						M.set(B.get(i1, j1) * this.get(i2, j2), i2 + i1 * this.nRows, j2 + j1 * this.nCols);
					}
				}
			}
		}
		return M;
	}

	public MatrixF clone() {
		return new MatrixF(this.arr, this.nRows, this.nCols, this.transpose);
	}

	public String toString() {
		String S = "";
		for (int i = 0; i < this.nRows; i++) {
			S += "[";
			for (int j = 0; j < this.nCols; j++) {
				float d = this.get(i, j);
				if (d == Float.MAX_VALUE) {
					S += "infi ";
				} else {
					S += String.format("%4.2f ", this.get(i, j));
				}
			}
			S += "]\n";
		}
		return S;
	}

	public void print() {
		System.out.println(toString());
	}

	public int getRowDimension() {
		return nRows;
	}

	public int getColDimension() {
		return nCols;
	}

	public float highestEigenvalue() {
		float[] arr = new float[this.nRows];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (float) (2 * Math.random() - 1);
		}
		MatrixF v = new MatrixF(arr, nRows, 1);
		for (int i = 0; i < 10; i++) {
			v = v.normalize();
			v = this.MatMult(v);
		}

		return v.norm();
	}

	public MatrixF highestEigenvector() {
		float[] arr = new float[this.nRows];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (float) (2 * Math.random() - 1);
		}
		MatrixF v = new MatrixF(arr, nRows, 1);
		for (int i = 0; i < 20; i++) {
			v = v.normalize();
			// System.out.println(v.trans());
			v = this.MatMult(v);
		}
		return v.normalize();
	}

	public float norm() {
		float S = 0;
		for (int i = 0; i < this.nRows; i++) {
			for (int j = 0; j < this.nCols; j++) {
				S += (float) Math.pow(this.get(i, j), 2);
				// System.out.println(S);
			}
		}
		return (float) Math.pow(S, 0.5);
	}

	public MatrixF normalize() {
		float S = 0;
		for (int i = 0; i < this.nRows; i++) {
			for (int j = 0; j < this.nCols; j++) {
				S += Math.pow(this.get(i, j), 2);
			}
		}
		return this.mult((float) Math.pow(S, -0.5));
	}
	public boolean equals(MatrixF other) {
		for (int i = 0; i < this.getRowDimension(); i++) {
			for (int j = 0; j < this.getColDimension(); j++) {
				if(this.get(i, j) != other.get(i, j)) {
					return false;
				}
			}
		}
		return true;
	}

	public MatrixF toDistMatrix() {
		MatrixF mat = this.clone();
		for (int i = 0; i < this.getRowDimension(); i++) {
			for (int j = 0; j < this.getColDimension(); j++) {
				if (i != j && this.get(i, j) == 0f) {
					mat.set(Float.MAX_VALUE, i, j);
				}
			}
		}
		return mat;
	}
	public float[][] toArray(){
		float[][] newArr = new float[this.nRows][this.nRows];
		for (int i = 0; i < newArr.length; i++) {
			for (int j = 0; j < newArr[0].length; j++) {
				newArr[i][j] = this.get(i, j);
			}
		}
		return newArr;
	}
	
}
