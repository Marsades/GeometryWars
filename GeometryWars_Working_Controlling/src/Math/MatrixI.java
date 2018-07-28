package Math;

public class MatrixI {
	boolean transpose = false;
	private boolean square = false;
	private int nRows; // Height
	private int nCols; // Width
	final int[] arr;

	public MatrixI(int N, int M) {
		this.nRows = N;
		this.nCols = M;
		arr = new int[N * M];
	}

	public MatrixI(int[] in, int nRows, int nCols) {
		this.arr = in.clone();
		this.nRows = nRows;
		this.nCols = nCols;
	}

	public MatrixI(int[][] in) {
		this.nRows = in.length;
		this.nCols = in[0].length;
		int[] newArr = new int[nRows * nCols];
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				newArr[i + nRows * j] = in[i][j];
			}
		}
		this.arr = newArr;
	}

	public MatrixI(int[] arr, int nRows, int nCols, boolean transpose) {
		this.arr = arr.clone();
		this.nRows = nRows;
		this.nCols = nCols;
		this.transpose = transpose;
	}

	public MatrixI dag() {
		MatrixI mat = this.clone();
		mat.transpose = !mat.transpose;
		mat.nCols = this.nRows;
		mat.nRows = this.nCols;
		return mat;
	}

	public int get(int i, int j) {
		if (transpose) {
			return arr[j + nCols * i];
		} else {
			return arr[i + nRows * j];
		}
	}

	public void set(int a, int row, int col) {
		arr[row + nRows * col] = a;
	}

	public static MatrixI diagonal(int n, int[] arr) {
		int[] newArr = new int[n * n];
		for (int i = 0; i < n; i++) {
			newArr[i + i * n] = arr[i];
		}
		MatrixI mat = new MatrixI(newArr, n, n);
		return mat;
	}

	public static MatrixI identity(int n) {
		int[] newArr = new int[n * n];
		for (int i = 0; i < n; i++) {
			newArr[i + i * n] = 1;
		}
		MatrixI mat = new MatrixI(newArr, n, n);
		return mat;
	}

	public static MatrixI zero(int n) {
		int[] newArr = new int[n * n];
		MatrixI mat = new MatrixI(newArr, n, n);
		return mat;
	}

	public static MatrixI circulant(int n) {
		int[] newArr = new int[n * n];
		for (int i = 0; i < n - 1; i++) {
			newArr[i + i * n + 1] = 1;
		}
		newArr[0 + n * (n - 1)] = 1;
		MatrixI mat = new MatrixI(newArr, n, n);
		return mat;
	}

	public MatrixI getSubMatrix(int x1, int x2, int y1, int y2) {
		MatrixI mat = new MatrixI(x2 - x1 + 1, y2 - y1 + 1);
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				mat.set(this.get(x, y), x - x1, y - y1);
			}
		}
		return mat;
	}

	public MatrixI setSubMatrix(int x1, int y1, MatrixI A) {
		MatrixI mat = this.clone();
		for (int x = x1; x < x1 + A.getRowDimension(); x++) {
			for (int y = y1; y < y1 + A.getColDimension(); y++) {
				mat.set(A.get(x - x1, y - y1), x, y);
			}
		}
		return mat;
	}

	public static MatrixI reverse(int n) {
		int[] newArr = new int[n * n];
		for (int i = 0; i < n; i++) {
			newArr[i + n * (n - 1 - i)] = 1;
		}
		MatrixI mat = new MatrixI(newArr, n, n);
		return mat;
	}

	public MatrixI compose(MatrixI A) {
		int newRows = this.nRows + A.nRows;
		int newCols = this.nCols + A.nCols;
		MatrixI mat = new MatrixI(newRows, newCols);
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

	public MatrixI add(MatrixI A) {
		MatrixI mat = this.clone();
		for (int i = 0; i < A.nRows; i++) {
			for (int j = 0; j < A.nCols; j++) {
				mat.set(this.get(i, j) + A.get(i, j), i, j);
			}
		}
		return mat;
	}

	public static MatrixI add(MatrixI A, MatrixI B) {
		MatrixI mat = A.clone();
		for (int i = 0; i < A.nRows; i++) {
			for (int j = 0; j < A.nCols; j++) {
				mat.set(A.get(i, j) + B.get(i, j), i, j);
			}
		}
		return mat;
	}

	public MatrixI mult(int a) {
		MatrixI mat = this.clone();
		for (int i = 0; i < this.nRows; i++) {
			for (int j = 0; j < this.nCols; j++) {
				mat.set(a * this.get(i, j), i, j);
			}
		}
		return mat;
	}

	public static MatrixI mult(MatrixI M, int a) {
		MatrixI mat = (MatrixI) M.clone();
		for (int i = 0; i < M.nRows; i++) {
			for (int j = 0; j < M.nCols; j++) {
				mat.set(a * M.get(i, j), i, j);
			}
		}
		return mat;
	}

	public static MatrixI MatMult(MatrixI... matrixs) {
		MatrixI mat = MatrixI.identity(matrixs[0].nCols);
		for (int i = 0; i < matrixs.length; i++) {
			mat.MatMult(matrixs[i]);
		}
		return mat;
	}

	public MatrixI MatMult(MatrixI A) {
		if (this.nCols != A.nRows) {
			System.out.print("Dimensions doesn't match!");
		}
		MatrixI mat = new MatrixI(this.nRows, A.nCols);
		for (int i = 0; i < mat.nRows; i++) {
			for (int j = 0; j < mat.nCols; j++) {
				int S = 0;
				for (int k = 0; k < A.nRows; k++) {
					S += this.get(i, k) * A.get(k, j);
				}
				mat.set(S, i, j);
			}
		}
		return mat;
	}

	public MatrixI Power(int n) {
		MatrixI mat = MatrixI.identity(this.nCols);
		for (int i = 0; i < n; i++) {
			mat = mat.MatMult(this);
		}
		return mat;
	}

	public MatrixI KroneckerProduct(MatrixI B) {
		MatrixI M = new MatrixI(this.nRows * B.nRows, this.nCols * B.nCols);
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

	public MatrixI clone() {
		return new MatrixI(this.arr, this.nRows, this.nCols, this.transpose);
	}

	public String toString() {
		String S = "";
		for (int i = 0; i < this.nRows; i++) {
			S += "[";
			for (int j = 0; j < this.nCols; j++) {
				int d = this.get(i, j);
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

	public int highestEigenvalue() {
		int[] arr = new int[this.nRows];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (2 * Math.random() - 1);
		}
		MatrixI v = new MatrixI(arr, nRows, 1);
		for (int i = 0; i < 10; i++) {
			v = v.normalize();
			v = this.MatMult(v);
		}

		return v.norm();
	}

	public MatrixI highestEigenvector() {
		int[] arr = new int[this.nRows];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (2 * Math.random() - 1);
		}
		MatrixI v = new MatrixI(arr, nRows, 1);
		for (int i = 0; i < 20; i++) {
			v = v.normalize();
			// System.out.println(v.trans());
			v = this.MatMult(v);
		}
		return v.normalize();
	}

	public int norm() {
		int S = 0;
		for (int i = 0; i < this.nRows; i++) {
			for (int j = 0; j < this.nCols; j++) {
				S += (int) Math.pow(this.get(i, j), 2);
				// System.out.println(S);
			}
		}
		return (int) Math.pow(S, 0.5);
	}

	public MatrixI normalize() {
		int S = 0;
		for (int i = 0; i < this.nRows; i++) {
			for (int j = 0; j < this.nCols; j++) {
				S += Math.pow(this.get(i, j), 2);
			}
		}
		return this.mult((int) Math.pow(S, -0.5));
	}

	public MatrixI toDistMatrix() {
		MatrixI mat = this.clone();
		for (int i = 0; i < this.getRowDimension(); i++) {
			for (int j = 0; j < this.getColDimension(); j++) {
				if (i != j && this.get(i, j) == 0f) {
					mat.set(Integer.MAX_VALUE, i, j);
				}
			}
		}
		return mat;
	}
	public int[][] toArray(){
		int[][] newArr = new int[this.nRows][this.nRows];
		for (int i = 0; i < newArr.length; i++) {
			for (int j = 0; j < newArr[0].length; j++) {
				newArr[i][j] = this.get(i, j);
			}
		}
		return newArr;
	}
}
