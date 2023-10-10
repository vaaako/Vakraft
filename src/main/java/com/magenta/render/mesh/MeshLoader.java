package com.magenta.render.mesh;

import java.nio.FloatBuffer; //  The buffers that the Vertex data is ultimately stored in
import java.nio.IntBuffer;
import java.util.ArrayList; 
import java.util.List; //List and ArrayLists are containers for storing data, in this case the VBO/VAO IDs

import org.lwjgl.BufferUtils; //For creating the FloatBuffer
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * A VAO is an array which contains the VBOs of a mesh.
 * The VBOs contain the mesh's Vertex positions, indices,
 * texture coordinates, and normals
 * */
public class MeshLoader {
	public static List<Integer> VAOs = new ArrayList<Integer>(); // Vertex Array Objects
	public static List<Integer> VBOs = new ArrayList<Integer>(); // Vertex Buffer Objects

	// Turns arrays of float/int data in a Buffer
	private static FloatBuffer createFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data); // Put data into the bufffer
		buffer.flip(); // Set to read mode (only gets what was written in the buffer)
		return buffer;
	}

	// Same as above but to EBO
	private static IntBuffer createIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	/* VBOs for the Vertex and Index data */
	private static void storeData(int attribute, int dimensions, float[] data) {
		int vbo = GL15.glGenBuffers(); // Creates a VBO ID
		VBOs.add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo); // Loads the current VBO to store the data

		FloatBuffer buffer = createFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); // Introduce the vertices into the VBO	
		/**
		 * Args:
		 * BUffer type, Size of data in bytes, Data, Use of this data
		 * 
		 * Data Uses:
		 * GL_STATIC  -> Vertices modified once
		 * GL_DYNAMIC -> Vertices modified more than once
		 * _DRAW      -> Vertices will be modified and be used to draw image to screen
		 * _READ / _COPY
		*/

		// Configure the Vertex Attibute so that OpenGL knows how to read the VBO
		GL20.glVertexAttribPointer(attribute, dimensions, GL11.GL_FLOAT, false, 0, 0);
		/**
		 * Args:
		 * Position of the vertex attribute,
		 * How many values per vertex -> (if is vec3 is composed of 3 values, for exmaple),
		 * Kinda of value
		 * Amount
		 * Offset (pointer to where the vertices begin in the array)
		 * */

		GL20.glEnableVertexAttribArray(attribute); // Enabling Vertex Positions to be drawn
		// GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // Unloads the current VBO when done
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo); // Unloads the current VBO when done
	}

	// Creates new EBO (indice buffer)
	private static void bindIndices(int[] data) {
		int vbo = GL15.glGenBuffers();
		VBOs.add(vbo);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		IntBuffer buffer = createIntBuffer(data);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	// Creates new VAO
	private static int genVAO() {
		int vao = GL30.glGenVertexArrays(); // Gen VAO
		VAOs.add(vao); // Add to VAOs
		GL30.glBindVertexArray(vao); // Use
		return vao;
	}

	// Generates VAO, VBO and EBO
	public static Mesh createMesh(float[] vertices, int[] indices, float[] texCoords, float[] shadingValue) {
		// UV -> Area on the txture the Mesh is using
		int vao = genVAO(); // Gen VAO

		storeData(0, 3, vertices);  // Store vertices positions
		storeData(1, 3, texCoords); // Texture coords
		storeData(2, 1, shadingValue); // Shading

		bindIndices(indices); // Bind
		GL30.glBindVertexArray(0); // Delete
		return new Mesh(vao, indices.length);
	}

	// public static void createTextuer(float[] texCoords) {
	// 	storeData(1, 3, texCoords); // Store texture coords
	// 	GL30.glBindVertexArray(0); // Delete
	// }
}
