package Math;
import processing.core.*;

public class Matrix3 {

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
		
		private static final float[] IDENTITY = {1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };

		Matrix3() {
			this(new float[] {1f,0f,0f,0f,1f,0f,0f,0f,1f});
		}

		Matrix3(float xx, float xy, float xz, float yx, float yy, float yz, float zx, float zy, float zz) {
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

		Matrix3(float[] values) {
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

		Matrix3(float[][] values) {
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

		public static Matrix3 rotateX(double theta) {
			return new Matrix3(
					1f, 0f, 0f,
					0f, (float) Math.cos(theta), (float) -Math.sin(theta),
					0f, (float) Math.sin(theta), (float) +Math.cos(theta));
		}
		public static Matrix3 rotateZ(double theta) {
			return new Matrix3(
					(float) Math.cos(theta), (float) -Math.sin(theta), 0f,
					(float) Math.sin(theta), (float) Math.cos(theta), 0f,
					0f, 0f, 1f);
		}
		public static Matrix3 rotateY(double theta) {
			return new Matrix3(
					(float) Math.cos(theta), 0f, + (float) Math.sin(theta),
					0f, 1f, 0f,
					(float) -Math.sin(theta), 0f, + (float) Math.cos(theta));
		}
		public static Matrix3 permuteRighthanded() {
			float[] f = {0,1f,0, 0,0,1f, 1f,0,0};
			return new Matrix3(f);
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
		public String toString() {
			String S = new String();
			for (int i = 0; i < this.array.length; i++) {
				S += array[i] ;
				S += ", ";
				if((i%3) ==2) {
					S += "\n";
				}
			}
			return S;
		}
		public PMatrix3D toPMatrix() {
			return new PMatrix3D(
					this.xx,this.xy,this.xz,0,
					this.yx,this.yy,this.yz,0,
					this.zx,this.zy,this.zz,0,
					0,0,0,1
					);
		}
		// Multiplying two matrices not in-place
		public static Matrix3 multiply(Matrix3 A, Matrix3 B) {
			float[] out = new float[9];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					for (int k = 0; k < 3; k++) {
						out[i+3*j] += A.array[i + 3*k] * B.array[k + 3*j];
					} 
				}
			}
			return new Matrix3(out);
		}

		public static Matrix3 power(Matrix3 M, int n){
			if (n<0) throw new Error("The integer cannot be negative.");
			else if (n == 0) {
//				float[][] f = { { 1f, 0f, 0f }, { 0f, 1f, 0f }, { 0f, 0f, 1f } };
				return new Matrix3(IDENTITY);
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
