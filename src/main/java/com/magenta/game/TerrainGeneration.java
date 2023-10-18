package com.magenta.game;

import java.util.Random;

// import com.magenta.engine.NoiseGenerator;
import com.magenta.game.noise.PerlinNoiseGenerator;

public class TerrainGeneration {
	private static final int WATER_HEIGHT = 28;  
	private final long seed;

	private final int CHUNK_SIZE;
	private final int CHUNK_HEIGHT;
   
	private double[][] heightMap;
	private final double offsetX;
	private final double offsetZ;

	private final PerlinNoiseGenerator noiseGenerator;

	private final Random random = new Random();
	public TerrainGeneration(int seed, int chunkSize, int chunkHeight) {
		this.seed = (seed == 0) ? random.nextInt(1000000) + 1 : seed; // 1 - 1000
		this.CHUNK_SIZE = chunkSize;
		this.CHUNK_HEIGHT = chunkHeight;
		this.heightMap = new double[CHUNK_SIZE][CHUNK_HEIGHT];

		Random random = new Random(this.seed);
		noiseGenerator = new PerlinNoiseGenerator(this.seed);


		// Noise offsets
		this.offsetX = random.nextDouble() * 1000000;
		this.offsetZ = random.nextDouble() * 1000000;
	}


	// Generate terrain height
	private int generateHeight(int wx, int wz) {
		double x = wx + offsetX;
		double z = wz + offsetZ;

		double height = 0;
		double frequency = 0.01;
		double amplitude = 1;
		double max = 0;

		// Perlin noise generation loop
		for(int i = 0; i < 8; i++) {
			height += noiseGenerator.noise(x * frequency, z * frequency) * amplitude;
			max += amplitude;
			amplitude /= 2;
			frequency *= 2;
		}

		double h = height / max;
		double e = Math.sin(h * Math.PI - Math.PI / 2) * 0.5 + 0.5;

		return (int) Math.floor((e * 0.5 + 0.5) * CHUNK_HEIGHT);
	}

	// Generate height map for a chunk
	private void generateHeightMap(int wx, int wz) {
		for (int i = 0; i < CHUNK_SIZE; i++) {
			for (int j = 0; j < CHUNK_SIZE; j++) {
				int x = wx + i - 1;
				int z = wz + j - 1;
				heightMap[i][j] = generateHeight(x, z);
			}
		}
	}

	private void carveCaves(int[][][] blocks) {
		for (int i = 0; i < CHUNK_SIZE; i++) {
			for (int j = 0; j < CHUNK_HEIGHT; j++) {
				for (int k = 0; k < CHUNK_SIZE; k++) {
					double noise = noiseGenerator.noise(i * 0.1, j * 0.1, k * 0.1);
					int height = (int) heightMap[i][k];


					if (noise > 0.6) { // Before = 0.6
						blocks[i][j][k] = 0;
					}
				}
			}
		}
	}

	private void addWater(int[][][] blocks) {
		for(int i = 0; i < CHUNK_SIZE; i++) {
			for(int k = 0; k < CHUNK_SIZE; k++) {
				int height = (int) heightMap[i][k];

				for (int j = height - 1; j <= WATER_HEIGHT; j++) {
					int block = blocks[i][j][k];
					if(block == 0)
						blocks[i][j][k] = 18; // Water
					else 
						blocks[i][j][k] = 7; // Sand
				}
			}
		}
	}

   
	private void addTrees(int[][][] blocks) {
		int amount = random.nextInt(3);
		for (int i = 0; i < amount; i++) {
			int x = random.nextInt(4, CHUNK_SIZE - 4);
			int z = random.nextInt(4, CHUNK_SIZE - 4);
			int height = (int) heightMap[x][z];
			int treeHeight = random.nextInt(3, 5);
			
			if (height > WATER_HEIGHT && height < CHUNK_HEIGHT - 10) {
				for (int j = 1; j <= treeHeight; j++) {
					blocks[x][height + j][z] = 9; // Wood log
				}
					
				// Add leaves around the tree trunk
				for (int a = -2; a <= 2; a++) {
					for (int b = -2; b <= 2; b++) {
						for (int j = 1; j <= 3; j++) {
							boolean shouldPlace = true;
							if ((a == 2 && b == -2 && j != 2) || (a == -2 && b == -2 && j != 2)) {
								shouldPlace = random.nextInt(6) == 0;
							}
							if (shouldPlace) {
								blocks[x + a][height + treeHeight + j][z + b] = 19; // Leaves
							}
						}
					}
				}
				
				// Add leaves on top of the tree trunk
				for (int a = -1; a <= 1; a++) {
					for (int b = -1; b <= 1; b++) {
						blocks[x + a][height + treeHeight + 4][z + b] = 19; // Leaves
					}
				}
			}
		}
	}

	private void addPlants(int[][][] blocks) {
		// int[] plants = { 15, 10, 11 }; // Plants
		int[] plants = { 10, 11 }; // Plants

		// For each block
		for (int i = 0; i < CHUNK_SIZE; i++) {
			for (int j = 0; j < CHUNK_HEIGHT; j++) {
				for (int k = 0; k < CHUNK_SIZE; k++) {

					// Block of grass and 5% chance
					if(blocks[i][j][k] == 3 && random.nextInt(100) <= 5) {
						// Spawn above grass
						blocks[i][j+1][k] = plants[random.nextInt(plants.length)];
					}
				}
			}
		}
	}

	public int[][][] generateChunk(int wx, int wz) {
		int[][][] blocks = new int[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE];

		generateHeightMap(wx, wz);

		for (int i = 0; i < CHUNK_SIZE; i++) {
			for (int j = 0; j < CHUNK_HEIGHT; j++) {
				for (int k = 0; k < CHUNK_SIZE; k++) {
					int height = (int) heightMap[i][k];
					double noise = noiseGenerator.noise(i * 0.1, j * 0.1, k * 0.1);
					double stoneThreshold = 24 + noise * 12;

					// 4 6 7 0 -> alternative
					if (j == height) {
						blocks[i][j][k] = 3; // Grass
					} else if (j < height && j > stoneThreshold) {
						blocks[i][j][k] = 5; // Dirt
					} else if (j < height) {
						blocks[i][j][k] = 6; // Stone
					} else {
						blocks[i][j][k] = 0;
					}
				}
			}
		}

		carveCaves(blocks);
		
		addWater(blocks);
		addTrees(blocks);
		
		addPlants(blocks);

		// Set bottom layer to bedrock
		for (int i = 0; i < CHUNK_SIZE; i++) {
			for (int k = 0; k < CHUNK_SIZE; k++) {
				blocks[i][0][k] = 1; // Bedrock
			}
		}

		return blocks;
	}

	public int[][][] bigBlock() {
		int[][][] blocks = new int[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE];
		int[] plants = { 0, 0, 10, 11 }; // Plants
		Random random = new Random();

		for(int x = 0; x < CHUNK_SIZE; x++) {
			for(int y = 0; y < CHUNK_HEIGHT; y++) {
				for(int z = 0; z < CHUNK_SIZE; z++) {
					// Big block chunk //
					// blocks[x][y][z] = BlocksEnum.COBBLESTONE.getId();
					
					// if (y > 13) // Above Y=13 choose random in list of [ air, grass ] aka [0, 3] (block index 3)
					//  blocks[x][y][z] = ((random.nextInt(2) == 0) ? BlocksEnum.AIR : BlocksEnum.GRASS).getId();
					// else // Bellow Y=13 choose random in list of [air, air, cooblestone]
					//  blocks[x][y][z] = ((random.nextInt(3) == 2) ? BlocksEnum.COBBLESTONE : BlocksEnum.AIR).getId();

					if(y > (int) CHUNK_HEIGHT / 2)
						blocks[x][y][z] = 6;
					else
						blocks[x][y][z] = 7;
					
					blocks[x][CHUNK_HEIGHT - 1][z] = plants[random.nextInt(plants.length)]; // Choose one of the flowers
					blocks[x][CHUNK_HEIGHT - 2][z] = 4; // Fill grass
					blocks[x][0][z] = 2; // Bedrock
				}
			}
		}
		return blocks;
	}
}