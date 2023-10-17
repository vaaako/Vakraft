package com.magenta.game;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Entity {	
	private final World world;
	
	// Physical variables
	private final Vector3f position = new Vector3f(0.0f, 80.0f, 0.0f);
	private final Vector2f rotation = new Vector2f((float) -Math.TAU / 4, 0.0f);


	// private final Vector3f velocity = new Vector3f(0, 0, 0);
	// private final Vector3f accel    = new Vector3f(0, 0, 0);
	private final float[] velocity    = new float[] { 0, 0, 0 };
	private final float[] accel    = new float[] { 0, 0, 0 };


	// Colission variables
	private final float width = 0.6f;
	private final float height = 1.8f;



	public Entity(World world) {
		this.world = world;
	}

	public void update(double deltaTime) {
		for (int i = 0; i < velocity.length; i++) {
			// velocity[i] = velocity[i] + accel[i] * friction[i] * deltaTime;
			velocity[i] = (float) (velocity[i] + accel[i] * deltaTime);
		}


	}
}