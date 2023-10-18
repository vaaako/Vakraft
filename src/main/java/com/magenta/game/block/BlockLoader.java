package com.magenta.game.block;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.magenta.game.block.models.*;
import com.magenta.render.TextureManager;

public class BlockLoader {  
	private final String filename;
	private BufferedReader reader;


	public BlockLoader(String filename) {
		this.filename = filename;

		// Load file
		reader = new BufferedReader(new InputStreamReader(BlockLoader.class.getClassLoader().getResourceAsStream(""+filename)));
		if(reader == null) throw new RuntimeException("File: " + filename + " not found");
	}

	public void loadContent(LinkedList<BlockType> blockTypes, TextureManager texManager) {
		String sline;
		int line = 0;

		// Default value
		String name = "Unknown";
		String[] textures = null;
		Model model = null;

		try {
			while((sline = reader.readLine()) != null) {
				if(sline.startsWith("//") || sline.isBlank() || sline.isEmpty()) continue;
				else if(sline.startsWith("null")) {
					blockTypes.add(0, null);
					line++;
					continue;
				}

				for(String props : sline.split("-")) {
					props = props.strip();
					String[] prop = props.split(" ");

					if(prop[0].equals("name")) {
						name = extractStringInQuotes(props);

					} else if(prop[0].equals("textures")) {
						textures = extractAllTextures(props);

					} else if(prop[0].equals("model")) {
						// model = new eval(prop[1]);
						model = extractModel(prop[1]);
						if(model == null) throw new RuntimeException("Model in line " + line + "doesn't exists");

					} else {
						throw new RuntimeException("Unexpected keyword in file " + filename + " at line " + line + "\n> " + prop[0]);
					}
				}

				blockTypes.add(line, new BlockType(name, textures, model, texManager));
				line++;
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String extractStringInQuotes(String input) {
		int start = input.indexOf("\"");
		int end = input.lastIndexOf("\"");
		if (start != -1 && end != -1 && start < end) {
			return input.substring(start + 1, end);
		} else {
			return null; // or handle the case when there are no double quotes in the input
		}
	}

	private String[] extractAllTextures(String props) {
		String[] texTotal = props.replaceFirst("textures ", "").split(",");

		String[] textures = new String[texTotal.length];
		for(int i = 0; i < texTotal.length; i++)
			textures[i] = texTotal[i].replaceAll(" ", "");
		return textures;
	}

	private Model extractModel(String stringModel) {
		if(stringModel.equals("Cube"))
			return new Cube();
		else if(stringModel.equals("Plant"))
			return new Plant();
		else if(stringModel.equals("Cactus"))
			return new Cactus();
		else if(stringModel.equals("Glass"))
			return new Glass();
		else
			return null;

		// try {
		// 	Class<?> modelClass = Class.forName(prop[1]);
		// 	model = (Model) modelClass.getDeclaredConstructor().newInstance();
		// } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
		// 		| InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
		// 	// TODO Auto-generated catch block
		// 	e.printStackTrace();
		// }
	}
} 

				