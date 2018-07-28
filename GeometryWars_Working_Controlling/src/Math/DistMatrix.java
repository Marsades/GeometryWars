package Math;

public class DistMatrix extends MatrixF {

	public DistMatrix(int N, int M) {
		super(N, M);
		for (int i = 0; i < this.getRowDimension(); i++) {
			for (int j = 0; j < this.getColDimension(); j++) {
				if (i != j) {
					this.set(Float.MAX_VALUE, i, j);
				}
			}
		}
	}

}
