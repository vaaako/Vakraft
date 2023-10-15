package com.magenta.main;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.magenta.engine.Camera;
import com.magenta.engine.Window;
import com.magenta.game.World;
import com.magenta.render.ShaderProgram;


public class Renderer {
	// private TextureManager texManager;
	private ShaderProgram shaderProgram;

	private final Window window;

	// Matrices //
	// Render Matrices
	private Matrix4f mvMatrix, pMatrix;

	// Camera
	private final Camera camera;
	private final float nearPlane, farPlane;

	// Matrices
	private Vector3f position;
	private Vector3f rotation;

	public Renderer(Window window, Camera camera) {
		this.window = window;
		this.camera = camera;

		// Matrices //
		// Consts
		nearPlane = camera.getNearPlane();
		farPlane = camera.getFarPlane();

		// Render
		mvMatrix  = new Matrix4f();
		pMatrix   = new Matrix4f();

		// Camera position/rotatio
		position = new Vector3f();
		rotation = new Vector3f();
	}

	public void init() {
		// Load shaders
		shaderProgram = new ShaderProgram("vertex.glsl", "fragment.glsl");

		// Load Texture Manager	
		// texManager = new TextureManager(16, 16, 256);
	}




	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	// Clear frame buffer of Color and Depth (Depth: 3D)
	}

	public void render(World world) {
		clear();
		// GL11.glRasterPos2i((int) (800 - 15) / 2, (int) (600 - 15) / 2);
		// GL11.glDrawPixels(15, 15, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, aim);

		// Use shader
		shaderProgram.use();
		
		// Use texture
		// texManager.use();
		world.getTexManager().use(); // Think on another way

		// One block //
		// updateMatrices();
		// GL30.glBindVertexArray(mesh.getVaoID()); // Use VAO
		// GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0); // Draw triangle

		updateMatrices();
		world.render();

		
		GL30.glBindVertexArray(0); // Unloads VAO
		window.update();
	}

	public void updateMatrices() {
		// Get camera position/rotation
		rotation = camera.getRotation();
		position = camera.getPosition();

		// System.out.println("X: " + position.x + " Y: " + position.y + " Z: " + position.z);
		
		// Projection Matrix //
		pMatrix.identity(); // Create matrix
		pMatrix.perspective((float) Math.toRadians(camera.getFovDeg()), (float)camera.getWidth() / camera.getHeight(), nearPlane, farPlane); // Adds perspective to the scene

		// Model view matrix //
		mvMatrix.identity();
		rotate2D(mvMatrix, (float) (rotation.x + Math.TAU / 4), (float) rotation.y);
		mvMatrix.translate(-position.x, -position.y, -position.z); // Move camera (minus = away, positive = zoom)	

		// Exports the camera amtrix to the Vertex Shader (proj * view)
		shaderProgram.uniformMatrix(shaderProgram.findUniform("camMatrix"), pMatrix.mul(mvMatrix));
	}

	private void rotate2D(Matrix4f matrix, float x, float y) {
		matrix.rotate(x, 0.0f, 1.0f, 0.0f);
		matrix.rotate(-y, (float) Math.cos(x), 0.0f, (float) Math.sin(x));
	}



	public void delete() {
		shaderProgram.delete();
		// texManager.delete();
	}
}