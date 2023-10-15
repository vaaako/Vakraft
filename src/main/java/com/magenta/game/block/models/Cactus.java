package com.magenta.game.block.models;

import com.magenta.game.block.Model;

public class Cactus extends Model {
	public Cactus() {
		super(true, false,
			new float[][] {
				{  0.4375f,  0.5000f,  0.5000f,  0.4375f, -0.5000f,  0.5000f,  0.4375f, -0.5000f, -0.5000f,  0.4375f,  0.5000f, -0.5000f }, // Right
				{ -0.4375f,  0.5000f, -0.5000f, -0.4375f, -0.5000f, -0.5000f, -0.4375f, -0.5000f,  0.5000f, -0.4375f,  0.5000f,  0.5000f }, // Left
				{  0.5000f,  0.5000f,  0.5000f,  0.5000f,  0.5000f, -0.5000f, -0.5000f,  0.5000f, -0.5000f, -0.5000f,  0.5000f,  0.5000f }, // Top
				{ -0.5000f, -0.5000f,  0.5000f, -0.5000f, -0.5000f, -0.5000f,  0.5000f, -0.5000f, -0.5000f,  0.5000f, -0.5000f,  0.5000f }, // Bottom
				{ -0.5000f,  0.5000f,  0.4375f, -0.5000f, -0.5000f,  0.4375f,  0.5000f, -0.5000f,  0.4375f,  0.5000f,  0.5000f,  0.4375f }, // Front
				{  0.5000f,  0.5000f, -0.4375f,  0.5000f, -0.5000f, -0.4375f, -0.5000f, -0.5000f, -0.4375f, -0.5000f,  0.5000f, -0.4375f }, // Back
			},

			new float[][] {
				{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f },
				{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f },
				{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f },
				{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f },
				{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f },
				{ 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f }
			},

			new float[][] {
				{ 0.6f, 0.6f, 0.6f, 0.6f },
				{ 0.6f, 0.6f, 0.6f, 0.6f },
				{ 1.0f, 1.0f, 1.0f, 1.0f },
				{ 0.4f, 0.4f, 0.4f, 0.4f },
				{ 0.8f, 0.8f, 0.8f, 0.8f },
				{ 0.8f, 0.8f, 0.8f, 0.8f },
			}
		);
	}

}