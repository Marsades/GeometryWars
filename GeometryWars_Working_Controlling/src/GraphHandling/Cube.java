package GraphHandling;

import processing.core.*;

public class Cube {
	public Face[] faces = new Face[6];

	static void main(String... args) {
		
	}
	Cube() {

		PVector vec1 = new PVector(1, 1, 0);
		PVector vec2 = new PVector(1, -1, 0);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < i; j++) {
				vec1 = rotateY(vec1, -PConstants.HALF_PI);
				vec1 = rotateZ(vec1, -PConstants.HALF_PI);

				vec2 = rotateY(vec2, -PConstants.HALF_PI);
				vec2 = rotateZ(vec2, -PConstants.HALF_PI);
			}

			Vertex vert1 = new Vertex(vec1);
			Vertex vert2 = new Vertex(vec2);
			Face fac1 = new Face(vert1, vert2);
			faces[i] = fac1;
			vert1.leftFace = faces[i];
			vert2.leftFace = faces[i];
		}
	}

	private PVector rotateX(PVector in, float theta) {
		return new PVector(in.x, (float) (Math.cos(theta) * in.y - Math.sin(theta) * in.z),
				(float) (Math.sin(theta) * in.y + Math.cos(theta) * in.z));
	}

	private PVector rotateY(PVector in, float theta) {
		return new PVector((float) (Math.cos(theta) * in.x + Math.sin(theta) * in.y), in.y,
				(float) (-Math.sin(theta) * in.x + Math.cos(theta) * in.z));
	}

	private PVector rotateZ(PVector in, float theta) {
		return new PVector((float) (Math.cos(theta) * in.x - Math.sin(theta) * in.y),
				(float) (Math.sin(theta) * in.x + Math.cos(theta) * in.y), in.z);
	}
	// private PVector matMult(Matrix mat, PVector in) {
	// return new PVector(
	// (float) (Math.cos(theta)*in.x + Math.sin(theta)*in.z),
	// in.y,
	// (float) (-Math.sin(theta)*in.x + Math.cos(theta)*in.z) );
	// }
}
