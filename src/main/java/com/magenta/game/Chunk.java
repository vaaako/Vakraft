package com.magenta.game;

import java.util.Iterator;
import java.util.LinkedList;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.magenta.render.mesh.Mesh;
import com.magenta.render.mesh.MeshLoader;

public class Chunk {
	public final static int CHUNK_WIDTH = 16;
	public final static int CHUNK_HEIGHT = 16;
	public final static int CHUNK_LENGTH = 16;

	private LinkedList<Float> vertexPositions = new LinkedList<>();
	private LinkedList<Float> texCoords = new LinkedList<>();
	private LinkedList<Integer> indices = new LinkedList<>();
	private LinkedList<Float> shadingValues = new LinkedList<>();
	private int meshIndexCounter = 0;

	private Vector3f chunkPosition;
	private final Vector3f realPosition;

	private World world;
	private int[][][] blocks;

	private Mesh mesh;

	public Chunk(World world, Vector3f chunkPosition) {
		this.world = world;
		this.chunkPosition = chunkPosition;
		this.realPosition = new Vector3f(chunkPosition).mul(CHUNK_WIDTH, CHUNK_HEIGHT, CHUNK_LENGTH); // World-space position for the chunk
		this.blocks = new int[CHUNK_WIDTH][CHUNK_HEIGHT][CHUNK_LENGTH]; // 0 -> air (default)
	}

	public void updateMesh() {
		// Reset values
		vertexPositions.clear();
		texCoords.clear();
		indices.clear();
		shadingValues.clear();

		meshIndexCounter = 0;


		// Iterate throught all local block positions in the chunk
		for(int localX = 0; localX < CHUNK_WIDTH; localX++) {	
			for(int localY = 0; localY < CHUNK_HEIGHT; localY++) {
				for(int localZ = 0; localZ < CHUNK_LENGTH; localZ++) {
					int blockNumber = this.blocks[localX][localY][localZ];

					// System.out.println("Block Number: " + blockNumber + "\nSize: " + this.world.getBlockTypes().size());
					// Vector3f localPos = new Vector3f(localX, localY, localZ);

					// Not air
					if(blockNumber > 0) {
						BlockType blockType = this.world.getBlockTypes().get(blockNumber);

						// Get the world-space position of the block
						float x = realPosition.x + localX;
						float y = realPosition.y + localY;
						float z = realPosition.z + localZ;

						// Check for each block face, if it's hidden by another block, and add that face to the chunk mesh if not
						if(!world.isOpaqueBlock(x + 1, y, z))
							addFace(0, blockType, x, y, z);
						if(!world.isOpaqueBlock(x - 1, y, z))
							addFace(1, blockType, x, y, z);

						if(!world.isOpaqueBlock(x, y + 1, z))
							addFace(2, blockType, x, y, z);
						if(!world.isOpaqueBlock(x, y - 1, z))
							addFace(3, blockType, x, y, z);

						if(!world.isOpaqueBlock(x, y, z + 1))
							addFace(4, blockType, x, y, z);
						if(!world.isOpaqueBlock(x, y, z - 1))
							addFace(5, blockType, x, y, z);
					}
				}
			}
		}

		// Pass mesh data to gpu
		if(meshIndexCounter == 0) // Mak sure there actually is data in the mesh
			return;

		this.mesh = MeshLoader.createMesh(convertFloats(vertexPositions), convertIntegers(indices), convertFloats(texCoords), convertFloats(shadingValues));
	}

	// Add a face to the chunk mesh
	public void addFace(int face, BlockType block, float x, float y, float z) {
		float[] vertexPositions = block.getVertexPositions()[face].clone(); // Get vertex positions of the face to be added

		// Add the world-space position of the face to it's vertex positions
		for(int i = 0; i < 4; i++) {
			vertexPositions[i * 3 + 0] += x;
			vertexPositions[i * 3 + 1] += y;
			vertexPositions[i * 3 + 2] += z;
		}

		// Add those vertex positions to the chunk mesh's vertex positions
		for(int i = 0; i < vertexPositions.length; i++) {
			this.vertexPositions.add(i, vertexPositions[i]);
		}
	
		// Shift each index by the chunk mesh's index counter so that no two faces share
		int[] indices = new int[] { 0, 1, 2, 0, 2, 3 }; // Indices for the face's vertices
		for(int i = 0; i < indices.length; i++) {
			this.indices.add(i, indices[i] + meshIndexCounter);
		}
		meshIndexCounter += 4; // Add "the amount of vertices" in a face to the chunk

		// Add the face's texture coordinates to the chunk mesh's texture coordinates
		float[] texCoords = block.getTexCoords()[face];
		for(int i = 0; i < texCoords.length; i++) {
			this.texCoords.add(i, texCoords[i]);
		}

		// Add the face's shading values to the chunk mesh's shading values
		float[] shadingValues = block.getShadingValues()[face];
		for(int i = 0; i < shadingValues.length; i++) {
			this.shadingValues.add(i, shadingValues[i]);
		}
	}




	public int getBlock(int x, int y, int z) {
		int blockTypeID = blocks[x][y][z];

		if(blockTypeID == 0)
			return 0; // Return air

		BlockType blockType = world.getBlockTypes().get(blockTypeID);
		if(blockType.isTransparent())
			return 0;
		else
			return blockTypeID;
	}



	public int[][][] getBlocks() {
		return blocks;
	}

	public Vector3f getChunkPosition() {
		return chunkPosition;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public World getWorld() {
		return world;
	}



	private float[] convertFloats(LinkedList<Float> floats) {
		float[] ret = new float[floats.size()];
		Iterator<Float> iterator = floats.iterator();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = iterator.next();
		}
		return ret;
	}

	private int[] convertIntegers(LinkedList<Integer> integers) {
		int[] ret = new int[integers.size()];
		Iterator<Integer> iterator = integers.iterator();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = iterator.next();
		}
		return ret;
	}

	// Render chunk
	public void draw() {
		// Make sure there actually is data in mesh
		if(meshIndexCounter == 0)
			return;

		GL30.glBindVertexArray(mesh.getVaoID()); // Unloads VAO
		GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0); // Draw triangle	
	}
}