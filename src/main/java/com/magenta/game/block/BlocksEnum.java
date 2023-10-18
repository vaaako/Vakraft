package com.magenta.game.block;

import com.magenta.game.block.models.Cactus;
import com.magenta.game.block.models.Cube;
import com.magenta.game.block.models.Glass;
import com.magenta.game.block.models.Plant;

public enum BlocksEnum {
	AIR(null, null, null),
	NOTFOUND("Not Found", // When query for a texture and not found, the default texture is the first added (aka this)
		new String[] { "notfound" }, new Cube()),
	
	COBBLESTONE("Cobblestone",
		new String[] { "cobblestone" }, new Cube()),
	GRASS("Grass",
		new String[] { "grass", "dirt", "grass_side" }, new Cube()),	
	FULLGRASS("Fullgrass",
		new String[] { "grass" }, new Cube()),	
	DIRT("Dirt",
		new String[] { "dirt" }, new Cube()),	
	STONE("Stone",
		new String[] { "stone" }, new Cube()),	
	SAND("Sand",
		new String[] { "sand" }, new Cube()),	
	PLANKS("Planks",
		new String[] { "planks" }, new Cube()),	
	LOG("Log",
		new String[] { "log_top", "log_side" }, new Cube()),	
	GLASS("Glass",
		new String[] { "glass" }, new Glass()),
	CACTUS("Cactus",
		new String[] { "cactus_top", "cactus_bottom", "cactus_side" }, new Cactus()),

	// Plants
	DAISY("Daisy",
		new String[] { "daisy" }, new Plant()),	
	ROSE("Rose",
		new String[] { "rose" }, new Plant()),
	DEAD_BUSH("Dead Bush",
		new String[] { "dead_bush" }, new Plant()),	
	BROWN_MUSHROOM("Brown Mushroom",
		new String[] { "brown_mushroom" }, new Plant()),
	RED_MUSHROOM("Red Mushroom",
		new String[] { "red_mushroom" }, new Plant()),
	AHIRO("Ahiro viado",
		new String[] { "ahiro" }, new Plant());

	private final String name;
	private final String[] textures;
	private final Model model;


	BlocksEnum(String name, String[] textures, Model model) {
		this.name = name;
		this.textures = textures;
		this.model = model;
	}

	public int getId() {
		return this.ordinal();
	}
	
	public String getName() {
		return name;
	}

	public String[] getTextures() {
		return textures;
	}

	public Model getModel() {
		return model;
	}

	public static BlocksEnum getBlockById(int id) {
		return BlocksEnum.values()[id];
	}
}
