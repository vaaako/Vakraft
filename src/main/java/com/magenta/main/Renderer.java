package com.magenta.main;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.magenta.engine.Camera;
import com.magenta.engine.Window;
import com.magenta.game.World;
import com.magenta.game.block.BlockType;
import com.magenta.game.block.BlocksEnum;
import com.magenta.render.ShaderProgram;
import com.magenta.render.TextureManager;
import com.magenta.render.mesh.Mesh;
import com.magenta.render.mesh.MeshLoader;



public class Renderer {
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

	// This is just for view a single block	
	// private final TextureManager texManager = new TextureManager(16, 16, 256);
	// private final BlockType blockType = new BlockType(BlocksEnum.AHIRO, texManager);
	// private final int[] indices = {0, 0, 0};
	// private final Mesh mesh = new MeshLoader(blockType.getVertexPositions(), indices, blockType.getTexCoords(), blockType.getShadingValues());

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
	}

	private void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	// Clear frame buffer of Color and Depth (Depth: 3D)
	}

   // float[] vertices = {
   //      centerX - halfSize, centerY - halfSize, // Bottom-left
   //      centerX + halfSize, centerY - halfSize, // Bottom-right
   //      centerX + halfSize, centerY + halfSize, // Top-right
   //      centerX - halfSize, centerY + halfSize  // Top-left
   //  };

	// WARNING: Still working on this //
	private void renderAim() {
		// float size = 0.01f; // Adjust the size as needed

		// // Define vertices and indices
		// float[] vertices = {
		// 	-size, -size, 0.0f, // Bottom-left
		// 	 size, -size, 0.0f,  // Bottom-right
		// 	 size,  size, 0.0f,   // Top-right
		// 	-size,  size, 0.0f   // Top-left
		// };

		// int indices[] = {
		// 	0, 1, 2, // Upper triangle
		// 	0, 2, 3  // Bottom triangle
		// };

		float radius = 0.01f; // Adjust the radius as needed
		int segments = 10;  // Number of segments to approximate a circle

		// Define vertices and indices for a circle
		float[] vertices = new float[(segments + 1) * 3];
		int[] indices = new int[segments * 3];

		for (int i = 0; i <= segments; i++) {
		    float angle = (float) (2 * Math.PI * i / segments);
		    vertices[i * 3] = (float) (radius * Math.cos(angle));
		    vertices[i * 3 + 1] = (float) (radius * Math.sin(angle));
		    vertices[i * 3 + 2] = 0.0f;
		}

		for (int i = 0; i < segments; i++) {
		    indices[i * 3] = 0;
		    indices[i * 3 + 1] = i + 1;
		    indices[i * 3 + 2] = i + 2;
		}


		Mesh mesh = MeshLoader.createMesh(vertices, indices);
		ShaderProgram shader = new ShaderProgram("rectver.glsl", "rectfrag.glsl");

		// Use shader
		shader.use();

		// Set up projection and model matrices
		Matrix4f projectionMatrix = new Matrix4f().ortho2D(-1, 1, -1, 1);
		Matrix4f modelMatrix = new Matrix4f().translate(0, 0, 0);

		// Combine projection and model matrices
		Matrix4f MVP = new Matrix4f(projectionMatrix).mul(modelMatrix);

		// Set the MVP matrix in your shader program
		int matrixLocation = GL30.glGetUniformLocation(shader.getProgramID(), "MVP");
		FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
		MVP.get(matrixBuffer);
		GL30.glUniformMatrix4fv(matrixLocation, false, matrixBuffer);

		// Render the square
		GL30.glBindVertexArray(mesh.getVaoID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
		// GL30.glBindVertexArray(0); // Unload VAO
	}

	public void render(World world) {
		clear();

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
		

		// renderAim();


		GL30.glBindVertexArray(0); // Unloads VAO
		window.update();
	}

	private void updateMatrices() {
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