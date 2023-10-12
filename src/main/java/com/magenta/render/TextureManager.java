package com.magenta.render;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

/**
 * Texture Array
 * - All textures must have the same width and height
 * 
 * */
public class TextureManager {
	private int texID;
	private final int width, height;
	private int texType = GL30.GL_TEXTURE_2D_ARRAY;

	private List<String> textures = new ArrayList<String>(); // Stored textures (to not load the same texture twice)

	public TextureManager(int width, int height, int max) {
		this.width = width;
		this.height = height;
		// Generates texture program
		texID = GL11.glGenTextures();

		// Assigns the texture to a Texture Unit
		GL11.glBindTexture(texType, texID); // texType

		// Apply texture filter on magnify and minify (linear/nearest)
		GL11.glTexParameteri(texType, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(texType, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

		// GL11.glTexParameteri(texType, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		// GL11.glTexParameteri(texType, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);


		// Assigns image to texture object
		GL15.glTexImage3D(texType, 0, GL11.GL_RGBA,
			width, height, max, 0,
			GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		// Data type of pixels -> Most commons -> GL_RGB (jpg/jpeg) and GL_RGBA (png),
	}

	public void generateMipmap() {	
		GL30.glGenerateMipmap(texType); // Generates mipmap -> Smaller versions of the same texture (used when texture is far away, for example)
	}

	private ByteBuffer loadTextureAsByte(String texture) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			STBImage.stbi_set_flip_vertically_on_load(true); // Inverte a imagem verticalmente (se necessário)
			// Store buffers
			IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
			IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
			IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);

			String texPath = "src/main/resources/textures/"+texture+".png";
			ByteBuffer texBuffer = STBImage.stbi_load(texPath, widthBuffer, heightBuffer, channelsBuffer, 0); // Loads
			STBImage.stbi_set_flip_vertically_on_load(true); // Image is loaded flipped by default
			if(texBuffer==null) throw new RuntimeException("Failed to load texture: " + texPath + " / " + STBImage.stbi_failure_reason());

			// System.out.println(widthBuffer.get(0) + "x" + heightBuffer.get(0));

			// Free memory
			// STBImage.stbi_image_free(texBuffer);

			return texBuffer;
		}
	}

	public void addTexture(String texture) {
		// Check if texture is added
		if(textures.contains(texture))
			return;
		textures.add(texture);

		// Loads image
		ByteBuffer texBuffer = loadTextureAsByte(texture);

		// Generate Image
		GL15.glBindTexture(texType, texID); // Make sure is binded

		// Pastes texture data where you want to be on texture array
		GL15.glTexSubImage3D(texType,
			0, 0, 0, textures.indexOf(texture),
			width, height, 1,
			GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, texBuffer);

		// Free memory
		STBImage.stbi_image_free(texBuffer);

		System.out.println("[-] Loaded Texture "+texture);
	}




	public void texUnit(ShaderProgram shader, String uniform, int unit) {
		int texUni = GL20.glGetUniformLocation(shader.getProgramID(), uniform); // Get uniform texture

		// Shader needs to be activated before changing the value of a uniform
		shader.use();
		GL20.glUniform1i(texUni, unit); // Use texture on unit number for texUnit
	}

	public void use() {
		GL15.glActiveTexture(GL15.GL_TEXTURE0);
		GL11.glBindTexture(texType, texID);
		// GL30.glUniform1i(texUni, unit);
	}

	public void unbind() {
		GL11.glBindTexture(texType, 0);
	}

	public void delete() {
		GL11.glDeleteTextures(texID);
	}

	public int getTexID() {
	    return texID;
	}

	public int getTexType() {
	    return texType;
	}

	public int getTextureIndex(String texture) {
		return textures.indexOf(texture);
	}
}