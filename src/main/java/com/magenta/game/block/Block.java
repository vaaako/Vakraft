package com.magenta.game.block;

public class Block {
	public final float[] vertexPositions = { // 12
		 0.5f,  0.5f,  0.5f,  0.5f, -0.5f,  0.5f,  0.5f, -0.5f, -0.5f,  0.5f,  0.5f, -0.5f, // X + 1
		-0.5f,  0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f,  0.5f, -0.5f,  0.5f,  0.5f, // X - 1
		 0.5f,  0.5f,  0.5f,  0.5f,  0.5f, -0.5f, -0.5f,  0.5f, -0.5f, -0.5f,  0.5f,  0.5f, // Y + 1
		-0.5f, -0.5f,  0.5f, -0.5f, -0.5f, -0.5f,  0.5f, -0.5f, -0.5f,  0.5f, -0.5f,  0.5f, // Y - 1
		-0.5f,  0.5f,  0.5f, -0.5f, -0.5f,  0.5f,  0.5f, -0.5f,  0.5f,  0.5f,  0.5f,  0.5f, // Z + 1
		 0.5f,  0.5f, -0.5f,  0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f,  0.5f, -0.5f  // Z - 1
	};

	public final float[] texCoords = {
		0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
		0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
		0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
		0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
		0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
		0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f
	};

	public final float[] shadingValue = {
		0.6f, 0.6f, 0.6f, 0.6f,
		0.6f, 0.6f, 0.6f, 0.6f,
		1.0f, 1.0f, 1.0f, 1.0f,
		0.4f, 0.4f, 0.4f, 0.4f,
		0.8f, 0.8f, 0.8f, 0.8f,
		0.8f, 0.8f, 0.8f, 0.8f
	};

	public final float[] shading = {
		0.80f, 0.80f, 0.80f, 0.80f,
		0.80f, 0.80f, 0.80f, 0.80f,
		1.0f,  1.0f,  1.0f,  1.0f,
		0.49f, 0.49f, 0.49f, 0.49f,
		0.92f, 0.92f, 0.92f, 0.92f,
		0.92f, 0.92f, 0.92f, 0.92f,
	};

	public final int[] indices = {
		 0,  1,  2,  0,  2,  3, // Right
		 4,  5,  6,  4,  6,  7, // Left
		 8,  9, 10,  8, 10, 11, // Top
		12, 13, 14, 12, 14, 15, // Bottom
		16, 17, 18, 16, 18, 19, // Front
		20, 21, 22, 20, 22, 23, // Back
	};


	private final String name;
	private final String[] faces; // { top, bottom, sides }

	public Block(String name, String[] faces) {
		this.name = name;
		this.faces = faces;
	}

	public String[] getFaces() {
		return faces;
	}

	public String getFace(int index) {
		return faces[index];
	}


	public float[] getVertexPositions() {
		return vertexPositions;
	}

	public float[] getTexCoords() {
		return texCoords;
	}

	public float[] getShadingValues() {
	    return shadingValue;
	}





	public boolean isTransparent() {
		return false;
	}

	public boolean isCube() {
		return true;
	}

}