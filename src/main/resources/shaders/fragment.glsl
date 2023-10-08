#version 330 core

// Outputs colors in RGBA
out vec4 FragColor;

// Inputs (import) the position from Vertex Shader
in vec3 pos;

// Gets textura coords
in vec3 interpolatedTexCoords;

// Makes texture array uniform
uniform sampler2DArray texSampler;

void main() {
	// FragColor = vec4(color, 1.0f);

	// Creates color
	// FragColor = vec4(pos / 2.0 + 0.5, 1.0);
	// FragColor = texture(texSampler, vec3(0.5, 0.5, 1.0));
	FragColor = texture(texSampler, interpolatedTexCoords);
}
