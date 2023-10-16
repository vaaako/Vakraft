package com.magenta.game.block.models;

import com.magenta.game.block.Model;

public class Plant extends Model {
	public Plant() {
		super(true, false, false,
			new float[][] {
				{ -0.3536f, 0.5000f,  0.3536f,   -0.3536f, -0.5000f,  0.3536f,    0.3536f, -0.5000f, -0.3536f,    0.3536f, 0.5000f, -0.3536f },
				{ -0.3536f, 0.5000f, -0.3536f,   -0.3536f, -0.5000f, -0.3536f,    0.3536f, -0.5000f,  0.3536f,    0.3536f, 0.5000f,  0.3536f },
				{  0.3536f, 0.5000f, -0.3536f,    0.3536f, -0.5000f, -0.3536f,   -0.3536f, -0.5000f,  0.3536f,   -0.3536f, 0.5000f,  0.3536f },
				{  0.3536f, 0.5000f,  0.3536f,    0.3536f, -0.5000f,  0.3536f,   -0.3536f, -0.5000f, -0.3536f,   -0.3536f, 0.5000f, -0.3536f },
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
				{ 1.0f, 1.0f, 1.0f, 1.0f },
				{ 1.0f, 1.0f, 1.0f, 1.0f },
				{ 1.0f, 1.0f, 1.0f, 1.0f },
				{ 1.0f, 1.0f, 1.0f, 1.0f },
			}
		);
	}

}