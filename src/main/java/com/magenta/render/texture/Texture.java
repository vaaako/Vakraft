package com.magenta.render.texture;

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

public class Texture {
	private int texID;
	private final int width, height;
	private int texType = GL30.GL_TEXTURE_2D_ARRAY;

	public Texture(int width, int height, int max) {
		this.width = width;
		this.height = height;
		// Generates texture program
		texID = GL11.glGenTextures();

		// Assigns the texture to a Texture Unit
		GL11.glBindTexture(texType, texID); // texType

		// Apply texture filter on magnify and minify (linear/nearest)
		GL11.glTexParameteri(texType, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(texType, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

		// Assigns image to texture object
		GL15.glTexImage3D(texType, 0, GL11.GL_RGBA,
			width, height, max, 0,
			GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		// Data type of pixels -> Most commons -> GL_RGB (jpg/jpeg) and GL_RGBA (png),
	}


	// 2D texture (using with font)
	public Texture(int width, int height, ByteBuffer buffer) {
		this.texType = GL11.GL_TEXTURE_2D;
		this.width = width;
		this.height = height;

		// Generates texture program
		texID = GL11.glGenTextures();

		// Assigns the texture to a Texture Unit
		GL11.glBindTexture(texType, texID); // texType

		GL11.glTexParameteri(texType, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(texType, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(texType, GL11.GL_TEXTURE_WRAP_S, GL15.GL_CLAMP_TO_BORDER);
		GL11.glTexParameteri(texType, GL11.GL_TEXTURE_WRAP_T, GL15.GL_CLAMP_TO_BORDER);


		// Assigns image to texture object
		GL15.glTexImage2D(texType, 0, GL11.GL_RGBA,
			width, height, 0,
			GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		// Data type of pixels -> Most commons -> GL_RGB (jpg/jpeg) and GL_RGBA (png),
	}


	public void generateMipmap() {	
		GL30.glGenerateMipmap(texType); // Generates mipmap -> Smaller versions of the same texture (used when texture is far away, for example)
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

	// public int getTextureIndex(String texture) {
	// 	return textures.indexOf(texture);
	// }
}