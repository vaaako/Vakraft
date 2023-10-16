package com.magenta.game.block;

public abstract class Model {
	// Config
	protected boolean isTransparent; // Not necessary transparent but facing block is visible through
	protected boolean isCube;
	protected boolean isGlass;

	// Mesh
	protected float[][] vertexPositions;
	protected float[][] texCoords;
	protected float[][] shadingValue;
	
	public Model(boolean isTransparent, boolean isCube, boolean isGlass, float[][] vertexPositions, float[][] texCoords, float[][] shadingValue) {
		this.isTransparent = isTransparent;
		this.isCube = isCube;
		this.isGlass = isGlass;

		this.vertexPositions = vertexPositions;
		this.texCoords = texCoords;
		this.shadingValue = shadingValue;

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


	// This is to be made when creating a model, so there is why is returning itself
	// public Model setIsGlass(boolean isGlass) {
	// 	this.isGlass = isGlass;
	// 	return this;
	// }
}