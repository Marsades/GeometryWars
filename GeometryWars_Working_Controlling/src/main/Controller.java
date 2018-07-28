package main;
import processing.core.*;
import units.Unit;

public class Controller {
	PVector curPos;
	PVector moveTo;
	float eAngle;
	float eDist;
	boolean moving;

	Controller(PVector pos) {
		moveTo = pos;
		eAngle = 0.08f;
		eDist = 2;
		boolean moving = false;
	}

	void setMovePosition(PVector point) {
		moveTo = point;
		moving = true;
	}

	int getOrder(Unit P) {
		if (!moving) {
			return 0;
		}
		curPos = P.getGlobalPos();
		PVector movingPlaneNormal = curPos.cross(moveTo).normalize();
		PVector surfaceNormal = P.getNormal();
		PVector moveDir = surfaceNormal.cross(movingPlaneNormal);
		PVector curDir = P.getGlobalDirection().normalize();
		PVector cross = curDir.cross(moveDir);
		float d = cross.dot(surfaceNormal);
		float k = PVector.sub(curPos, moveTo).magSq();
		if (k < eDist) {
			moving = false;
			return 0;
		} else if (d < -eAngle) {
			return 1; // turnLeft
		} else if (d > eAngle) {
			return -1; // turnRight
		} else {
			return 2;
		}
	}
}