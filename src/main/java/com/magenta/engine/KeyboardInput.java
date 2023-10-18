package com.magenta.engine;

import org.lwjgl.glfw.GLFW;

public class KeyboardInput {
	private Window window;
	private int keyDown = -1;
	
	public KeyboardInput(Window window) {
		this.window = window;
	}

	public void init() {
		GLFW.glfwSetKeyCallback(window.getWindowHandle(), (window, key, scancode, action, mods) -> {
			// Just change if is action is pressed
		  	if(action == GLFW.GLFW_PRESS) this.keyDown = key;
			// System.out.println("Key: " + key + "\nAction: " + action + "\nMods: " + mods);
		});
	}

	public boolean isPressingKey(int key) {
		return GLFW.glfwGetKey(window.getWindowHandle(), key) == GLFW.GLFW_PRESS;
	}
	
	public boolean isKeyDown(int key) {
		int currentKeyDown = keyDown;
		if(currentKeyDown != key) return false;

		// Reset keyDown after checking
		keyDown = -1; 
		return true;
		
	}

	public int getKeyDown() {
		return keyDown; // Return the stored value
	}
}