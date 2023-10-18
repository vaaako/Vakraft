package com.magenta.game;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.magenta.render.ShaderProgram;
import com.magenta.render.mesh.Mesh;
import com.magenta.render.mesh.MeshLoader;

public class Aim {
	// Mesh
	private final float[] vertices;
	private final int[] indices;

	// Format
	private final float size = 0.01f; // Radius (if circle)
	private final int segments = 10;  // Number of segments to approximate a circle

	// Programs
	private final Mesh mesh;
	private final ShaderProgram shaderProgram;

	// Matrices
	// Matrix4f projectionMatrix = new Matrix4f().ortho2D(-1, 1, -1, 1);
	// Matrix4f modelMatrix      = new Matrix4f().translate(0, 0, 0);
	// Matrix4f modelViewProjection = new Matrix4f(projectionMatrix).mul(modelMatrix);


	public enum AimType {
		SQUARE, CIRCLE;
	}

	public Aim(AimType type) {
		if(type == AimType.SQUARE) {
			vertices = new float[] {
				-size, -size, 0.0f, // Bottom-left
				 size, -size, 0.0f, // Bottom-right
				 size,  size, 0.0f, // Top-right
				-size,  size, 0.0f  // Top-left
			};

			indices = new int[] {
				0, 1, 2, // Upper triangle
				0, 2, 3  // Bottom triangle
			};
		} else {
			vertices = new float[(segments + 1) * 3];
			indices = new int[segments * 3];

			for (int i = 0; i <= segments; i++) {
				float angle = (float) (2 * Math.PI * i / segments);
				vertices[i * 3] = (float) (size * Math.cos(angle));
				vertices[i * 3 + 1] = (float) (size * Math.sin(angle));
				vertices[i * 3 + 2] = 0.0f;
			}

			for (int i = 0; i < segments; i++) {
				indices[i * 3] = 0;
				indices[i * 3 + 1] = i + 1;
				indices[i * 3 + 2] = i + 2;
			}
		}


		mesh = MeshLoader.createMesh(vertices, indices);
		shaderProgram = new ShaderProgram("aim/vertex.glsl", "aim/fragment.glsl");
	}

	public void render() {
		shaderProgram.use();

		// Set the MVP matrix in your shader program
		// int matrixLocation = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "MVP");
		// FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
		// modelViewProjection.get(matrixBuffer);
		// GL20.glUniformMatrix4fv(matrixLocation, false, matrixBuffer);

		GL30.glBindVertexArray(mesh.getVaoID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
	}
}