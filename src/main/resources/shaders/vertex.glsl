#version 330 core

// Positions / Coodinates
layout (location = 0) in vec3 aPos;

// Texture
layout (location = 1) in vec3 texCoords;

// Outputs position for fragment shader
out vec3 pos;

// Outputs texture coords
out vec3 interpolatedTexCoords;

// Outputs the texture coordinates to the fragment shader
uniform mat4 camMatrix;

void main() {
	gl_Position = camMatrix * vec4(aPos, 1.0); // Sets position
	// Multiplying a matrix by a vecotr IS NOT the same as the opposite	

	// Export to fragment shader
	pos = aPos;
	interpolatedTexCoords = texCoords;
}