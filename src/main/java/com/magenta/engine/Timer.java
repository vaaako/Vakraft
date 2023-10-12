package com.magenta.engine;

public class Timer {
	private double last;
	
	public Timer() {
		last = getTime();
	}

	public double getCurrentTime() {
		return System.nanoTime();
	}

	public double getTime() {
		return System.nanoTime() / 1e9;
	}

	public double getElapsedTime() {
		double now = getTime();
		double deltaTime = now - last; // Calc elapsed time
		last = now; // Lat time now is "now" time
		return deltaTime;
	}


	public void setLast(double last) {
		this.last = last;
	}

	public double getLast() {
		return last;
	}
}