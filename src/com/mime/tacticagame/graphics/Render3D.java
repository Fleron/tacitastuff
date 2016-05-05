package com.mime.tacticagame.graphics;

import com.mime.tacticagame.Display;
import com.mime.tacticagame.Game;
import com.mime.tacticagame.input.Controller;

public class Render3D extends Render {

	public double[] zBuffer;
	private double renderDistance = 5000.0;
	private double forward, right, sine, cosine, up;

	public Render3D(int width, int height) {
		super(width, height);
		zBuffer = new double[width * height];

	}

	public void floor(Game game) {
		double floorPosition = 8.0;
		double ceilingPosition = 800.0;
		forward = game.controls.z;
		right = game.controls.x;
		up = game.controls.y;
		double headBobbing = Math.sin(game.time / 6.0) * 0.5;

		double rotation = game.controls.rotation;
		sine = Math.sin(rotation);
		cosine = Math.cos(rotation);

		for (int y = 0; y < height; y++) {
			double ceiling = (y - height / 2.0) / height;

			double z = (floorPosition + up) / ceiling;
			if (Controller.walking) {
				z = (floorPosition + up + headBobbing) / ceiling;
			}
			if (ceiling < 0) {
				z = (ceilingPosition - up) / -ceiling;
				if (Controller.walking) {
					z = (ceilingPosition - up - headBobbing) / -ceiling;
				}
			}

			if (z < 200) {
				for (int x = 0; x < width; x++) {
					double xDepth = (x - width / 2.0) / height;
					xDepth *= z;
					double xx = xDepth * cosine + z * sine;
					double yy = z * cosine - xDepth * sine;
					int xPix = (int) (xx + right);
					int yPix = (int) (yy + forward);
					zBuffer[x + y * width] = z;
					pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 8];
					renderDistanceLimiter(x + y * width);
				}
			}
		}
	}

	public void renderWall(double xLeft, double xRight, double zDistance, double yHeight) {
		double xcLeft = (xLeft - right) * 2;
		double zcLeft = (zDistance - forward) * 2;

		double rotLeftX = xcLeft * cosine - zcLeft * sine;
		double yCornerTL = (-yHeight - up) * 2;
		double yCornerBL = ((0.5 - yHeight) - up) * 2;
		double rotLeftZ = zcLeft * cosine + xcLeft * sine;

		double xcRight = (xRight - right) * 2;
		double zcRight = (zDistance - forward) * 2;

		double rotRightX = xcRight * cosine - zcRight * sine;
		double yCornerTR = (-yHeight - up) * 2;
		double yCornerBR = ((0.5 - yHeight) - up) * 2;
		double rotRightZ = zcRight * cosine + xcRight * sine;

		double xPixelLeft = (rotLeftX / rotLeftZ * height + width / 2);
		double xPixelRight = (rotRightX / rotRightZ * height + width / 2);

		if (xPixelLeft >= xPixelRight) {
			return;
		}

		int xPixelLeftInt = (int) xPixelLeft;
		int xPixelRightInt = (int) xPixelRight;

		if (xPixelLeftInt < 0) {
			xPixelLeftInt = 0;
		}

		if (xPixelRightInt >= width) {
			xPixelRightInt = width;
		}

		double yPixelLeftTop = (int) (yCornerTL / rotLeftZ * height + height / 2);
		double yPixelLeftBottom = (int) (yCornerBL / rotLeftZ * height + height / 2);
		double yPixelRightTop = (int) (yCornerTR / rotRightZ * height + height / 2);
		double yPixelRightBottom = (int) (yCornerBR / rotRightZ * height + height / 2);

		for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
			double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);

			double yPixelTop = yPixelLeftTop + (yPixelLeftTop - yPixelRightTop) * pixelRotation;
			double yPixelBottom = yPixelLeftBottom + (yPixelLeftBottom - yPixelRightBottom) * pixelRotation;
		}

	}

	public void renderDistanceLimiter(int i) {
		int color = pixels[i];
		int brightness = (int) (renderDistance / (zBuffer[i]));

		if (brightness < 0) {
			brightness = 0;
		}

		if (brightness > 255) {
			brightness = 255;
		}

		int r = (color >> 16) & 0xff;
		int g = (color >> 8) & 0xff;
		int b = (color) & 0xff;

		r = r * brightness / 255;
		g = g * brightness / 255;
		b = b * brightness / 255;

		pixels[i] = (r << 16 | g << 8 | b);
	}
}
