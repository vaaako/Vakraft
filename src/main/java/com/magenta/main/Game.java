package com.magenta.main;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.magenta.engine.Camera;
import com.magenta.engine.IGameLogic;
import com.magenta.engine.Window;
import com.magenta.engine.input.MouseInput;

public class Game implements IGameLogic {
	private Renderer renderer;
	private static Camera camera;
	public MouseInput mouseInput;

	private boolean firstClick = true;

	@Override
	public void init(Window window) throws Exception {
		renderer = new Renderer(window);
		renderer.init();

		camera = new Camera(window);
		window.setClearColor(1.0f, 0.5f, 1.0f, 1.0f);
	}

	@Override
	public void input(Window window, MouseInput mouseInput) {
		if(window.isKeyPressed(GLFW.GLFW_KEY_ESCAPE))
			GLFW.glfwSetWindowShouldClose(window.getWindowHandle(), true);
	}

	@Override
	public void update(float delta, MouseInput mouseInput, Window window) {
		if(mouseInput.isLMBPressed()) {
			GLFW.glfwSetInputMode(window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);

			// Prevents camera from jumping on the first click
			if(firstClick) {
				GLFW.glfwSetCursorPos(window.getWindowHandle(), (int) window.getWidth() / 2, (int) window.getHeight() / 2);
				firstClick = false;
			}

			Vector2f rotVec = mouseInput.getMovement(); // Get the movement the cursor made
			camera.moveRotation(rotVec.x * camera.getSensitivity(), rotVec.y * camera.getSensitivity()); // Change camera rotation
			// GLFW.glfwSetCursorPos(window.getWindowHandle(), (int) window.getWidth() / 2, (int) window.getHeight() / 2);
		} else if(!mouseInput.isLMBPressed()) {
			firstClick = true;
			GLFW.glfwSetInputMode(window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		}
	}

	@Override
	public void render(Window window) {
		renderer.render(camera);
	}

	@Override
	public void cleanup(Window window) {
		// All below is after while loop break

		// Destroy window
		window.destroy();

		// Other
		renderer.delete();
	}
}