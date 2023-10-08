package com.magenta.render.shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ShaderProgram {
	private int programID;

	public ShaderProgram(String vert, String frag) {
		int vertexID = loadShader(vert, GL20.GL_VERTEX_SHADER); // Load vertex shader
		int fragmentID = loadShader(frag, GL20.GL_FRAGMENT_SHADER); // Load fragment shader
		programID = GL20.glCreateProgram(); // Create shader program

		// Bind shader on shader program
		GL20.glAttachShader(programID, vertexID);
		GL20.glAttachShader(programID, fragmentID);

		// Bind all
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);

		// Get all the Uniform variables within the shader
		GL20.glDeleteShader(vertexID);
		GL20.glDeleteShader(fragmentID);
	}

	// Get some uniform from shader
	public int findUniform(String name) {
		return GL20.glGetUniformLocation(programID, name);
	}

// Example of matrix
// float[] matrixData = {
//     1.0f, 0.0f, 0.0f, 0.0f,
//     0.0f, 1.0f, 0.0f, 0.0f,
//     0.0f, 0.0f, 1.0f, 0.0f,
//     0.0f, 0.0f, 0.0f, 1.0f
// };


	public void uniformMatrix(int location, Matrix4f matrix) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16); // Create a FloatBuffer to hold the matrix data
		matrix.get(buffer); // Copy the matrix data into the buffer
		GL30.glUniformMatrix4fv(location, false, buffer);
	}

	private int loadShader(String file, int type) {
		// Takes the data that BufferedReader is taking from Shader file and loading everythign into the String shaderSouce as one string of code to send to GLSL
		StringBuilder shaderSource = new StringBuilder();

		// Read file
		try {
			// BufferedReader reader = new BufferedReader(new FileReader("/shaders/"+file));
			BufferedReader reader = new BufferedReader(new InputStreamReader(ShaderProgram.class.getClassLoader().getResourceAsStream("shaders/"+file)));

			String line;
			while((line = reader.readLine()) != null)
				shaderSource.append(line).append("\n"); // Get lines

			reader.close(); // Close reader
		} catch (IOException e){
			System.err.println("Can't read file");
			e.printStackTrace();
			System.exit(-1);
		}

		// return shaderSource.toString();

		int shaderProgram = GL20.glCreateShader(type); // Type is any of the shader types: Vertex, Geometry or Fragment
		GL20.glShaderSource(shaderProgram, shaderSource); // With shaderProgram set, send shaderSource
		GL20.glCompileShader(shaderProgram); // Compile shader and sends it off to the CPU

		// Handle any GLSL error
		if(GL20.glGetShaderi(shaderProgram, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderProgram, 512));
			System.err.println("Couldn't compile the shader of type: " + type);
			System.exit(-1);
		}

		return shaderProgram;
	}

	public int getProgramID() {
	    return programID;
	}

	public void use() {
		GL20.glUseProgram(programID);
	}

	public void delete() {
		// Going back to default render mode
		GL20.glDeleteProgram(programID);
		// GL20.glUseProgram(0);
	}
}