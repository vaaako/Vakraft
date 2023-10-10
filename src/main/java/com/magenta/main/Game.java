package com.magenta.main;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.magenta.engine.Camera;
import com.magenta.engine.IGameLogic;
import com.magenta.engine.Window;
import com.magenta.engine.MouseInput;

public class Game implements IGameLogic {
	private Renderer renderer;
	private Camera camera;

	private final Vector3f cameraInc; // Movement
	private boolean firstClick = true,
					movimentEnable = false,
					doubleSpeed =  false;

	// Temp rotate
	float rotation = 0.0f, prevTime = (float) GLFW.glfwGetTime();

	public Game() {
		cameraInc = new Vector3f();
	}

	@Override
	public void init(Window window) throws Exception {
		renderer = new Renderer(window);
		renderer.init();

		camera = new Camera(window, 0.06f);

		window.setClearColor(1.0f, 0.5f, 1.0f, 1.0f);
	}

	@Override
	public void input(Window window, MouseInput mouseInput) {
		if(window.isKeyPressed(GLFW.GLFW_KEY_ESCAPE))
			GLFW.glfwSetWindowShouldClose(window.getWindowHandle(), true);

		cameraInc.set(0, 0, 0); // Reset values

		// Movement //
		if(window.isKeyPressed(GLFW.GLFW_KEY_W))
			cameraInc.z = 1;
		else if(window.isKeyPressed(GLFW.GLFW_KEY_S))
			cameraInc.z = -1;

		if(window.isKeyPressed(GLFW.GLFW_KEY_A))
			cameraInc.x = 1;
		else if(window.isKeyPressed(GLFW.GLFW_KEY_D))
			cameraInc.x = -1;


		if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
			cameraInc.y = -1;
		else if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE))
			cameraInc.y = 1;

		if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL))
			doubleSpeed = !doubleSpeed;
	}

	@Override
	public void update(double delta, MouseInput mouseInput, Window window) {
		// Move with cameraInc
		camera.movePosition(cameraInc.x, cameraInc.y, cameraInc.z,
			doubleSpeed ? 2.0f : 1.0f);


		// Updates 60 times per second
		double now = GLFW.glfwGetTime();
		if(now - prevTime >= (1 / 60)) {
			rotation += 1.0f;
			now = prevTime;
		}
		camera.setCuberotation(rotation);


		// Temp
		if(mouseInput.isLMBPressed()) {
			// Disable cursor, enable moviment
			if(!movimentEnable) {
				movimentEnable = true;
				GLFW.glfwSetInputMode(window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
				// GLFW.glfwSetInputMode(window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_CAPTURED);

				GLFW.glfwSetCursorPos(window.getWindowHandle(), (int) window.getWidth() / 2, (int) window.getHeight() / 2);
			}
		} else if(mouseInput.isRMBPressed()) {
			movimentEnable = false;
			GLFW.glfwSetInputMode(window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		}

		if(movimentEnable) {
			Vector2f rotVec = mouseInput.getMovement(); // Get the movement the cursor made
			camera.moveRotation(rotVec.x * camera.getSensitivity(), rotVec.y * camera.getSensitivity()); // Change camera rotation
			// GLFW.glfwSetCursorPos(window.getWindowHandle(), (int) window.getWidth() / 2, (int) window.getHeight() / 2);
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