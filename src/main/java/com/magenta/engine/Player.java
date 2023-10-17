package com.magenta.engine;

import org.joml.Vector3f;
import com.magenta.game.Entity;
import com.magenta.game.World;

public class Player extends Entity {
	private final Window window;

	// Physics
	private final float WALKING_SPEED = 4.317f;
	private final float SPRINTING_SPEED = 7.0f;

	// Camera movement vectors
	private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
	private Vector3f rotation = new Vector3f((float) Math.TAU / 4, 0.0f, 0.0f);

	// Camera config
	private final float fovDeg, sensitivity;
	private final float nearPlane = 0.01f, farPlane = 100.0f;

	// Player
	private final float eyelevel = this.viewHeight - 0.2f; 
	private final float targetSpeed    = WALKING_SPEED;
	private float speed                = targetSpeed;

	public Player(Window window, float fovDeg, float sensitivity, World world) {
		super(world);

		this.window = window;
		this.fovDeg = fovDeg;
		this.sensitivity = sensitivity;

		// width = window.getWidth();
		// height = window.getHeight();
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
	
	public void movePosition(double delta, float offsetX, float offsetY, float offsetZ, boolean doubleSpeed) {
		this.speed += (float) ((this.targetSpeed - this.speed) * delta * 20);
		

		float angle = (float) (rotation.x - Math.atan2(offsetZ, offsetX) + (Math.TAU / 4));
		if(offsetX != 0.0f || offsetZ != 0.0f) {
			// position.x += (float) Math.cos(angle) * speed;
			// position.z += (float) Math.sin(angle) * speed;

			velocity[0] = (float) Math.cos(angle) * speed;
			velocity[2] = (float) Math.sin(angle) * speed;
		}

		velocity[1] = offsetY * 0.1f;
	}

	public float getNearPlane() {
		return nearPlane;
	}

	public float getFarPlane() {
		return farPlane;
	}

	public float getViewWidth() {
		return viewWidth;
	}

	public float getViewHeight() {
		return viewHeight;
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

	public float getFovWithVelocity() {
		return fovDeg + 20 * (speed - WALKING_SPEED) / (SPRINTING_SPEED - WALKING_SPEED);
	}

	public float getSensitivity() {
		return sensitivity;
	}

	public float getEyelevel() {
	    return eyelevel;
	}
}