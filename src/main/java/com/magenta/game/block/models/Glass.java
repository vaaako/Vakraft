package com.magenta.game.block.models;

import com.magenta.game.block.Model;

public class Glass extends Model {
	public Glass() {
		super(true, true, true,
			new float[][] {
				{  0.5f,  0.5f,  0.5f,    0.5f, -0.5f,  0.5f,    0.5f, -0.5f, -0.5f,    0.5f,  0.5f, -0.5f }, // Right / X + 1
				{ -0.5f,  0.5f, -0.5f,   -0.5f, -0.5f, -0.5f,   -0.5f, -0.5f,  0.5f,   -0.5f,  0.5f,  0.5f }, // Left / X - 1
				{  0.5f,  0.5f,  0.5f,    0.5f,  0.5f, -0.5f,   -0.5f,  0.5f, -0.5f,   -0.5f,  0.5f,  0.5f }, // Top / Y + 1
				{ -0.5f, -0.5f,  0.5f,   -0.5f, -0.5f, -0.5f,    0.5f, -0.5f, -0.5f,    0.5f, -0.5f,  0.5f }, // Bottom / Y - 1
				{ -0.5f,  0.5f,  0.5f,   -0.5f, -0.5f,  0.5f,    0.5f, -0.5f,  0.5f,    0.5f,  0.5f,  0.5f }, // Front / Z + 1
				{  0.5f,  0.5f, -0.5f,    0.5f, -0.5f, -0.5f,   -0.5f, -0.5f, -0.5f,   -0.5f,  0.5f, -0.5f }  // Back / Z - 1
			}
		);	
	}
}