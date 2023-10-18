package com.magenta.game;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.joml.Math;
import org.joml.Vector3f;

import com.magenta.engine.Timer;
import com.magenta.game.block.BlockLoader;
import com.magenta.game.block.BlockType;
import com.magenta.render.TextureManager;

public class World {
	private TextureManager texManager;
	private LinkedList<BlockType> blockTypes = new LinkedList<>();
	private Map<Vector3f, Chunk> chunks = new LinkedHashMap<>();

	private final int WORLD_SIZE = 8; // this * this = WORLD_SIZE
	private final int WORLD_CENTER = WORLD_SIZE / 2; // Used to spawn at the center of the world

	public World() {
		texManager = new TextureManager(16, 16, 256);
		System.out.println("Loading textures...");

		// Add blocks from file
		new BlockLoader("blocks.vk").loadContent(blockTypes, texManager); // Load all blocks from file
		texManager.generateMipmap();

		Timer timer = new Timer();
		System.out.println("\n\nGenerating world...");

		// Generate chunk randomly
		// generateChunk(0, 0);
		for(int wx = 0; wx < WORLD_SIZE; wx++) { // wx -> World's x
			for(int wz = 0; wz < WORLD_SIZE; wz++) {
				// float x = (float) Math.floor((wx - 1)) / Chunk.CHUNK_SIZE);
				// float z = (float) Math.floor((wz - 1)) / Chunk.CHUNK_SIZE);

				// Chunk currentChunk = new Chunk(this, new Vector3f(x, 0, z)); // Y: -1 = Start at -1 (camera spawns at Y:0)
				Chunk currentChunk = new Chunk(this, new Vector3f(wx - WORLD_CENTER, 0, wz - WORLD_CENTER)); // Y: -1 = Start at -1 (camera spawns at Y:0)
				chunks.put(currentChunk.getChunkPosition(), currentChunk); // Add to chunks list
			}
		}


		System.out.println("=> Generated world in: " + (float) timer.getElapsedTime() + " seconds");	
		System.out.println("\n=> Loading world...");

		// Load world (render all chunk meshes)
		for(Chunk chunk : chunks.values()) {
			chunk.updateMesh();
		}

		float elapsed = (float) timer.getElapsedTime();
		System.out.println("=> Loaded world in: " + elapsed + " seconds");
		System.out.println("Average: " + elapsed / chunks.size() + " per chunk\n");
	}
	
	private void loadChunk(int wx, int wz) {
		float x = (float) Math.floor((wx - 1) / Chunk.CHUNK_SIZE);
		float z = (float) Math.floor((wz - 1) / Chunk.CHUNK_SIZE);

		Chunk currentChunk = new Chunk(this, new Vector3f(x, 0, z)); // Y: -1 = Start at -1 (camera spawns at Y:0)
		chunks.put(currentChunk.getChunkPosition(), currentChunk); // Add to chunks list
	}

	private void generateChunk(int wx, int wz) {
		// if chunk loaded blablabla
		// else generate
	}

	// private Chunk getChunk(float x, float z) {
	// 	// Convert to chunk coordinates
	// 	float nx = (float) Math.floor((x - 1) / Chunk.CHUNK_SIZE);
	// 	float nz = (float) Math.floor((z - 1) / Chunk.CHUNK_SIZE);

	// 	Chunk chunk = chunks.get(nx);
	// 	if(chunk != null) return chunk.get(nz);
	// 	return null;
	// }









	public Vector3f getChunkPosition(Vector3f position) {
		return new Vector3f(
			(float) Math.floor(position.x / Chunk.CHUNK_SIZE),
			(float) Math.floor(position.y / Chunk.CHUNK_HEIGHT),
			(float) Math.floor(position.z / Chunk.CHUNK_SIZE)
		);
	}

	public Vector3f getLocalPosition(Vector3f position) {
		Vector3f chunk = getChunkPosition(position);
		return new Vector3f(
			position.x - (chunk.x * Chunk.CHUNK_SIZE),
			position.y - (chunk.y * Chunk.CHUNK_HEIGHT),
			position.z - (chunk.z * Chunk.CHUNK_SIZE)
		);
	}

	// Get the index in the BlockManager array of the block at a certain position
	public int getBlockInChunk(float x, float y, float z) {
		// Get the chunk in wich the block it's position
		Vector3f chunkPosition = getChunkPosition(new Vector3f(x, y, z));

		// Return "air" if the chunk doens't exist
		if(chunks.get(chunkPosition) == null)
			return 0;

		// Get the relative position of the block in the chunk
		Vector3f temp = getLocalPosition(new Vector3f(x, y, z));
		return chunks.get(chunkPosition).getBlock((int) temp.x, (int) temp.y, (int) temp.z);
		// return the block number at the local position in the correct chunk
	}

	public int getBlockInChunk(Vector3f position) {
		return getBlockInChunk(position.x, position.y, position.z);
	}

	// Get block type and check if it's opaque or not
	public boolean isOpaqueBlock(Vector3f position) {
		// Air counts as a transparent block, so test for that or not
		int blockTypeID = getBlockInChunk(position.x, position.y, position.z);
		if(blockTypes.get(blockTypeID) != null) // Block exists
			return !blockTypes.get(blockTypeID).isTransparent(); // Not transparent = Opaque
		return false; // Air
	}

	public boolean isOpaqueBlock(float x, float y, float z) {
		return isOpaqueBlock(new Vector3f(x, y, z));
	}


	// public int getBlock(Vector3f position) {
	// 	Vector3f chunkPosition = getChunkPosition(position);
		
	// 	// If no chunks exist at this position, create a new one
	// 	Chunk currentChunk;
	// 	if(!chunks.containsKey(chunkPosition)) {
	// 		currentChunk = new Chunk(this, chunkPosition);
	// 		chunks.put(chunkPosition, currentChunk);
	// 	} else {
	// 		currentChunk = chunks.get(chunkPosition);
	// 	}	

	// 	// No point updating mesh if the block is the same
	// 	Vector3f localPosition = getLocalPosition(position);
	// 	int lx = (int) localPosition.x;
	// 	int ly = (int) localPosition.y;
	// 	int lz = (int) localPosition.z;

	// 	return currentChunk.getBlocks()[lx][ly][lz];
	// }

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
		int lastBlockNumber = getBlockInChunk(position.x, position.y, position.z);
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
		if(lx == (Chunk.CHUNK_SIZE - 1))
			tryUpdateChunkMesh(cx + 1, cy, cz);
		if(lx == 0)
			tryUpdateChunkMesh(cx - 1, cy, cz);

		if(ly == (Chunk.CHUNK_SIZE - 1))
			tryUpdateChunkMesh(cx, cy + 1, cz);
		if(ly == 0)
			tryUpdateChunkMesh(cx, cy - 1, cz);

		if(lz == (Chunk.CHUNK_HEIGHT - 1))
			tryUpdateChunkMesh(cx, cy, cz + 1);
		if(lz == 0)
			tryUpdateChunkMesh(cx, cy, cz - 1);	

		currentChunk.updateMesh(); // Render updated block
	}

	public void tryUpdateChunkMesh(float x, float y, float z) {
		Vector3f chunkPosition = new Vector3f(x, y, z);
		if(chunks.containsKey(chunkPosition))
			chunks.get(chunkPosition).updateMesh();
	}



	// public int getBlockIDByName(String name) {
	// 	return trackBlocks.get(name);
	// }

	// public String getBlockNameByID(int id) {
	// 	return blockTypes.get(id).getName();
	// }

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
