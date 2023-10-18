package com.magenta.game.block;

public abstract class Model {
	// Config
	protected boolean isTransparent; // Not necessary transparent but facing block is visible through
	protected boolean isCube;
	protected boolean isGlass;

	// Mesh
	protected float[][] vertexPositions;
	protected float[][] texCoords = {
		{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f },
		{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f },
		{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f },
		{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f },
		{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f },
		{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f }	
	};

	protected float[][] shadingValue = {
		{ 0.6f, 0.6f, 0.6f, 0.6f },
		{ 0.6f, 0.6f, 0.6f, 0.6f },
		{ 1.0f, 1.0f, 1.0f, 1.0f },
		{ 0.4f, 0.4f, 0.4f, 0.4f },
		{ 0.8f, 0.8f, 0.8f, 0.8f },
		{ 0.8f, 0.8f, 0.8f, 0.8f }
	};
	
	public Model(boolean isTransparent, boolean isCube, boolean isGlass, float[][] vertexPositions) {
		this.isTransparent = isTransparent;
		this.isCube = isCube;
		this.isGlass = isGlass;

		this.vertexPositions = vertexPositions;
	}

	public boolean isTransparent() {
		return isTransparent;
	}

	public boolean isCube() {
		return isCube;
	}

	public boolean isGlass() {
		return isGlass;
	}

	public float[][] getVertexPositions() {
		return vertexPositions;
	}

	public float[][] getTexCoords() {
		return texCoords;
	}

	public float[][] getShadingValue() {
		return shadingValue;
	}


	// Vertex and Texture almost always are the same
	protected void setShadingValue(float[][] newShadingValue) {
		this.shadingValue = newShadingValue;
	}
}