package com.magenta.engine;

import org.lwjgl.glfw.GLFW;

public class KeyboardInput {
	private Window window;
	private int pressedKey = -1;
	// private int keyReleased = -1;


	public KeyboardInput(Window window) {
		this.window = window;
	}

	public void init() {
		GLFW.glfwSetKeyCallback(window.getWindowHandle(), (window, key, scancode, action, mods) -> {
			// Just change if is action is pressed
			if(action == 1) this.pressedKey = key;

			// System.out.println("Key: " + key + "\nAction: " + action + "\nMods: " + mods);
		});
	}

	public boolean isPressingKey(int key) {
		return GLFW.glfwGetKey(window.getWindowHandle(), key) == GLFW.GLFW_PRESS;
	}
	
	public boolean isKeyPressed(int key) {
		// System.out.println("pressedKey: " + pressedKey + "\n key: " + key);
		int currentKey = pressedKey;
		pressedKey = -1;
		return currentKey == key;
	}

	public int getpressedKey() {
		int currentKey = pressedKey; // Store the current value
		pressedKey = -1; // Reset the value
		return currentKey; // Return the stored value
	}
}