package com.magenta.render;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.magenta.engine.Camera;

public class MatrixDraw {
	// Render Matrices
	private Matrix4f mvMatrix, pMatrix;

	// Temp rotation
	private float cuberotation = 0.5f;
	private Matrix4f cubeModelMatrix4f = new Matrix4f();
	private Matrix4f finalModelViewRotationMatrix = new Matrix4f();

	// Camera
	private final Camera camera;
	private final float width, height, nearPlane, farPlane;

	// Matrices
	private Vector3f position;
	private Vector2f rotation;


	public MatrixDraw(Camera camera) {
		this.camera = camera;

		width = camera.getWidth();
		height = camera.getHeight();
		nearPlane = camera.getNearPlane();
		farPlane = camera.getFarPlane();

		mvMatrix  = new Matrix4f();
		pMatrix   = new Matrix4f();

		position = new Vector3f();
		rotation = new Vector2f();
	}

	public void updateMatrices(ShaderProgram shaderProgram) {
	// public void updateMatrices(Vector3f blockPosition, ShaderProgram shaderProgram) {
		pMatrix.identity(); // Create matrix
		pMatrix.perspective((float) Math.toRadians(camera.getFovDeg()), (float)width / height, nearPlane, farPlane); // Adds perspective to the scene
		// Last two args: Closest/Furthest that can see = If something is closer than 0.1 units then it will clipped, if is further away than 100.0 units it will again get clipped

		mvMatrix.identity();

		rotation = camera.getRotation();
		position = camera.getPosition();

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
		// mvMatrix.translate(-position.x + blockPosition.x, -position.y + blockPosition.y, position.z + blockPosition.z); // Move camera (minus = away, positive = zoom)	
		// ^ Translates plugging on view / Indicating in which direction and how much to move the world


		// Temp rotation
		// cubeModelMatrix4f.identity();
		// cubeModelMatrix4f.rotateY((float) Math.toRadians(cuberotation));
		// finalModelViewRotationMatrix = mvMatrix.mul(cubeModelMatrix4f);

		// Exports the camera amtrix to the Vertex Shader (proj * view)
		shaderProgram.uniformMatrix(shaderProgram.findUniform("camMatrix"), pMatrix.mul(mvMatrix));
		// shaderProgram.uniformMatrix(shaderProgram.findUniform("camMatrix"), pMatrix.mul(finalModelViewRotationMatrix));
	}

	private void rotate2D(Matrix4f matrix, float x, float y) {
		matrix.rotate(x, 0.0f, 1.0f, 0.0f);
		matrix.rotate(-y, (float) Math.cos(x), 0.0f, (float) Math.sin(x));
	}

	// Temp
	public void setCuberotation(float cuberotation) {
		this.cuberotation = cuberotation;
	}
}