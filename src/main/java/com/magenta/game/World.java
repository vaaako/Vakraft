package com.magenta.game;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import org.joml.Math;
import org.joml.Vector3f;

import com.magenta.engine.NoiseGenerator;
import com.magenta.engine.Timer;
import com.magenta.game.block.BlockType;
import com.magenta.game.block.BlocksEnum;
import com.magenta.render.TextureManager;

public class World {
	private TextureManager texManager;
	private LinkedList<BlockType> blockTypes = new LinkedList<>();
	private Map<Vector3f, Chunk> chunks = new LinkedHashMap<>();

	private final int WORLD_SIZE = 2; // this * this = WORLD_SIZE
	private final int WORLD_CENTER = WORLD_SIZE / 2; // Used to spawn at the center of the world

	public World() {
		texManager = new TextureManager(16, 16, 256);


		// Add blocks dynamically
		// Don't works properly yet
		blockTypes.add(BlocksEnum.AIR.getId(), null); // Id = 0	
		for(int i = 1; i < BlocksEnum.values().length; i++) {
			BlocksEnum blockEnum = BlocksEnum.values()[i];
			blockTypes.add(blockEnum.getId(), new BlockType(blockEnum, texManager) );
		}
		
		texManager.generateMipmap();

		// NoiseGenerator noiseGenerator = new NoiseGenerator();
		// int terrainHeight = (int)(noiseGenerator.noise(wx, wz)); // Adjust parameters as needed

		// Generate chunk randomly
		for(int xw = 0; xw < WORLD_SIZE; xw++) { // xw -> X World
			for(int zw = 0; zw < WORLD_SIZE; zw++) {
				Chunk currentChunk = new Chunk(this, new Vector3f(xw - WORLD_CENTER, -1, zw - WORLD_CENTER)); // Y: -1 = Start at -1 (camera spawns at Y:0)
				int[][][] blocks = currentChunk.getBlocks();
			
				generateChunk(blocks);
				chunks.put(currentChunk.getChunkPosition(), currentChunk); // Add to chunks list
			}
		}


		// Update each chunk's mesh
		Timer timer = new Timer();

		System.out.println("\n=> Loading world...");

		// Load world (render all chunk meshes)
		for(Chunk chunk : chunks.values()) {
			// System.out.println("Chunks size: " + chunks.values().size());
			chunk.updateMesh();
		}

		float elapsed = (float) timer.getElapsedTime();
		System.out.println("=> Loaded world in: " + elapsed + " seconds");
		System.out.println("Average: " + elapsed / chunks.size() + " per chunk\n");
	}


	public void generateChunk(int[][][] blocks) {
		int[] firstRnd = { BlocksEnum.AIR.getId(), BlocksEnum.AIR.getId(), BlocksEnum.DAISY.getId(), BlocksEnum.ROSE.getId() }; // Plants
		// int[] firstRnd = { BlocksEnum.AIR.getId(), BlocksEnum.CACTUS.getId(), BlocksEnum.DEAD_BUSH.getId() }; // Desert
		// int[] firstRnd = { BlocksEnum.AIR.getId(), BlocksEnum.RED_MUSHROOM.getId(), BlocksEnum.BROWN_MUSHROOM.getId() }; // Mushroom
		Random random = new Random();

		for(int x = 0; x < Chunk.CHUNK_WIDTH; x++) {
			for(int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
				for(int z = 0; z < Chunk.CHUNK_LENGTH; z++) {
					// Big block chunk //
					// blocks[x][y][z] = BlocksEnum.COBBLESTONE.getId();
					
					// if (y > 13) // Above Y=13 choose random in list of [ air, grass ] aka [0, 3] (block index 3)
					// 	blocks[x][y][z] = ((random.nextInt(2) == 0) ? BlocksEnum.AIR : BlocksEnum.GRASS).getId();
					// else // Bellow Y=13 choose random in list of [air, air, cooblestone]
					// 	blocks[x][y][z] = ((random.nextInt(3) == 2) ? BlocksEnum.COBBLESTONE : BlocksEnum.AIR).getId();

					// Flowers //
					if(y == 15)
						blocks[x][y][z] = firstRnd[random.nextInt(firstRnd.length)]; // Choose one of the flowers

					else if(y == 14)
						blocks[x][y][z] = BlocksEnum.GRASS.getId();
					else if(y > 10)
						blocks[x][y][z] = BlocksEnum.DIRT.getId();
					
					// else if(y > 10)
					// 	blocks[x][y][z] = BlocksEnum.SAND.getId();

					else
						blocks[x][y][z] = BlocksEnum.STONE.getId();
				}
			}
		}
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

		currentChunk.updateMesh(); // Render updated block
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
