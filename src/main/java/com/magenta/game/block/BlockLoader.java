package com.magenta.game.block;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

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
		String line;
		int lineNumber = 0;

		// Default values
		String name = "Unknown";
		String[] textures = { "unknown" };
		Model model = new Cube();

		try {
			while((line = reader.readLine()) != null) {
				if(line.startsWith("//") || line.isBlank()) {
					continue; // Skip comments and empty lines
				} else if(line.startsWith("null")) {
					blockTypes.add(0, null);
					lineNumber++;
					continue;
				}

				processLine(line, blockTypes, texManager, name, textures, model, lineNumber);
				lineNumber++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processLine(String line, LinkedList<BlockType> blockTypes, TextureManager texManager, String name, String[] textures, Model model, int lineNumber) {
		String[] props = line.split("-");
		for (String prop : props) {
			prop = prop.strip();
			String[] parts = prop.split(" ");
			
			if(parts[0].equals("name")) {
				name = extractStringInQuotes(prop);

			} else if(parts[0].equals("textures")) {
				textures = extractAllTextures(prop);

			} else if(parts[0].equals("model")) {
				model = extractModel(parts[1]);
				if(model == null) throw new RuntimeException("Model in line " + lineNumber + " doesn't exist");

			} else {
				throw new RuntimeException("Unexpected keyword" + parts[0] + " in file " + filename + " at line " + lineNumber);
			}
		}

		blockTypes.add(lineNumber, new BlockType(name, textures, model, texManager));
	}


	private String extractStringInQuotes(String input) {
		int start = input.indexOf("\"");
		int end = input.lastIndexOf("\"");
		if(start != -1 && end != -1 && start < end) {
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
		else if(stringModel.equals("Liquid"))
			return new Liquid();
		else if(stringModel.equals("Leaves"))
			return new Leaves();
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

				