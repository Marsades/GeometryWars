package Math;
import processing.core.PVector;
import shortestPathImplement.PathMatrixPair;
import java.io.*;

public class MatrixTest {
	
	static int res = 2;
	static MatrixF A = MatrixF.circulant(3).compose(MatrixF.identity(3).KroneckerProduct(MatrixF.circulant(3))); // Rotates around corners
	static MatrixF B = new MatrixF(new float[][] {//Rotates 180 about an, for now unknown, axis
		{0,0,0,0,0,0,0,0,1,0,0,0},
		{0,0,0,0,0,0,1,0,0,0,0,0},
		{0,0,0,0,0,0,0,1,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,1,0},
		{0,0,0,0,0,0,0,0,0,0,0,1},
		{0,0,0,0,0,0,0,0,0,1,0,0},
		{0,1,0,0,0,0,0,0,0,0,0,0},
		{0,0,1,0,0,0,0,0,0,0,0,0},
		{1,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,1,0,0,0,0,0,0},
		{0,0,0,1,0,0,0,0,0,0,0,0},
		{0,0,0,0,1,0,0,0,0,0,0,0}});
	static MatrixF K = MatrixF.identity(2).compose(MatrixF.circulant(3).Power(2)).compose(MatrixF.identity(7));
	
	static MatrixF C = new MatrixF(new float[][] {
		{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		{ 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 }
});
	static float d = 1.414f;
	
//	static MatrixF FaceSimple = new MatrixF(new float[][] {
//		{0,1,d,1},
//		{1,0,1,d},
//		{d,1,0,1},
//		{1,d,1,0}});
	
	static MatrixF DisjointFace = (new MatrixF(4,4)).toDistMatrix();
	
	/* Distance matrix structure:
	 *  ------------------------
	 * | Intern Edge			|
	 * | 			 Intern Edge| = A
	 *  ------------------------
	 * 
	 *  -----
	 * | A	 |
	 * |   A | = Internal Face distances = B
	 *  -----
	 * 
	 *   -----
	 *  | B   |
	 *  |   B | = Internal Cube distances = C
	 *   -----
	 * 
	*/
	
	public static void main(String[] args) {
//		System.out.println("Test:");
//		PVector t = new PVector(1,0,0);
//		System.out.println("t-vector is: " + t.toString());
//		PVector axis = new PVector(0,0,1);
//		System.out.println("The axis is:" + axis.toString());
//		float angle = (float) Math.PI/2;
//		Quaternion A = new Quaternion();
//		Quaternion B = new Quaternion(); 
//		Matrix A = Matrix.rotateY(-Math.PI/2);
//		Matrix B = Matrix.rotateZ(-Math.PI/2);
//		Matrix T = Matrix.permuteRighthanded();
//		t = Quaternion.rotate(t, axis, angle);
//		Matrix T = Matrix.multiply(A,B);
//		int n = 2;
//		T = Matrix.power(T, n);
//		System.out.println("Transformation matrix is: \n" + T.toString());
//		t = T.multiply(t);
//		System.out.println("After transformation t is: " + t.toString());
//		float f = (float) (Float.MIN_NORMAL * Math.pow(2, 102));
//		System.out.println("Value of f: " + f);
//		int k = (int) (1f - f);
//		System.out.println("Value of k: " + k);
		
//		Matrix A = Matrix.circulant(3);
//		A.print();
//		Matrix B = A.compose(A);
//		B.print();
//		Matrix C = B.compose(B);
//		C.print();
		
		
		float[] seq = new float[res*12];
		for (int i = 0; i < seq.length; i++) {
			seq[i] = i;
		}
		MatrixF V = new MatrixF(seq, seq.length, 1);
		
		MatrixF FaceSuperSimple = new MatrixF(new float[][] {
			{0, 
				1,
				(float) Math.sqrt(5f),
				(float) Math.sqrt(8f),
				(float) Math.sqrt(10f),
				3, 
				(float) Math.sqrt(5f),
				(float) Math.sqrt(2f),
			}});
		MatrixF FaceSimple = getSimpleFace(FaceSuperSimple);

		A = MatrixF.identity(res).KroneckerProduct(A);
		B = MatrixF.identity(res).KroneckerProduct(B);
		C = MatrixF.identity(res).KroneckerProduct(C);
		K = MatrixF.identity(res).KroneckerProduct(K);
		
		MatrixF Face = C.dag().MatMult(FaceSimple).MatMult(C);
		
//		A.print();
//		B.print();
//		C.print();
		
		MatrixF Dist1 = new MatrixF(res*12, res*12);
		MatrixF Dist2 = new MatrixF(res*12, res*12);
		
		float d = (float) Math.sqrt(2);
		
		for (int n = 0; n < 3; n++) {
			for (int m = 0; m < 2; m++) {
				System.out.println("Face " + (m + 2*n));
				
				MatrixF D = (B.Power(m)).MatMult(A.Power(n));
				System.out.println("D matrix:");
				D.print();
				
				MatrixF L = D.dag().MatMult(Face).MatMult(D);
				System.out.println("L matrix:");
				L.print();
				Dist2 = Dist2.add(L);
				
				MatrixF E = C.MatMult(D).MatMult(V);
				E.dag().print();
//				Dist1.set(Dist1.get((int) E.get(0, 0), (int) E.get(1, 0)) + 1f, (int) E.get(0, 0), (int) E.get(1, 0));
//				Dist1.set(Dist1.get((int) E.get(1, 0), (int) E.get(0, 0)) + 1f, (int) E.get(1, 0), (int) E.get(0, 0));
//				Dist1.set(Dist1.get((int) E.get(2, 0), (int) E.get(3, 0)) + 1f, (int) E.get(2, 0), (int) E.get(3, 0));
//				Dist1.set(Dist1.get((int) E.get(3, 0), (int) E.get(2, 0)) + 1f, (int) E.get(3, 0), (int) E.get(2, 0));
//				
//				Dist1.set(1f, (int) E.get(0, 0), (int) E.get(2, 0));
//				Dist1.set(1f, (int) E.get(2, 0), (int) E.get(0, 0));
//				Dist1.set(1f, (int) E.get(1, 0), (int) E.get(3, 0));
//				Dist1.set(1f, (int) E.get(3, 0), (int) E.get(1, 0));
//				
//				Dist1.set(1.414f, (int) E.get(0, 0), (int) E.get(3, 0));
//				Dist1.set(1.414f, (int) E.get(3, 0), (int) E.get(0, 0));
//				Dist1.set(1.414f, (int) E.get(1, 0), (int) E.get(2, 0));
//				Dist1.set(1.414f, (int) E.get(2, 0), (int) E.get(1, 0));
				
//				for (int i = 0; i < E.getRowDimension() - 1; i++) {
//					for (int j = i + 1; j < E.getRowDimension(); j++) {
//						Dist1.set(1f, (int) E.get(i, 0), (int) E.get(j, 0));
//						Dist1.set(1f, (int) E.get(j, 0), (int) E.get(i, 0));
//					}
//				}
//				Matrix front = faceToFrontMatrix(m+2*n);
//				front.MatMult(V).dag().print();
			}
		}
		
		System.out.println("Dist1 matrix:");
		Dist1.print();
		System.out.println("Dist2 matrix:");
		Dist2.print();
		// Conjoining two cube faces
		MatrixF toFront = faceToFrontMatrix(1);
		System.out.println("Dist2 with face 1 matrix:");
		MatrixF E = toFront.MatMult(Dist2).MatMult(toFront.dag());
		E.print();
		MatrixF T = E.getSubMatrix(0, 3, 0, 3);
		MatrixF F = E.getSubMatrix(4, 11, 4, 11);
		
		MatrixF N = new MatrixF(res * (24 - 4), res * (24 - 4));
		N = N.setSubMatrix(0, 0, E);
		N = N.setSubMatrix(8, 8, E);
		N.print();
		N = N.toDistMatrix();
		N.print();
		N = N.setSubMatrix(8, 8, DisjointFace);
		N.print();
		PathMatrixPair P = new PathMatrixPair(res*20);
		float[][] domino = N.toArray();
		P.setDistances(domino);
		
		PathMatrixPair newP = AllPairShortestPath( P );
		printMatrix("Domino distances: ", newP.distances);
		
		FileWriter out;
		try {
			out = new FileWriter("Cuboctahedron_Dist_Matrix.txt", true);
//			out.write(Dist2.toString());
//			out.write(Boolean.toString(isCorrectDistMat(Dist2)));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Face blowup test: ");
		// Face blowup test:
		
		FaceSimple = MatrixF.identity(res).KroneckerProduct(FaceSimple);
		FaceSimple.print();
		Face.print();
	}
	
	private static MatrixF getSimpleFace(MatrixF dist) {
		MatrixF S = new MatrixF(4*res, 4*res);
		for (int i = 0; i < dist.getColDimension(); i++) {
			S = S.add(MatrixF.identity(res).KroneckerProduct(MatrixF.circulant(4).Power(i)).mult(dist.get(0, i)));
		}
		return S;
	}

	public static MatrixF faceToFrontMatrix(int face) {
		int n = face % 3;
		int m = face % 2;
		
		MatrixF D = B.Power(m).MatMult(A.Power(n));
		
		return K.MatMult(D);
	}
	public static MatrixF faceToBackMatrix(int face) {
		return MatrixF.reverse(12).MatMult(faceToFrontMatrix(face));
	}
	public MatrixF conjoin(int faceA, int faceB, MatrixF A, MatrixF B) {
		return MatrixF.identity(12);
	}
	public static boolean isCorrectDistMat(MatrixF dist) {
		return (A.MatMult(dist).equals(dist.MatMult(A)) && B.MatMult(dist).equals(dist.MatMult(B)));
	}

	public static PathMatrixPair AllPairShortestPath(PathMatrixPair W) {
		// Initialization;
		int N = W.length;
//		printMatrix("Input distances: ", W.distances);
//		printMatrix("Input routes: ", W.route);
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
//		printMatrix("Output distances: ", output.distances);
//		printMatrix("Output routes: ", output.route);
		return output;
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
					System.out.print(String.format("%5.2f ", cur));
				}
			}
			System.out.println();
		}
	}
}
