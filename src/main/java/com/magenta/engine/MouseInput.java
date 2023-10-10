package com.magenta.engine;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MouseInput {
	private Vector2d previousPos, currentPos;
	private Vector2f movement;
	private boolean inWindow   = false,
					pressedLMB = false,
					pressedRMB = false,
					pressedMMB = false;

	public MouseInput() {
		this.previousPos = new Vector2d(0, 0);
		this.currentPos  = new Vector2d(0, 0);
		this.movement    = new Vector2f();
	}

	public void init(Window window) {
		GLFW.glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) -> {
			currentPos.x = xpos;
			currentPos.y = ypos;
		});

		GLFW.glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered) -> {
			inWindow = entered;
		});
		
		GLFW.glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
			pressedLMB = button == GLFW.GLFW_MOUSE_BUTTON_LEFT   && action == GLFW.GLFW_PRESS;
			pressedMMB = button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE && action == GLFW.GLFW_PRESS;
			pressedRMB = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT  && action == GLFW.GLFW_PRESS;
		});
	}

	// Get previous position //
	public void input() {
		movement.x = 0;
		movement.y = 0;

		if(previousPos.x > 0 && previousPos.y > 0 && inWindow) {
			// Ellapsed position
			double deltaX = currentPos.x - previousPos.x,
				   deltaY = currentPos.y - previousPos.y;

			// double centerX = window.getWidth() / 2;
			// double centerY = window.getHeight() / 2;
			// double deltaX = currentPos.x - centerX,
			// 	   deltaY = currentPos.y - centerY;

			boolean rotX = deltaX != 0,
					rotY = deltaY != 0;

			// The movement the cursor made (e.g. X +2.0 x Y +1.0)
			if(rotX)
				movement.x = (float) deltaX;
			if(rotY)
				movement.y = (float) -deltaY;

			// System.out.println(movement.x + "x" + movement.y);
		}

		previousPos.x = currentPos.x;
		previousPos.y = currentPos.y;
	}

	// Get pressed //
	public boolean isLMBPressed() {
		return pressedLMB;
	}

	public boolean isRMBPressed() {
		return pressedRMB;
	}

	public boolean isMMBPressed() {
		return pressedMMB;
	}

	// Release //
	public void releaseLMB() {
		pressedLMB = false;
	}

	public void releaseRMB() {
		pressedRMB = false;
	}

	public void releaseMMB() {
		pressedMMB = false;
	}

	// This//
	public Vector2f getMovement() {
		return movement;
	}

	public Vector2d getPreviousPos() {
		return previousPos;
	}
}