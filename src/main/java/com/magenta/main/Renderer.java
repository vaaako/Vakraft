package com.magenta.main;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.magenta.engine.Camera;
import com.magenta.engine.Window;
import com.magenta.engine.input.MouseInput;
import com.magenta.game.block.Block;
import com.magenta.game.block.BlockManager;
import com.magenta.render.mesh.Mesh;
import com.magenta.render.mesh.MeshLoader;
import com.magenta.render.shader.ShaderProgram;
import com.magenta.render.texture.TextureManager;

public class Renderer {
	private Mesh mesh;
	private TextureManager texManager;
	private BlockManager blockManager;
	// private static Camera camera;
	// private static MouseInput mouseInput;	
	private ShaderProgram shaderProgram;

	private final Window window;

	public Renderer(Window window) {
		this.window = window;
	}

	public void init() {

		/**
		 * Move camera and releated to another class
		 * 
		 * */
		// mouseInput = new MouseInput();
		// mouseInput.init(window);

		// Load shaders
		shaderProgram = new ShaderProgram("vertex.glsl", "fragment.glsl");
		// matrixPosition = shaderProgram.findUniform("camMatrix");
		// texSampler = shaderProgram.findUniform("texSampler");

		// Load Texture Manager	
		texManager = new TextureManager(16, 16, 256);

		// Get cube
		blockManager = new BlockManager(texManager);
		Block block = blockManager.getBlock("grass");

		// Load mesh
		mesh = MeshLoader.createMesh(block.vertexPositions, block.indices, block.texCoords);
	}

	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	// Clear frame buffer of Color and Depth (Depth: 3D)
	}


	public void render(Camera camera) {
		clear();

		// Use shader
		shaderProgram.use();
		
		// Draw camera and matrices
		camera.matrix(90.0f, 0.01f, 100.0f, shaderProgram);

		// Use texture
		texManager.use();

		// Draw
		GL30.glBindVertexArray(mesh.getVaoID()); // Use VAO
		GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0); // Draw triangle
		/*   ^ Args: Primitive type, How many indices to draw, Data type, Index of indicies */

		GL30.glBindVertexArray(0); // Unloads VAO
		window.update();
	}

	public void delete() {
		shaderProgram.delete();
		texManager.delete();
	}
}