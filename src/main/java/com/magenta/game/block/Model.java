package com.magenta.game.block;

public abstract class Model {
	protected boolean isTransparent; // Not necessary transparent but facing block is visible through
	protected boolean isCube;
	protected float[][] vertexPositions;
	protected float[][] texCoords;
	protected float[][] shadingValue;
    
    public Model(boolean isTransparent, boolean isCube, float[][] vertexPositions, float[][] texCoords, float[][] shadingValue) {
		this.isTransparent = isTransparent;
		this.isCube = isCube;
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

	public float[][] getVertexPositions() {
		return vertexPositions;
	}

	public float[][] getTexCoords() {
		return texCoords;
	}

	public float[][] getShadingValue() {
		return shadingValue;
	}
}