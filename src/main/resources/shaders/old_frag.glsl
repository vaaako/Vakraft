#version 330 core

// Outputs colors in RGBA
out vec4 FragColor;

// Inputs (import) the position from Vertex Shader
// in vec3 pos;

// Gets texture coords
in vec3 texCoord;

// Gets shading
in float shadingValue;

// Makes texture array uniform
uniform sampler2DArray texSampler;

void main() {
	// Creates color
	// FragColor = vec4(pos / 2.0 + 0.5, 1.0);
	// FragColor = texture(texSampler, vec3(0.5, 0.5, 1.0));
	// FragColor = texture(texSampler, texCoord);
	FragColor = texture(texSampler, texCoord) * shadingValue; // Shadow

	// Mix with color
	// FragColor = texture(texSampler, texCoord) * vec4(color(r, g, b), 1.0);
} 

/*
Low resolution
	// Adjust texture quality
	float texQuality = 8.0; // 8x8

	// Resize image
	vec2 scaledTexCoords = floor(texCoord.xy * texQuality) / texQuality;

	// Acess texture with scaled coords
	FragColor = texture(texSampler, vec3(scaledTexCoords, texCoord.z)) * shadingValue;
*/
