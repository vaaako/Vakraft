package com.magenta.game;

import com.magenta.render.TextureManager;

public class BlockType {
	public final float[][] vertexPositions = { // 12
		{  0.5f,  0.5f,  0.5f,  0.5f, -0.5f,  0.5f,  0.5f, -0.5f, -0.5f,  0.5f,  0.5f, -0.5f }, // Right / X + 1
		{ -0.5f,  0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f,  0.5f, -0.5f,  0.5f,  0.5f }, // Left / X - 1
		{  0.5f,  0.5f,  0.5f,  0.5f,  0.5f, -0.5f, -0.5f,  0.5f, -0.5f, -0.5f,  0.5f,  0.5f }, // Top / Y + 1
		{ -0.5f, -0.5f,  0.5f, -0.5f, -0.5f, -0.5f,  0.5f, -0.5f, -0.5f,  0.5f, -0.5f,  0.5f }, // Bottom / Y - 1
		{ -0.5f,  0.5f,  0.5f, -0.5f, -0.5f,  0.5f,  0.5f, -0.5f,  0.5f,  0.5f,  0.5f,  0.5f }, // Front / Z + 1
		{  0.5f,  0.5f, -0.5f,  0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f,  0.5f, -0.5f }  // Back / Z - 1
	};

	public final float[][] texCoords = {
		{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f },
		{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f },
		{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f },
		{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f },
		{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f },
		{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f }
	};

	public final float[][] shadingValue = {
		{ 0.6f, 0.6f, 0.6f, 0.6f },
		{ 0.6f, 0.6f, 0.6f, 0.6f },
		{ 1.0f, 1.0f, 1.0f, 1.0f },
		{ 0.4f, 0.4f, 0.4f, 0.4f },
		{ 0.8f, 0.8f, 0.8f, 0.8f },
		{ 0.8f, 0.8f, 0.8f, 0.8f }
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

	// private final TextureManager texManager;
	// private Map<String, Block> blocks = new HashMap<>();

	public BlockType(String name, String[] faces, TextureManager texManager) {
		this.name = name;
		this.faces = faces;
		// this.texManager = texManager;

		int texIndex;

		// Load all block textures
		for(String face : faces) { // faces => This block textures (array)
			texManager.addTexture(face); // Adds current texture
			texIndex = texManager.getTextureIndex(face); // Texture index in All Textures array (grab image)
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
					int topFaceIndex = texManager.getTextureIndex(this.getFace(0));
					int bottomFaceIndex = texManager.getTextureIndex(this.getFace(1));

					setBlockFace(0, texIndex); // Right
					setBlockFace(1, texIndex); // Left

					setBlockFace(2, topFaceIndex); // Top
					setBlockFace(3, bottomFaceIndex); // Bottom

					setBlockFace(4, texIndex); // Front
					setBlockFace(5, texIndex); // Back
					break;
				case 6:
					int faceIndex;
					for(int i=0; i < 6; i++) {
						faceIndex = texManager.getTextureIndex(this.getFace(i));
						setBlockFace( i, faceIndex);
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
		return faces[index];
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
		return false;
	}

	public boolean isCube() {
		return true;
	}
}