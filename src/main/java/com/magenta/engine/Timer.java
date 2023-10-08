package com.magenta.engine;

public class Timer {
	private double last;
	private final int TARGET_FPS = 60,
					  TARGET_UPS = 120;
	private final double nsPerFrame = 1_000_000_000.0 / TARGET_FPS, // 1 sec (in nanoTime) / targetFPS => interval
						 nsPerTick  = 1_000_000_000.0 / TARGET_UPS; // Frequency time

	// public float getCurrentTime() {
	// 	return System.nanoTime();
	// }

	public float getDeltaFrame(float now) {
		// float now = getTime();
		float delta = (float) ((now - last) / nsPerFrame); // (time elapsed between frames) / interval
		return delta;
	}

	public float getDeltaTick(float now) {
		float delta = (float) ((now - last) / nsPerTick);
		return delta;
	}

	public void setLast(double last) {
		this.last = last;
	}

	public double getLast() {
		return last;
	}

	public int getTargetFPS() {
		return TARGET_FPS;
	}
}