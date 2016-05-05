package com.mime.tacticagame.input;

public class Controller {
	public double x, z, y, rotation, xa, za, rotationa;
	public static boolean turnLeft = false;
	public static boolean turnRight = false;
	public static boolean walking = false;

	public void tick(boolean forward, boolean back, boolean strafeLeft, boolean strafeRight, boolean jump, boolean crouch, boolean sprint) {
		double rotationSpeed = 0.025;
		double jumpHeight = 0.5;
		double crouchHeight = 0.3;
		double walkingSpeed = 0.5;
		double xMove = 0;
		double zMove = 0;

		if (forward) {
			zMove++;
			walking = true;
		}

		if (back) {
			zMove--;
			walking = true;
		}

		if (strafeLeft) {
			xMove--;
			walking = true;
		}

		if (strafeRight) {
			xMove++;
			walking = true;
		}

		if (turnLeft) {
			rotation -= rotationSpeed;
		}

		if (turnRight) {
			rotation += rotationSpeed;
		}

		if (jump) {
			y += jumpHeight;
			sprint = false;
			walking = false;
		}

		if (crouch) {
			y -= crouchHeight;
			sprint = false;
			walkingSpeed = 0.25;
		}

		if (sprint) {
			walkingSpeed = 1;
		}

		xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkingSpeed;
		za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkingSpeed;

		x += xa;
		y *= 0.9;
		z += za;
		xa *= 0.1;
		za *= 0.1;
		rotation += rotationa;
		rotationa *= 0.5;

	}
}
