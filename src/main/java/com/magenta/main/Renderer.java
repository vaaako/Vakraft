package com.magenta.main;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.magenta.engine.Camera;
import com.magenta.engine.Window;
import com.magenta.game.World;
import com.magenta.game.block.BlockManager;
import com.magenta.render.mesh.MeshLoader;
import com.magenta.render.ShaderProgram;
import com.magenta.render.TextureManager;

public class Renderer {
	private TextureManager texManager;
	private ShaderProgram shaderProgram;

	private final Window window;

	// Matrices //
	// Render Matrices
	private Matrix4f mvMatrix, pMatrix;

	// Temp rotation
	// private float cuberotation = 0.5f;
	// private Matrix4f cubeModelMatrix4f = new Matrix4f();
	// private Matrix4f finalModelViewRotationMatrix = new Matrix4f();

	// Camera
	private final Camera camera;
	private final float nearPlane, farPlane;

	// Matrices
	private Vector3f position;
	private Vector2f rotation;

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
		rotation = new Vector2f();
	}

	public void init() {
		// Load shaders
		shaderProgram = new ShaderProgram("vertex.glsl", "fragment.glsl");

		// Load Texture Manager	
		// texManager = new TextureManager(16, 16, 256);

		// Get cube
		// BlockManager blockManager = new BlockManager(texManager);
		// Block block = blockManager.getBlock("grass");

		// Load mesh (the mesh needs to be initialized)
		// mesh = MeshLoader.createMesh(block.vertexPositions, block.indices, block.texCoords, block.shadingValue);

		// Load first VAO
		// MeshLoader.genVAO();
	}

	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	// Clear frame buffer of Color and Depth (Depth: 3D)
	}

	public void render(World world) {
		clear();

		// Use shader
		shaderProgram.use();
		
		// Use texture
		// texManager.use();
		world.getTexManager().use(); // Think on another way

		// Basic Chunk //
		// Draw camera and matrices
		// for(int x = 0; x < 16; x++) {
		// 	for(int y = 0; y < 16; y++) {
		// 		for(int z = 0; z < 16; z++) {
		// 			updateMatrices(new Vector3f(x, y, z));
		// 			GL30.glBindVertexArray(mesh.getVaoID()); // Use VAO	
		// 			GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0); // Draw triangle
		// 			/*   ^ Args: Primitive type, How many indices to draw, Data type, Index of indicies */
		// 		}
		// 	}
		// }

		// One block //
		// updateMatrices();
		// GL30.glBindVertexArray(mesh.getVaoID()); // Use VAO
		// GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0); // Draw triangle

		// Method 1 //
		// for(Chunk chunk : world.getChunks().values()) {
		// 	updateMatrices();
		// 	GL30.glBindVertexArray(chunk.getMesh().getVaoID()); // Use VAO
		// 	GL11.glDrawElements(GL11.GL_TRIANGLES, chunk.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0); // Draw triangle
		// }

		updateMatrices();
		world.render();

		GL30.glBindVertexArray(0); // Unloads VAO
		window.update();
	}

	public void updateMatrices() {
	// public void updateMatrices(Vector3f blockPosition) {
		pMatrix.identity(); // Create matrix
		pMatrix.perspective((float) Math.toRadians(camera.getFovDeg()), (float)camera.getWidth() / camera.getHeight(), nearPlane, farPlane); // Adds perspective to the scene
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
	// public void setCuberotation(float cuberotation) {
	// 	this.cuberotation = cuberotation;
	// }






	public void delete() {
		shaderProgram.delete();
		// texManager.delete();
	}
}