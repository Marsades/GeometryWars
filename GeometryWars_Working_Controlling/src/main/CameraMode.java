package main;

public enum CameraMode {

	freeView, playerView, lockedView;
	CameraMode next() {
		if(this == freeView) {
			return playerView;
		} else if(this == playerView) {
			return lockedView;
		} else {
			return freeView;
		}
	}
}
