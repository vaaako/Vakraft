package com.magenta.engine;

import java.util.function.Function;

import org.joml.Vector3f;
import org.joml.Vector3i;

import com.magenta.game.World;
import com.magenta.game.block.BlocksEnum;

public class HitRay {
	public static int HIT_RANGE = 5;

	private Vector3f vector, position, block;
	// private final Vector3f rotation;

	private final World world;
	private float distance;

	public HitRay(World world, Vector3f rotation, Vector3f startingPos) {
		this.world = world;

		// Get the ray unit vector based on rotation angles
		// sqrt(ux ^ 2 + uy ^ 2 + uz ^ 2) must always equal 1
		this.vector = new Vector3f(
			(float) (Math.cos(rotation.x) * Math.cos(rotation.y)),
			(float) Math.sin(rotation.y),
			(float) (Math.sin(rotation.x) * Math.cos(rotation.y))
		);

		// Block position in wich point currently is
		this.position = new Vector3f(startingPos);
		this.block = new Vector3f(
			Math.round(this.position.x),
			Math.round(this.position.y),
			Math.round(this.position.z)
		);

		this.position = new Vector3f(startingPos); // Point position
		// this.rotation = rotation;

		// Current distance the point has travelled
		this.distance = 0;
	}

	// Check and step both return true if something is hit, and false if not
	public boolean check(Function<Vector3f[], Integer> hitCallback, float distance, Vector3f currBlock, Vector3f nextBlock) {
		if(world.getBlockInChunk(nextBlock.x, nextBlock.y, nextBlock.z) != 0) {
			hitCallback.apply(new Vector3f[]{ currBlock, nextBlock });
			return true;
		} else {
			position.add(vector.x * distance, vector.y * distance, vector.z * distance);
			block = nextBlock;
			this.distance += distance;
		}

		return false;
	}

	public boolean step(Function<Vector3f[], Integer> hitCallback) {
		int bx = (int) block.x;
		int by = (int) block.y;
		int bz = (int) block.z;

		// Point position relative to block centre
		Vector3f localPos = new Vector3f(
			position.x - bx,
			position.y - by,
			position.z - bz
		);

		// We don't want to deal with negatives, so remove the sign
		// This is also cool because it means we don't need to take into account the sign of our ray vector
		// We do need to remember which components were negative for later on, however
		
		Vector3f absVector = new Vector3f(vector);
		Vector3i sign = new Vector3i(1, 1, 1); // 1 - positive / -1 - negative


		if(vector.x < 0) {
			sign.x = -1;
			absVector.x = -absVector.x;
			localPos.x = -localPos.x;
		}

		if(vector.y < 0) {
			sign.y = -1;
			absVector.y = -absVector.y;
			localPos.y = -localPos.y;
		}

		if(vector.z < 0) {
			sign.z = -1;
			absVector.z = -absVector.z;
			localPos.z = -localPos.z;
		}


		float lx = localPos.x;
		float ly = localPos.y;
		float lz = localPos.z;
		float vx = absVector.x;
		float vy = absVector.y;
		float vz = absVector.z;

		// Calculate intersections
		// I only detail the math for the first component (X) because the rest is pretty self-explanatory

		// Ray line (passing through the point) r ≡ (x - lx) / vx = (y - ly) / lz = (z - lz) / vz (parametric equation)

		// +x face fx ≡ x = 0.5 (y & z can be any real number)
		// r ∩ fx ≡ (0.5 - lx) / vx = (y - ly) / vy = (z - lz) / vz

		// x: x = 0.5
		// y: (y - ly) / vy = (0.5 - lx) / vx IFF y = (0.5 - lx) / vx * vy + ly
		// z: (z - lz) / vz = (0.5 - lx) / vx IFF z = (0.5 - lx) / vx * vz + lz

		if(vx != 0.0f) {
			float x = 0.5f;
			float y = (((0.5f - lx) / vx) * vy) + ly;
			float z = (((0.5f - lx) / vx) * vz) + lz;

			if(y >= -0.5f && y <= 0.5f && z >= -0.5f && z <= 0.5f) {
				float distance = (float) Math.sqrt(Math.pow(x - lx, 2) + Math.pow(y - ly, 2) + Math.pow(z - lz, 2));
				return check(hitCallback, distance, new Vector3f(bx, by, bz), new Vector3f(bx + sign.x, by, bz));
			}
		}

		if(vy != 0.0f) {
			float x = (((0.5f - ly) / vy) * vx) + lx;
			float y = 0.5f;
			float z = (((0.5f - ly) / vy) * vz) + lz;

			if(x >= -0.5f && x <= 0.5f && z >= -0.5f && z <= 0.5f) {
				float distance = (float) Math.sqrt(Math.pow(x - lx, 2) + Math.pow(y - ly, 2) + Math.pow(z- lz, 2));
				return check(hitCallback, distance, new Vector3f(bx, by, bz), new Vector3f(bx, by + sign.y, bz));
			}
		}

		if(vz != 0.0f) {
			float x = (((0.5f - lz) / vz) * vx) + lx;
			float y = (((0.5f - lz) / vz) * vy) + ly;
			float z = 0.5f;

			if(x >= -0.5f && x <= 0.5f && y >= -0.5f && y <= 0.5f) {
				float distance = (float) Math.sqrt(Math.pow(x - lx, 2) + Math.pow(y - ly, 2) + Math.pow(z- lz, 2));
				return check(hitCallback, distance, new Vector3f(bx, by, bz), new Vector3f(bx, by, bz + sign.z));
			}
		}


		// We can return straight away here
		// If we intersect with one face, we know for a fact we're not intersecting with any of the others	
		return false;
	}

	public float getDistance() {
		return distance;
	}

	public void setRotationAndPosition(Vector3f rotation, Vector3f position) {
		this.vector = new Vector3f(
			(float) (Math.cos(rotation.x) * Math.cos(rotation.y)),
			(float) Math.sin(rotation.y),
			(float) (Math.sin(rotation.x) * Math.cos(rotation.y))
		);

		this.position = position;
	}
}