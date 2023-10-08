package com.magenta.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.magenta.render.shader.ShaderProgram;

public class Camera {
	private Window window;
	private int width, height;

	// Camera options
	private final float speed = 0.1f, sensitivity = 0.1f;

	// Movement vectors
	private Vector3f position = new Vector3f(0.0f, 0.0f, -3.0f);
	private Vector2f rotation = new Vector2f((float) Math.TAU / 4, 0.0f);

	// Render Matrices
	private Matrix4f mvMatrix, pMatrix;

	// Temp rotate
	// private Vector3f axis = new Vector3f(0.0f, 1.0f, 0.0f);
	// private float rotation = 0.5f;

	public Camera(Window window) {
		this.window = window;

		width = window.getWidth();
		height = window.getHeight();

		position = new Vector3f(0.0f, 0.0f, -3.0f);
		rotation = new Vector2f((float) Math.TAU / 4, 0.0f);

		mvMatrix  = new Matrix4f();
		pMatrix   = new Matrix4f();
	}

	public void setRotation(float x, float y, float z) {
		rotation.x = x;
		rotation.y = y;
		// rotation.z = z;
	}

	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}

	public void moveRotation(double xpos, double ypos) {
		rotation.x -= xpos * sensitivity;
		rotation.y += ypos * sensitivity;
		// rotation.z += offsetZ;

		// Avoid 360º spin (Y only duurh)
		rotation.y = Math.max((float) (-Math.TAU / 4), Math.min((float) (Math.TAU / 4), rotation.y));
	}

	public void movePosition(float offsetX, float offsetY, float offsetZ, float doubleSpeed) {
		float angle = (float)(rotation.x - Math.atan2(offsetZ, offsetX) + (Math.TAU / 4));
		if(offsetX != 0.0f || offsetZ != 0.0f) {
			position.x += (float) Math.cos(angle) * doubleSpeed * 0.1f;
			position.z += (float) Math.sin(angle) * doubleSpeed * 0.1f;
		}

		position.y += offsetY * 0.1f;
	}

	private void rotate2D(Matrix4f matrix, float x, float y) {
		matrix.rotate(x, 0.0f, 1.0f, 0.0f);
		matrix.rotate(-y, (float) Math.cos(x), 0.0f, (float) Math.sin(x));
	}




	public void matrix(float FOVdeg, float nearPlane, float farPlane, ShaderProgram shaderProgram) {
		pMatrix.identity(); // Create matrix
		pMatrix.perspective((float) Math.toRadians(FOVdeg), (float)width / height, nearPlane, farPlane); // Adds perspective to the scene
		// Last two args: Closest/Furthest that can see = If something is closer than 0.1 units then it will clipped, if is further away than 100.0 units it will again get clipped

		mvMatrix.identity();

		mvMatrix.translate(-position.x, -position.y, position.z); // Move camera (minus = away, positive = zoom)	
		// ^ Translates plugging on view / Indicating in which direction and how much to move the world
		
		// Moving whole world instead of camera	
		// It needs to be negative because technically is the scene that is moving arround the camera
		rotate2D(mvMatrix, (float) -(rotation.x - Math.TAU / 4), (float) rotation.y);
		// mvMatrix.rotate((float) -(Math.toRadians(rotation.x) - Math.TAU / 4), new Vector3f((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y), 0.0f));


		// Exports the camera amtrix to the Vertex Shader (proj * view)
		shaderProgram.uniformMatrix(shaderProgram.findUniform("camMatrix"), pMatrix.mul(mvMatrix));
	}

	// public void keyboardInputs() {
	// 	// Keyboard //
	// 	if(GLFW.glfwGetKey(window.getWindowHandle(), GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_TRUE)
	// 		GLFW.glfwSetWindowShouldClose(window.getWindowHandle(), true); // Close window
	// }

	public float getSensitivity() {
	    return sensitivity;
	}

	// Update on resize
	public void updateSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
}