package com.magenta.engine;

import org.joml.Vector3f;

public class Camera {
	private int width, height;

	// Camera movement vectors
	private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
	private Vector3f rotation = new Vector3f((float) Math.TAU / 4, 0.0f, 0.0f);

	// Camera config
	private final float fovDeg, sensitivity;
	private final float nearPlane = 0.01f, farPlane = 100.0f;

	public Camera(Window window, float fovDeg, float sensitivity) {
		this.fovDeg = fovDeg;
		this.sensitivity = sensitivity;

		width = window.getWidth();
		height = window.getHeight();
	}

	public void setRotation(float x, float y, float z) {
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
	}

	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}

	public void moveRotation(double xpos, double ypos) {
		// rotation.x -= xpos * sensitivity;
		rotation.x += xpos * sensitivity;
		rotation.y += ypos * sensitivity;
		// rotation.z += offsetZ;

		// Avoid 360º spin (Y only duurh)
		rotation.y = Math.max((float) (-Math.TAU / 4),
			Math.min((float) (Math.TAU / 4), rotation.y)
		);
	}
	
	public void movePosition(float offsetX, float offsetY, float offsetZ, float speed) {
		float angle = (float)(rotation.x - Math.atan2(offsetZ, offsetX) + (Math.TAU / 4));
		if(offsetX != 0.0f || offsetZ != 0.0f) {
			position.x += (float) Math.cos(angle) * speed * 0.1f;
			position.z += (float) Math.sin(angle) * speed * 0.1f;
		}

		position.y += offsetY * 0.1f;
	}

	public float getNearPlane() {
		return nearPlane;
	}

	public float getFarPlane() {
		return farPlane;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public float getFovDeg() {
		return fovDeg;
	}

	public float getSensitivity() {
		return sensitivity;
	}
}