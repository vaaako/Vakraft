package com.magenta.game;

import java.util.Iterator;
import java.util.LinkedList;

import org.joml.Vector3f;

import com.magenta.render.mesh.Mesh;
import com.magenta.render.mesh.MeshLoader;

public class Subchunk {
	public final static int SUBCHUNK_WIDTH  = 4;
	public final static int SUBCHUNK_HEIGHT = 4;
	public final static int SUBCHUNK_LENGTH = 4;

	private LinkedList<Float> vertexPositions = new LinkedList<>();
	private LinkedList<Float> texCoords = new LinkedList<>();
	private LinkedList<Integer> indices = new LinkedList<>();
	private LinkedList<Float> shadingValues = new LinkedList<>();
	private int meshIndexCounter = 0;

	private Vector3f subchunkPosition;
	private final Vector3f localPosition, realPosition;

	private World world;	
	private Chunk parent;
	private int[][][] blocks;

	private Mesh mesh;

	public Subchunk(Chunk parent, Vector3f subchunkPosition) {
		this.parent = parent;
		this.subchunkPosition = subchunkPosition;
		
		this.world = parent.getWorld();	
		this.localPosition = new Vector3f(subchunkPosition).mul(SUBCHUNK_WIDTH, SUBCHUNK_HEIGHT, SUBCHUNK_LENGTH);
		this.realPosition = new Vector3f(subchunkPosition).add(localPosition.x, localPosition.y, localPosition.z);
	}



	public void updateMesh() {
		// Reset values
		vertexPositions.clear();
		texCoords.clear();
		indices.clear();
		shadingValues.clear();

		meshIndexCounter = 0;


		// Iterate throught all local block positions in the chunk
		for(int localX = 0; localX < SUBCHUNK_WIDTH; localX++) {	
			for(int localY = 0; localY < SUBCHUNK_HEIGHT; localY++) {
				for(int localZ = 0; localZ < SUBCHUNK_LENGTH; localZ++) {
					int parentX = (int) localPosition.x + localX;
					int parentY = (int) localPosition.y + localY;
					int parentZ = (int) localPosition.z + localZ;

					int blockNumber = this.parent.getBlocks()[parentX][parentY][parentZ];

					// Not air
					if(blockNumber > 0) {
						BlockType blockType = this.world.getBlockTypes().get(blockNumber);

						// Get the world-space position of the block
						float x = realPosition.x + localX;
						float y = realPosition.y + localY;
						float z = realPosition.z + localZ;

						// If block is cube, we want it to check neighbouring blocks so that we don't uselessly render faces
						// If block isn't a cube, we just want to render all faces, regardless of neighbouring blocks
						// Since the vast majority of blocks are probably anyway going to be cubes, this won't impact performance all that much; the amount of useless faces drawn is going to be minimal
						if(blockType.isCube()) {
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
						} else {
							for(int i = 0; i < blockType.getVertexPositions().length; i++) {
								addFace(i, blockType, x, y, z);
							}
						}
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



	public float[] getVertexPositions() {
	    return convertFloats(vertexPositions);
	}

	public int[] getIndices() {
	    return convertIntegers(indices);
	}

	public float[] getTexCoords() {
	    return convertFloats(texCoords);
	}

	public float[] getShadingValues() {
	    return convertFloats(shadingValues);
	}

	public int getMeshIndexCounter() {
	    return meshIndexCounter;
	}
}