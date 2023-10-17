package com.magenta.game;

import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class Entity {	
	protected final World world;
	
	// Physical variables
	protected final Vector3f position = new Vector3f(0.0f, 80.0f, 0.0f);
	protected final Vector2f rotation = new Vector2f((float) -Math.TAU / 4, 0.0f);


	// protected final Vector3f velocity = new Vector3f(0, 0, 0);
	// protected final Vector3f accel    = new Vector3f(0, 0, 0);

	// TODO try to change to vector3f later
	protected float[] velocity    = new float[] { 0.0f, 0.0f, 0.0f };
	protected final float[] accel    = new float[] { 0.0f, 0.0f, 0.0f };


	// Colission variables
	protected final float viewWidth = 0.6f;
	protected final float viewHeight = 1.8f;



	protected Entity(World world) {
		this.world = world;
	}

	public void updateEntity(double deltaTime) {
		for (int i = 0; i < velocity.length; i++) {
			// velocity[i] = velocity[i] + accel[i] * friction[i] * deltaTime;
			velocity[i] = (float) (velocity[i] + accel[i] * deltaTime);
		}
		velocity = new float[] {0.0f, 0.0f, 0.0f};
	}
}