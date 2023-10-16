package com.magenta.game.block;

import com.magenta.render.TextureManager;

public class BlockType {
	// Block specs //
	private final int id;
	// private final BlocksEnum blockEnum;
	private final String name;
	private final String[] faces; // { top, bottom, sides }
	private final Model model;


	// public BlockType(String name, String[] faces, Model model, TextureManager texManager) {
	public BlockType(BlocksEnum blockEnum, TextureManager texManager) {
		// Block //
		this.id = blockEnum.getId();
		// this.blockEnum = blockEnum;
		this.name = blockEnum.getName();
		this.faces = blockEnum.getTextures();

		// Model //
		model = blockEnum.getModel(); // Just for init

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
		if(face > (model.vertexPositions.length) - 1) return;
		model.texCoords[face] = model.texCoords[face].clone();

		// Set block face
		for(int v=0; v < 4; v++) { // v -> vertex
			model.texCoords[face][v * 3 + 2] = texture; // Change texCord to adjust to the texture
			// This way on selecting a block the texture is alredy applied
		}
	}




	// Block // 
	public int getId() {
		return id;
	}

	public String[] getFaces() {
		return faces;
	}

	public String getFace(int index) {
		return "blocks/" + faces[index]; // Load from blocks/
	}


	// From Model //
	public float[][] getVertexPositions() {
		return model.vertexPositions;
	}

	public float[][] getTexCoords() {
		return model.texCoords;
	}

	public float[][] getShadingValues() {
	    return model.shadingValue;
	}

	public boolean isTransparent() {
		return model.isTransparent();
	}

	public boolean isCube() {
		return model.isCube();
	}

	public boolean isGlass() {
		return model.isGlass();
	}
}