package com.magenta.game;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import org.joml.Math;
import org.joml.Vector3f;

import com.magenta.engine.Timer;
import com.magenta.render.TextureManager;

public class World {
	private TextureManager texManager;
	private LinkedList<BlockType> blockTypes = new LinkedList<>();
	private Map<Vector3f, Chunk> chunks = new LinkedHashMap<>();

	private final int WORLD_SIZE = 8;

	public World() {
		texManager = new TextureManager(16, 16, 256);

		// blockTypes.add( new BlockType("notfound",    new String[] { "notfound" }, texManager) ); // Default texture
		blockTypes.add(0, null); // Id - 0	
		blockTypes.add(new BlockType("cobblestone", new String[] { "cobblestone" }, texManager) ); // Id - 1
		blockTypes.add(new BlockType("stone",       new String[] { "stone" }, texManager) );
		blockTypes.add(new BlockType("grass",       new String[] { "grass", "dirt", "grass_side" }, texManager) );
		blockTypes.add(new BlockType("fullgrass",       new String[] { "grass" }, texManager) );

		blockTypes.add(new BlockType("sand",        new String[] { "sand" }, texManager) );
		blockTypes.add(new BlockType("dirt",        new String[] { "dirt" }, texManager) );

		blockTypes.add(new BlockType("log",         new String[] { "log_top", "log_side" }, texManager) );
		blockTypes.add(new BlockType("planks",      new String[] { "planks" }, texManager) );
		// blockTypes.add(new BlockType("devblock",    new String[] { "dev/r", "dev/l", "dev/t", "dev/bb", "dev/f", "dev/b" }, texManager) );

		texManager.generateMipmap();

		Random random = new Random();

		// Generate chunk randomly
		for(int xw = 0; xw < WORLD_SIZE; xw++) { // xw -> X World
			for(int zw = 0; zw < WORLD_SIZE; zw++) {
				Chunk currentChunk = new Chunk(this, new Vector3f(xw - 4, -1, zw - 4));
				int[][][] blocks = currentChunk.getBlocks();

				for(int x = 0; x < Chunk.CHUNK_WIDTH; x++) {
					for(int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
						for(int z = 0; z < Chunk.CHUNK_LENGTH; z++) {
							if (y > 13) // Above Y=13 choose between block 0 (air), 1 (index 1 of blockTypes: cobblestone) or Grass
								blocks[x][y][z] = (random.nextInt(2) == 0) ? 0 : 3;
							else
								blocks[x][y][z] = (random.nextInt(3) == 2) ? 1 : 0;
						}
					}
				}

				chunks.put(currentChunk.getChunkPosition(), currentChunk); // Add to chunks list
			}
		}


		// Update each chunk's mesh
		Timer timer = new Timer();

		System.out.println("\n=> Loading world...");
		for(Chunk chunk : chunks.values()) {
			// System.out.println("Chunks size: " + chunks.values().size());
			chunk.updateMesh();
		}

		float elapsed = (float) timer.getElapsedTime();
		System.out.println("=> Loaded world in: " + elapsed + " seconds");
		System.out.println("Average: " + elapsed / chunks.size() + " per chunk\n");
	}


	public Vector3f getChunkPosition(Vector3f position) {
		return new Vector3f((float) Math.floor(position.x / Chunk.CHUNK_WIDTH), (float) Math.floor(position.y / Chunk.CHUNK_HEIGHT), (float) Math.floor(position.z / Chunk.CHUNK_LENGTH));
	}

	public Vector3f getLocalPosition(Vector3f position) {
		Vector3f temp = getChunkPosition(position);
		return new Vector3f(position.x - (temp.x * Chunk.CHUNK_WIDTH), position.y - (temp.y * Chunk.CHUNK_HEIGHT), position.z - (temp.z * Chunk.CHUNK_LENGTH));
	}

	// Get the index in the BlockManager array of the block at a certain position
	public int getBlockNumber(float x, float y, float z) {
		// Get the chunk in wich the block it's position
		Vector3f chunkPosition = new Vector3f((float) Math.floor(x / Chunk.CHUNK_WIDTH), (float) Math.floor(y / Chunk.CHUNK_HEIGHT), (float) Math.floor(z / Chunk.CHUNK_LENGTH));

		// Return "air" if the chunk doens't exist
		if(chunks.get(chunkPosition) == null)
			return 0;

		// Get the relative position of the block in the chunk
		Vector3f temp = getLocalPosition(new Vector3f(x, y, z));
		return chunks.get(chunkPosition).getBlock((int) temp.x, (int) temp.y, (int) temp.z);
		// return the block number at the local position in the correct chunk
	}




	public LinkedList<BlockType> getBlockTypes() {
		return blockTypes;
	}

	public Map<Vector3f, Chunk> getChunks() {
		return chunks;
	}

	public TextureManager getTexManager() {
		return texManager;
	}



	// Render all chunks
	public void render() {
		for(Chunk chunk : chunks.values())
			chunk.draw();
	}
}
