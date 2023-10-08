package com.magenta.game.block;

import java.util.HashMap;
import java.util.Map;

import com.magenta.render.texture.TextureManager;

public class BlockManager {
	private final TextureManager texManager;
	private Map<String, Block> blocks = new HashMap<>();

	public BlockManager(TextureManager texManager) {
		this.texManager = texManager;

		addAllBlocks();
	}

	private void addAllBlocks() {
		/**
		 * N. of textures in the array:
		 * 1 -> Use texture to all sides
		 * 2 -> Top/Bottom and Sides
		 * 3 -> Top, Bottom and Sides
		 * 6 -> Right, Left, Top, Bottom, Front and Back
		 * */
		addBlock("notfound",    new String[] { "notfound" }); // Default texture
		addBlock("cobblestone", new String[] { "cobblestone" });
		addBlock("grass",       new String[] { "grass", "dirt", "grass_side" });
		addBlock("dirt",        new String[] { "dirt" });
		addBlock("stone",       new String[] { "stone" });
		addBlock("sand",        new String[] { "sand" });
		addBlock("planks",      new String[] { "planks" });
		addBlock("log",         new String[] { "log_top", "log_side" });

		addBlock("devblock",    new String[] { "dev/r", "dev/l", "dev/t", "dev/bb", "dev/f", "dev/b" });
		// addBlock("devblock",    new String[] { "dev/debug2", "dev/debug" });
	} 

	private void setBlockFace(Block block, int side, int texture) {
		// Set block face
		for(int v=0; v < 4; v++) { // v -> vertex
			block.texCoords[side * 12 + v * 3 + 2] = texture; // Change texCord to adjust to the texture
			// This way on selecting a block the texture is alredy applied
		}
	}


	public void addBlock(String name, String[] blockFaces) {
		Block newBlock = new Block(name, blockFaces);
		blocks.put(name, newBlock);

		int texIndex;

		// Load all block textures
		for(String face : blockFaces) { // blockFaces => This block textures (array)
			// texture = blockFaces[face]; // In old texture loader (python)
			texManager.addTexture(face);
			texIndex = texManager.getTextureIndex(face); // Texture index in All Textures array (grab image)
			// ^ gets current face to apply texture

			switch (blockFaces.length) {
				case 1:
					// Sides the same
					for(int i=0; i < 6; i++) {
						setBlockFace(newBlock, i, texIndex); // Front
					}
					break;
				case 2:
					int topAndBottomFaceIndex = texManager.getTextureIndex(newBlock.getFace(0));

					setBlockFace(newBlock, 0, texIndex); // Right
					setBlockFace(newBlock, 1, texIndex); // Left

					setBlockFace(newBlock, 2, topAndBottomFaceIndex); // Top
					setBlockFace(newBlock, 3, topAndBottomFaceIndex); // Bottom

					setBlockFace(newBlock, 4, texIndex); // Front
					setBlockFace(newBlock, 5, texIndex); // Back
					break;
				case 3:
					int topFaceIndex = texManager.getTextureIndex(newBlock.getFace(0));
					int bottomFaceIndex = texManager.getTextureIndex(newBlock.getFace(1));

					setBlockFace(newBlock, 0, texIndex); // Right
					setBlockFace(newBlock, 1, texIndex); // Left

					setBlockFace(newBlock, 2, topFaceIndex); // Top
					setBlockFace(newBlock, 3, bottomFaceIndex); // Bottom

					setBlockFace(newBlock, 4, texIndex); // Front
					setBlockFace(newBlock, 5, texIndex); // Back
					break;
				case 6:
					int faceIndex;
					for(int i=0; i < 6; i++) {
						faceIndex = texManager.getTextureIndex(newBlock.getFace(i));
						setBlockFace(newBlock, i, faceIndex);
					}
					break;
				default:
					throw new RuntimeException("Faces must have a lenght of 1, 3 or 6");
			}
			// System.out.println("[ ] Applyed Texture "+face+"\n");
		}

	}


	public Block getBlock(String name) {
		return blocks.get(name);
	}
}