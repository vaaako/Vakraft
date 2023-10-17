package com.magenta.engine;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

public class Window {
	private String title;
	private int width, height;
	private long windowHandle;
	private boolean vSync;


	public Window(String title, int width, int height, boolean vSync) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.vSync = vSync;
	}

	public void initGLFW() {
		GLFWErrorCallback.createPrint(System.err).set(); // Errors to System.err
		
		if(!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
				
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		
		windowHandle = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
		if(windowHandle == NULL) throw new IllegalStateException("Unable to create GLFW Window");

		// Calback
		// On resizing window, adjust viewport
		GLFW.glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
			// Tells OpenGL size of rendering window
			System.out.println("Resizing to: " + width + "x" + height);

			// int aspect = width/height;
			// GL11.glViewport(0, 0, width * aspect, height * aspect); // Rendering startX, startX, width and height
			GL11.glViewport(0, 0, width, height); // Rendering startX, startX, width and height

			this.width = width;
			this.height = height;
		});


		// Get the thread stack and push a new frame
		// This is just to center the window
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*
			
			GLFW.glfwGetWindowSize(windowHandle, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			
			// Set window position to center of screen
			GLFW.glfwSetWindowPos(windowHandle,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
			
		} // The stack frame is popped automatically

		GLFW.glfwMakeContextCurrent(windowHandle); // Make the OpenGL context current
		
		if(vSync) GLFW.glfwSwapInterval(1); // Enable V-Sync
		GLFW.glfwShowWindow(windowHandle); // Make window visible

		// Begining of the start
		GL.createCapabilities(); // Finishes the initializing process	
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Set the clear color
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enable 3D depth
		GL11.glEnable(GL11.GL_CULL_FACE); // Don't show inside faces
	}


	// This //
	public long getWindowHandle() {
		return windowHandle;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isvSync() {
		return vSync;
	}

	public void setClearColor(float r, float g, float b, float alpha) {
		GL11.glClearColor(r, g, b, alpha);
	}

	public void setTitle(String title) {
		this.title = title;
		GLFW.glfwSetWindowTitle(windowHandle, title);
	}

	public void setvSync(boolean vSync) {
		this.vSync = vSync;
	}




	// GLFW //
	public boolean windowShouldClose() {
		return GLFW.glfwWindowShouldClose(windowHandle);
	}

	public void update() {
		GLFW.glfwSwapBuffers(windowHandle); // Swap front buffers to back buffers (clean screen basically)
		GLFW.glfwPollEvents(); // Window events (close, resize etc)
	}

	public void destroy() {
		// Free the window callbacks and destroy the window
		Callbacks.glfwFreeCallbacks(windowHandle);
		GLFW.glfwDestroyWindow(windowHandle);

		// Terminate GLFW and free the error callback
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}
}