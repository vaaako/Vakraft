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

	private final int WORLD_SIZE = 8; // this * this = WORLD_SIZE

	public World() {
		texManager = new TextureManager(16, 16, 256);

		// blockTypes.add( new BlockType("notfound",    new String[] { "notfound" }, texManager) ); // Default texture
		blockTypes.add(0, null); // Id - 0	
		blockTypes.add(new BlockType("cobblestone", new String[] { "cobblestone" }, texManager) ); // Id - 1
		blockTypes.add(new BlockType("grass",       new String[] { "grass", "dirt", "grass_side" }, texManager) );
		blockTypes.add(new BlockType("fullgrass",       new String[] { "grass" }, texManager) );
		
		blockTypes.add(new BlockType("dirt",        new String[] { "dirt" }, texManager) );
		blockTypes.add(new BlockType("stone",       new String[] { "stone" }, texManager) );
		blockTypes.add(new BlockType("sand",        new String[] { "sand" }, texManager) );
		blockTypes.add(new BlockType("planks",      new String[] { "planks" }, texManager) );
		blockTypes.add(new BlockType("log",         new String[] { "log_top", "log_side" }, texManager) );
		// blockTypes.add(new BlockType("devblock",    new String[] { "dev/r", "dev/l", "dev/t", "dev/bb", "dev/f", "dev/b" }, texManager) );

		// blockTypes.add(new BlockType("daisy",       new String[] { "daisy" }, plant_model, texManager));
		// blockTypes.add(new BlockType("rose",        new String[] { "rose" }, plat_model, texManager));
		// blockTypes.add(new BlockType("cactus",      new String[] { "cactus_top", "cactups_bottom", "cactus_side" }, cactus_model, texManager));
		// blockTypes.add(new BlockType("dead_bush",      new String[] { "dead_bush" }, plant_model, texManager));

		texManager.generateMipmap();

		// int[] firstRnd = {0, 9, 10};
		int[] firstRnd = {0, 7, 8};
		Random random = new Random();

		// Generate chunk randomly
		for(int xw = 0; xw < WORLD_SIZE; xw++) { // xw -> X World
			for(int zw = 0; zw < WORLD_SIZE; zw++) {
				Chunk currentChunk = new Chunk(this, new Vector3f(xw - 4, -1, zw - 4));
				int[][][] blocks = currentChunk.getBlocks();

				for(int x = 0; x < Chunk.CHUNK_WIDTH; x++) {
					for(int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
						for(int z = 0; z < Chunk.CHUNK_LENGTH; z++) {
							if (y > 13) // Above Y=13 choose random in list of [ air, grass ] aka [0, 3] (block index 3)
								blocks[x][y][z] = (random.nextInt(2) == 0) ? 0 : 3;
							else // Bellow Y=13 choose random in list of [air, air, cooblestone]
								blocks[x][y][z] = (random.nextInt(3) == 2) ? 1 : 0;


							// if(y == 15)
							// 	blocks[x][y][z] = firstRnd[random.nextInt(firstRnd.length)];
							// else if(y == 14)
							// 	blocks[x][y][z] = 2;
							// else if(y > 10)
							// 	blocks[x][y][z] = 4;
							// else
							// 	blocks[x][y][z] = 5;
							//currentChunk.blocks[i][j][k] = 1;
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
		return new Vector3f(
			(float) Math.floor(position.x / Chunk.CHUNK_WIDTH),
			(float) Math.floor(position.y / Chunk.CHUNK_HEIGHT),
			(float) Math.floor(position.z / Chunk.CHUNK_LENGTH)
		);
	}

	public Vector3f getLocalPosition(Vector3f position) {
		Vector3f chunk = getChunkPosition(position);
		return new Vector3f(
			position.x - (chunk.x * Chunk.CHUNK_WIDTH),
			position.y - (chunk.y * Chunk.CHUNK_HEIGHT),
			position.z - (chunk.z * Chunk.CHUNK_LENGTH)
		);
	}

	// Get the index in the BlockManager array of the block at a certain position
	public int getBlockNumber(float x, float y, float z) {
		// Get the chunk in wich the block it's position
		// Vector3f chunkPosition = new Vector3f((float) Math.floor(x / Chunk.CHUNK_WIDTH), (float) Math.floor(y / Chunk.CHUNK_HEIGHT), (float) Math.floor(z / Chunk.CHUNK_LENGTH));
		Vector3f chunkPosition = getChunkPosition(new Vector3f(x, y, z));

		// Return "air" if the chunk doens't exist
		if(chunks.get(chunkPosition) == null)
			return 0;

		// Get the relative position of the block in the chunk
		Vector3f temp = getLocalPosition(new Vector3f(x, y, z));
		return chunks.get(chunkPosition).getBlock((int) temp.x, (int) temp.y, (int) temp.z);
		// return the block number at the local position in the correct chunk
	}

	// Get block type and check if it's opaque or not
	public boolean isOpaqueBlock(Vector3f position) {
		// Air counts as a transparent block, so test for that or not
		int blockNumber = getBlockNumber(position.x, position.y, position.z);	
		if(blockTypes.contains(blockNumber))
			return !blockTypes.get(blockNumber).isTransparent();
		return false;
	}

	public boolean isOpaqueBlock(float x, float y, float z) {
		return isOpaqueBlock(new Vector3f(x, y, z));
	}

	// Set number to 0 (air) to remove block
	public void setBlock(Vector3f position, int number) {
		Vector3f chunkPosition = getChunkPosition(position);
		
		// If no chunks exist at this position, create a new one
		Chunk currentChunk;
		if(!chunks.containsKey(chunkPosition)) {
			// No point in creating a whole new chunk if we're not gonna be adding anything
			if(number == 0) return;

			currentChunk = new Chunk(this, chunkPosition);
			chunks.put(chunkPosition, currentChunk);
		} else {
			currentChunk = chunks.get(chunkPosition);
		}

		

		// No point updating mesh if the block is the same
		int lastBlockNumber = getBlockNumber(position.x, position.y, position.z);
		if(lastBlockNumber == number) return;

		Vector3f localPosition = getLocalPosition(position);
		int lx = (int) localPosition.x;
		int ly = (int) localPosition.y;
		int lz = (int) localPosition.z;

		currentChunk.getBlocks()[lx][ly][lz] = number;

		int cx = (int) chunkPosition.x;
		int cy = (int) chunkPosition.y;
		int cz = (int) chunkPosition.z;

		// Check if position is located at chunk border
		if(lx == (Chunk.CHUNK_WIDTH - 1))
			tryUpdateChunkMesh(cx + 1, cy, cz);
		if(lx == 0)
			tryUpdateChunkMesh(cx - 1, cy, cz);

		if(ly == (Chunk.CHUNK_HEIGHT - 1))
			tryUpdateChunkMesh(cx, cy + 1, cz);
		if(ly == 0)
			tryUpdateChunkMesh(cx, cy - 1, cz);

		if(lz == (Chunk.CHUNK_LENGTH - 1))
			tryUpdateChunkMesh(cx, cy, cz + 1);
		if(lz == 0)
			tryUpdateChunkMesh(cx, cy, cz - 1);	

		currentChunk.updateMesh();
	}

	public void tryUpdateChunkMesh(float x, float y, float z) {
		Vector3f chunkPosition = new Vector3f(x, y, z);
		if(chunks.containsKey(chunkPosition))
			chunks.get(chunkPosition).updateMesh();
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
		for(Chunk chunk : chunks.values()) {
			chunk.draw();
		}
	}
}
