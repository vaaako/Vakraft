package com.magenta.game.block;

import com.magenta.render.TextureManager;

public class BlockType {
	// Block specs //
	private final String name;
	private final String[] faces; // { top, bottom, sides }
	// private final Model model;

	// Model vars //	
	private final float[][] vertexPositions;
	private final float[][] texCoords;
	private final float[][] shadingValue;

	private final boolean isTransparent;
	private final boolean isCube;

	public BlockType(String name, String[] faces, Model model, TextureManager texManager) {
		// Block //
		this.name = name;
		this.faces = faces;
		// this.model = model;

		// Model //
		this.vertexPositions = model.getVertexPositions();
		this.texCoords = model.getTexCoords();
		this.shadingValue = model.getShadingValue();
		this.isTransparent = model.isTransparent();
		this.isCube = model.isCube();


		// Add textures //
		// Load all block textures
		int texIndex;
		for(String face : faces) { // faces => This block textures (array)
			texManager.addTexture("blocks/"+face); // Adds current texture
			texIndex = texManager.getTextureIndex("blocks/"+face); // Texture index in All Textures array (grab image)
			// ^ gets current face to apply texture

			switch (faces.length) {
				case 1:
					// Sides the same
					for(int i=0; i < 6; i++) {
						setBlockFace(i, texIndex); // Front
					}
					break;
				case 2:
					int topAndBottomFaceIndex = texManager.getTextureIndex(this.getFace(0));

					setBlockFace(0, texIndex); // Right
					setBlockFace(1, texIndex); // Left

					setBlockFace(2, topAndBottomFaceIndex); // Top
					setBlockFace(3, topAndBottomFaceIndex); // Bottom

					setBlockFace(4, texIndex); // Front
					setBlockFace(5, texIndex); // Back
					break;
				case 3:
					setBlockFace(0, texIndex); // Right
					setBlockFace(1, texIndex); // Left

					setBlockFace(2, texManager.getTextureIndex(this.getFace(0))); // Top
					setBlockFace(3, texManager.getTextureIndex(this.getFace(1))); // Bottom

					setBlockFace(4, texIndex); // Front
					setBlockFace(5, texIndex); // Back
					break;
				case 6:
					for(int i=0; i < 6; i++) {
						setBlockFace(i, texManager.getTextureIndex(this.getFace(i)));
					}
					break;
				default:
					throw new RuntimeException("Faces must have a lenght of 1, 3 or 6");
			}
			System.out.println("-> Applyed Texture "+face+"");
		}
	}

    private void setBlockFace(int face, int texture) {
		if(face > (vertexPositions.length) - 1) return;
		texCoords[face] = texCoords[face].clone();

		// Set block face
		for(int v=0; v < 4; v++) { // v -> vertex
			texCoords[face][v * 3 + 2] = texture; // Change texCord to adjust to the texture
			// This way on selecting a block the texture is alredy applied
		}
	}




	// Block // 
	public String[] getFaces() {
		return faces;
	}

	public String getFace(int index) {
		return "blocks/" + faces[index]; // Load from blocks/
	}


	public float[][] getVertexPositions() {
		return vertexPositions;
	}

	public float[][] getTexCoords() {
		return texCoords;
	}

	public float[][] getShadingValues() {
	    return shadingValue;
	}


	public boolean isTransparent() {
		return isTransparent;
	}

	public boolean isCube() {
		return isCube;
	}
}