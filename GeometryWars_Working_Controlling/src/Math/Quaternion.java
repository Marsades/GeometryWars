package Math;
import processing.core.*;

/***************************************************************************
 * Quaternion class written by BlackAxe / Kolor aka Laurent Schmalen in 1997
 * Translated to Java(with Processing) by RangerMauve in 2012 this class is
 * freeware. you are fully allowed to use this class in non- commercial
 * products. Use in commercial environment is strictly prohibited
 */

final public class Quaternion {
	public final float W, X, Y, Z; // components of a quaternion

	// default constructor
	public Quaternion() {
		W = 1.0f;
		X = 0.0f;
		Y = 0.0f;
		Z = 0.0f;
	}

	// initialized constructor
	public Quaternion(float w, float x, float y, float z) {
		W = w;
		X = x;
		Y = y;
		Z = z;
	}

	/* None of the operations should modify the Quaternion acted on.*/
	/** 
	 * Adds the quaternion q to the quaternion and returns the result. 
	 * @param The quaternion to add
	 * @return The sum of the quaternions.
	 *  */
	public Quaternion add(Quaternion q) {
		return new Quaternion(this.W + q.W, this.X + q.X, this.Y + q.Y, this.Z + q.Z);
	}
	/** 
	 * Subtracts the quaternion q from the quaternion and returns the result. 
	 * @param The quaternion to subtract
	 * @return The difference of the quaternions.
	 *  */
	public Quaternion sub(Quaternion q) {
		return new Quaternion(this.W - q.W, this.X - q.X, this.Y - q.Y, this.Z - q.Z);
	}
	// quaternion multiplication
	public static Quaternion mult(Quaternion q1, Quaternion q2) {
		float w = q1.W * q2.W - (q1.X * q2.X + q1.Y * q2.Y + q1.Z * q2.Z);

		float x = q1.W * q2.X + q2.W * q1.X + q1.Y * q2.Z - q1.Z * q2.Y;
		float y = q1.W * q2.Y + q2.W * q1.Y + q1.Z * q2.X - q1.X * q2.Z;
		float z = q1.W * q2.Z + q2.W * q1.Z + q1.X * q2.Y - q1.Y * q2.X;

		return new Quaternion(w, x, y, z);
	}
	public Quaternion rightMult(Quaternion q2) {
		float w = this.W * q2.W - (this.X * q2.X + this.Y * q2.Y + this.Z * q2.Z);

		float x = this.W * q2.X + q2.W * this.X + this.Y * q2.Z - this.Z * q2.Y;
		float y = this.W * q2.Y + q2.W * this.Y + this.Z * q2.X - this.X * q2.Z;
		float z = this.W * q2.Z + q2.W * this.Z + this.X * q2.Y - this.Y * q2.X;

		return new Quaternion(w, x, y, z);
	}
	
	public Quaternion scalarMult(float a) {
		return new Quaternion(a * this.W, a * this.X, a * this.Y, a * this.Z);
	}

	// conjugates the quaternion
	public Quaternion conjugate() {
		return new Quaternion(this.W, -this.X, -this.Y, -this.Z);
	}

	// inverts the quaternion
	public Quaternion reciprocal() {
		float norm = (float) Math.sqrt(W * W + X * X + Y * Y + Z * Z);
		
		if (norm == 0.0f)
			norm = 1.0f;

		float recip = 1.0f / norm;

		return new Quaternion(W * recip, -X * recip,-Y * recip, -Z * recip);
	}
	public float norm() {
		return (float) Math.sqrt(W * W + X * X + Y * Y + Z * Z);
	}

	// sets to unit quaternion
	public Quaternion normalize() {
		float norme = this.norm();
		if (norme == 0.0) {
			return new Quaternion(1.0f,0,0,0);
		} else {
			float recip = 1.0f / norme;
			return this.copy().scalarMult(recip);
		}
	}
	
	// Returns a copy of this quaternion
	public Quaternion copy() {
		return new Quaternion(W, X, Y, Z);
	}
	/*
	// Rotates towards other quaternion
	public void slerp(Quaternion a, Quaternion b, float t) {
		float omega, cosom, sinom, sclp, sclq;
		int i;

		cosom = a.X * b.X + a.Y * b.Y + a.Z * b.Z + a.W * b.W;

		if ((1.0f + cosom) > Float.MIN_VALUE) {
			if ((1.0f - cosom) > Float.MIN_VALUE) {
				omega = (float) Math.acos(cosom);
				sinom = (float) Math.sin(omega);
				sclp = (float) Math.sin((1.0f - t) * omega) / sinom;
				sclq = (float) Math.sin(t * omega) / sinom;
			} else {
				sclp = 1.0f - t;
				sclq = t;
			}

			X = sclp * a.X + sclq * b.X;
			Y = sclp * a.Y + sclq * b.Y;
			Z = sclp * a.Z + sclq * b.Z;
			W = sclp * a.W + sclq * b.W;
		} else {
			X = -a.Y;
			Y = a.X;
			Z = -a.W;
			W = a.Z;

			sclp = (float) Math.sin((1.0f - t) * Math.PI * 0.5);
			sclq = (float) Math.sin(t * Math.PI * 0.5);

			X = sclp * a.X + sclq * b.X;
			Y = sclp * a.Y + sclq * b.Y;
			Z = sclp * a.Z + sclq * b.Z;
		}
	}
	*/

	// Example of rotating PVector about a directional PVector
	public static PVector rotate(PVector v, PVector r, float a) {
		Quaternion Q1 = new Quaternion(0, v.x, v.y, v.z);
		Quaternion Q2 = new Quaternion((float) Math.cos(a / 2), r.x * (float) Math.sin(a / 2),
				r.y * (float) Math.sin(a / 2), r.z * (float) Math.sin(a / 2));
		System.out.println("Q1, Quaternion to rotate: " + Q1.toString());
		System.out.println("Q2, Rotation Quaternion: " + Q2.toString());
		Quaternion Q3 = Q2.rightMult(Q1).rightMult(Q2.conjugate());
		System.out.println("Q3, Transformed Quaternion: " + Q3.toString());
		return new PVector(Q3.X, Q3.Y, Q3.Z);
	}
	// The "star-operator" takes a vector and makes it such that it takes the cross product with what it operator on. 
	public static Quaternion star(PVector w) {
		return new Quaternion(0, w.x, w.y, w.z);
	}	
	
	public PMatrix3D toRotationMatrix() {
		return new PMatrix3D(
				1 - 2 * (Y * Y + Z * Z), 2 * (X * Y - W * Z), 2 * (X * Z + W * Y), 0, 
				2 * (X * Y + W * Z), 1 - 2 * (X * X + Z * Z), 2 * (Y * Z - W * X), 0,
				2 * (X * Z - W * Y), 2 * (Y * Z + W * X), 1 - 2 * (X * X + Y * Y), 0, 
				0, 0, 0, 1);
	}
	
	public String toString() {
		return new String("[W,X,Y,Z] = [" + this.W + ", " + this.X + ", " + this.Y + ", " + this.Z + "]");
	}

	/*
	// Rotates towards other quaternion
	public void slerp(Quaternion a, Quaternion b, float t) {
		float omega, cosom, sinom, sclp, sclq;
		int i;
	
		cosom = a.X * b.X + a.Y * b.Y + a.Z * b.Z + a.W * b.W;
	
		if ((1.0f + cosom) > Float.MIN_VALUE) {
			if ((1.0f - cosom) > Float.MIN_VALUE) {
				omega = (float) Math.acos(cosom);
				sinom = (float) Math.sin(omega);
				sclp = (float) Math.sin((1.0f - t) * omega) / sinom;
				sclq = (float) Math.sin(t * omega) / sinom;
			} else {
				sclp = 1.0f - t;
				sclq = t;
			}
	
			X = sclp * a.X + sclq * b.X;
			Y = sclp * a.Y + sclq * b.Y;
			Z = sclp * a.Z + sclq * b.Z;
			W = sclp * a.W + sclq * b.W;
		} else {
			X = -a.Y;
			Y = a.X;
			Z = -a.W;
			W = a.Z;
	
			sclp = (float) Math.sin((1.0f - t) * Math.PI * 0.5);
			sclq = (float) Math.sin(t * Math.PI * 0.5);
	
			X = sclp * a.X + sclq * b.X;
			Y = sclp * a.Y + sclq * b.Y;
			Z = sclp * a.Z + sclq * b.Z;
		}
	}
	*/
	
	/*
	// Rotates towards other quaternion
	public void slerp(Quaternion a, Quaternion b, float t) {
		float omega, cosom, sinom, sclp, sclq;
		int i;
	
		cosom = a.X * b.X + a.Y * b.Y + a.Z * b.Z + a.W * b.W;
	
		if ((1.0f + cosom) > Float.MIN_VALUE) {
			if ((1.0f - cosom) > Float.MIN_VALUE) {
				omega = (float) Math.acos(cosom);
				sinom = (float) Math.sin(omega);
				sclp = (float) Math.sin((1.0f - t) * omega) / sinom;
				sclq = (float) Math.sin(t * omega) / sinom;
			} else {
				sclp = 1.0f - t;
				sclq = t;
			}
	
			X = sclp * a.X + sclq * b.X;
			Y = sclp * a.Y + sclq * b.Y;
			Z = sclp * a.Z + sclq * b.Z;
			W = sclp * a.W + sclq * b.W;
		} else {
			X = -a.Y;
			Y = a.X;
			Z = -a.W;
			W = a.Z;
	
			sclp = (float) Math.sin((1.0f - t) * Math.PI * 0.5);
			sclq = (float) Math.sin(t * Math.PI * 0.5);
	
			X = sclp * a.X + sclq * b.X;
			Y = sclp * a.Y + sclq * b.Y;
			Z = sclp * a.Z + sclq * b.Z;
		}
	}
	*/
	
	// Takes the log of a quaternion
	public Quaternion log() {
		float Length;
	
		Length = (float) Math.sqrt(this.X * this.X + this.Y * Y + this.Z * Z);
		Length = (float) Math.atan(Length / W);
	
		return new Quaternion(0.0f, X*Length, Y*Length, Z*Length);
	}

	public Quaternion exp() {
		float Mul;
		float Length = (float) Math.sqrt(X * X + Y * Y + Z * Z);
	
		if (Length > 1.0e-4)
			Mul = (float) Math.sin(Length) / Length;
		else
			Mul = 1.0f;
	
		return new Quaternion((float) Math.cos(Length), X*Mul, Y*Mul, Z*Mul);
	}

	public Quaternion pow(float n) {
		return this.log().scalarMult(n).exp();
	}
	
	public static Quaternion fromAxis(float Angle, PVector axis) {
		return Quaternion.fromAxis(Angle, axis.x, axis.y, axis.z);
	}

	// Makes quaternion from axis
	public static Quaternion fromAxis(float Angle, float x, float y, float z) {
		float omega, s, c;
	
		s = (float) Math.sqrt(x * x + y * y + z * z);
	
		if (Math.abs(s) > Float.MIN_VALUE) {
			c = 1.0f / s;
	
			x = x*c;
			y = y*c;
			z = z*c;
	
			omega = -0.5f * Angle;
			s = (float) Math.sin(omega);
	
			return (new Quaternion((float) Math.cos(omega),s*x,s*y,s*z)).normalize();
		} else {
			return new Quaternion(1f,0,0,0);
		}
	}
}
