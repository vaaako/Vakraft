package com.magenta.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.magenta.render.shader.ShaderProgram;

public class Camera {
	private int width, height;

	// Camera options
	private final float sensitivity;

	// Movement vectors
	private Vector3f position = new Vector3f(0.0f, 0.0f, -3.0f);
	private Vector2f rotation = new Vector2f((float) Math.TAU / 4, 0.0f);

	// Render Matrices
	private Matrix4f mvMatrix, pMatrix;

	// Temp rotation
	private float cuberotation = 0.5f;
	private Matrix4f cubeModelMatrix4f = new Matrix4f();
	private Matrix4f finalModelViewRotationMatrix = new Matrix4f();

	public Camera(Window window, float sensitivity) {
		this.sensitivity = sensitivity;

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
	
	public void movePosition(float offsetX, float offsetY, float offsetZ, float speed) {
		float angle = (float)(rotation.x - Math.atan2(offsetZ, offsetX) + (Math.TAU / 4));
		if(offsetX != 0.0f || offsetZ != 0.0f) {
			position.x += (float) Math.cos(angle) * speed * 0.1f;
			position.z += (float) Math.sin(angle) * speed * 0.1f;
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

		/**
		 * 
		 * Rotate before translate creates a first person camera
		 * the opposite creates a orbital camera
		 * 
		 * */
		// Moving whole world instead of camera	
		// It needs to be negative because technically is the scene that is moving arround the camera
		rotate2D(mvMatrix, (float) -(rotation.x - Math.TAU / 4), (float) rotation.y);

		mvMatrix.translate(-position.x, -position.y, position.z); // Move camera (minus = away, positive = zoom)	
		// ^ Translates plugging on view / Indicating in which direction and how much to move the world


		// Temp rotation
		cubeModelMatrix4f.identity();
		cubeModelMatrix4f.rotateY((float) Math.toRadians(cuberotation));
		finalModelViewRotationMatrix = mvMatrix.mul(cubeModelMatrix4f);

		// Exports the camera amtrix to the Vertex Shader (proj * view)
		// shaderProgram.uniformMatrix(shaderProgram.findUniform("camMatrix"), pMatrix.mul(mvMatrix));
		shaderProgram.uniformMatrix(shaderProgram.findUniform("camMatrix"), pMatrix.mul(finalModelViewRotationMatrix));
	}






	public void setCuberotation(float cuberotation) {
		this.cuberotation = cuberotation;
	}


	public float getSensitivity() {
	    return sensitivity;
	}

	// Update on resize
	// public void updateSize(int width, int height) {
	// 	this.width = width;
	// 	this.height = height;
	// }
}