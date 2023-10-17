package com.magenta.game.block;

import com.magenta.game.block.models.Cactus;
import com.magenta.game.block.models.Cube;
import com.magenta.game.block.models.Glass;
import com.magenta.game.block.models.Plant;

public enum BlocksEnum {
	AIR(0, null,
		null, null),
	NOTFOUND(1, "Not Found", // When query for a texture and not found, the default texture is the first added (aka this)
		new String[] { "notfound" }, new Cube()),
	
	COBBLESTONE(2, "Cobblestone",
		new String[] { "cobblestone" }, new Cube()),
	GRASS(3, "Grass",
		new String[] { "grass", "dirt", "grass_side" }, new Cube()),	
	FULLGRASS(4, "Fullgrass",
		new String[] { "grass" }, new Cube()),	
	DIRT(5, "Dirt",
		new String[] { "dirt" }, new Cube()),	
	STONE(6, "Stone",
		new String[] { "stone" }, new Cube()),	
	SAND(7, "Sand",
		new String[] { "sand" }, new Cube()),	
	PLANKS(8, "Planks",
		new String[] { "planks" }, new Cube()),	
	LOG(9, "Log",
		new String[] { "log_top", "log_side" }, new Cube()),	
	GLASS(10, "Glass",
		new String[] { "glass" }, new Glass()),
	CACTUS(11, "Cactus",
		new String[] { "cactus_top", "cactus_bottom", "cactus_side" }, new Cactus()),

	// Plants
	DAISY(12, "Daisy",
		new String[] { "daisy" }, new Plant()),	
	ROSE(13, "Rose",
		new String[] { "rose" }, new Plant()),
	DEAD_BUSH(14, "Dead Bush",
		new String[] { "dead_bush" }, new Plant()),	
	BROWN_MUSHROOM(15, "Brown Mushroom",
		new String[] { "brown_mushroom" }, new Plant()),
	RED_MUSHROOM(16, "Red Mushroom",
		new String[] { "red_mushroom" }, new Plant()),
	AHIRO(17, "Ahiro viado",
		new String[] { "ahiro" }, new Plant());

	private final int id;
	private String name;
	private String[] textures;
	private Model model;


	BlocksEnum(int id, String name, String[] textures, Model model) {
		this.id = id;
		this.name = name;
		this.textures = textures;
		this.model = model;
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

	public Model getModel() {
		return model;
	}

	public static BlocksEnum getBlockById(int id) {
		return BlocksEnum.values()[id];
	}
}
