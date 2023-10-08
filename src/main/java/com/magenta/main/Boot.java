package com.magenta.main;

import org.lwjgl.glfw.GLFWKeyCallback;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.magenta.engine.Camera;
import com.magenta.engine.Window;
import com.magenta.engine.input.MouseInput;


public class Boot {
	private static Window window;
	private int width = 640, height = 480;
	
	private Renderer renderer;
	private MouseInput mouseInput;
	private static Camera camera;
	
	private boolean firstInput = false;  // Prevents the camera from jumping around when first clicking left click

	// Handle keys (temp)
	// private static GLFWKeyCallback keyboardProcessInput = new GLFWKeyCallback() {
	// 	@Override
	// 	public void invoke(long window, int key, int scancode, int action, int mods) {
	// 		camera.keyboardInputs(); // TODO: Pass to another class later
	// 	}
	// };


	public void run() {
		window = new Window("Just testing around", width, height, true);
		window.initGLFW();
		// window.setKeyboardCallback(keyboardProcessInput);

		loop();
		// All below is after while loop break

		// Destroy window
		window.destroy();

		// Other
		renderer.delete();
	}

	public void loop() {
		// window.setKeyboardCallback(keyboardProcessInput);

		renderer = new Renderer(window);
		renderer.init();

		// Load camera
		camera = new Camera(window);

		// Load mouse input
		mouseInput = new MouseInput();
		mouseInput.init(window);

		window.setClearColor(1.0f, 0.5f, 1.0f, 1.0f);
		while (!window.windowShouldClose()) {
			renderer.render(camera);

			mouseInput.input();
			if(mouseInput.isLMBPressed()) {
				GLFW.glfwSetInputMode(window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
				Vector2f rotVec = mouseInput.getMovement(); // Get the movement the cursor made
				camera.moveRotation(rotVec.x * camera.getSensitivity(), rotVec.y * camera.getSensitivity()); // Change camera rotation
				firstInput = false;

				// GLFW.glfwSetCursorPos(window.getWindowHandler(), window.getWidth()/2, window.getHeight()/2);
			} else if(!mouseInput.isLMBPressed()) {
				firstInput = true;
				GLFW.glfwSetInputMode(window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
			}
		}
	}
}
