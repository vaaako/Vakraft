package com.magenta.game.block;


public enum BlocksEnum {
	AIR(0, null, null),
	COBBLESTONE(1, "cobblestone", new String[]{"cobblestone"}),
	GRASS(2, "grass", new String[]{"grass", "dirt", "grass_side"}),
	FULLGRASS(3, "fullgrass", new String[]{"grass"}),
	DIRT(4, "dirt", new String[]{"dirt"}),
	STONE(5, "stone", new String[]{"stone"}),
	SAND(6, "sand", new String[]{"sand"}),
	PLANKS(7, "planks", new String[]{"planks"}),
	LOG(8, "log", new String[]{"log_top", "log_side"});
	
	// DAISY(9),
	// ROSE(10),
	// CACTUS(11),
	// DEAD_BUSH(12);

	private final int id;
	private String name;
	private String[] textures;


	BlocksEnum(int id, String name, String[] textures) {
		this.id = id;
		this.name = name;
		this.textures = textures;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String[] getTextures() {
		return textures;
	}

	public static BlocksEnum getBlockById(int id) {
		return BlocksEnum.values()[id];
	}
}
