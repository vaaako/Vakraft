package com.magenta.main;

import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.magenta.engine.Camera;
import com.magenta.engine.HitRay;
import com.magenta.engine.IGameLogic;
import com.magenta.engine.KeyboardInput;
import com.magenta.engine.Window;
import com.magenta.game.Chunk;
import com.magenta.game.World;
import com.magenta.game.block.BlocksEnum;
import com.magenta.engine.MouseInput;

public class Game implements IGameLogic {
	// Managers //
	private Renderer renderer;
	private Camera camera;
	private static MouseInput mouseInput;

	// World //
	private HitRay hitRay;
	private static World world;

	private final Vector3f cameraInc; // Movement
	private boolean movimentEnable = false,
					doubleSpeed =  false;

	private static int holdingBlock = 1;

	public Game() {
		cameraInc = new Vector3f();
	}

	@Override
	public void init(Window window, MouseInput mouseInput) throws Exception {
		Game.mouseInput = mouseInput; // To use on HitRay callback
		
		// Load camera
		camera = new Camera(window, 90.0f, 0.06f);

		// Load renderer
		renderer = new Renderer(window, camera);
		renderer.init();

		// Load world
		world = new World();

		// Load in world in chunk
		
		Vector3f firstPos = world.getChunkPosition(world.getChunks().entrySet().iterator().next().getKey());
		// camera.setPosition(firstPos.x, firstPos.y, firstPos.z);
		
		Vector3f lastPos = world.getChunkPosition(world.getChunks().keySet().stream().reduce((first, second) -> second).orElse(null));
		// camera.setPosition(lastPos.x, lastPos.y, lastPos.z);
	
		System.out.println("First Position: " + firstPos + "\nLast Position: " + lastPos);	
		// camera.setPosition(-36.0f, 1.0f, -33.0f);
		// camera.setPosition((Chunk.CHUNK_WIDTH + Chunk.CHUNK_HEIGHT) * -1, 1.0f, (Chunk.CHUNK_WIDTH + Chunk.CHUNK_HEIGHT) * -1);

		
		// window.setClearColor(1.0f, 0.5f, 1.0f, 1.0f);
		window.setClearColor(0.0f, 0.7f, 0.8f, 1.0f);
	}

	@Override
	public void input(Window window, KeyboardInput keyboardInput, MouseInput mouseInput) {
		if(keyboardInput.isPressingKey(GLFW.GLFW_KEY_ESCAPE))
			GLFW.glfwSetWindowShouldClose(window.getWindowHandle(), true);

		cameraInc.set(0, 0, 0); // Reset values

		// Movement //
		if(keyboardInput.isPressingKey(GLFW.GLFW_KEY_W))
			cameraInc.z = 1;
		else if(keyboardInput.isPressingKey(GLFW.GLFW_KEY_S))
			cameraInc.z = -1;

		if(keyboardInput.isPressingKey(GLFW.GLFW_KEY_A))
			cameraInc.x = -1;
		else if(keyboardInput.isPressingKey(GLFW.GLFW_KEY_D))
			cameraInc.x = 1;

		// Fly
		if(keyboardInput.isPressingKey(GLFW.GLFW_KEY_LEFT_SHIFT))
			cameraInc.y = -1;
		else if(keyboardInput.isPressingKey(GLFW.GLFW_KEY_SPACE))
			cameraInc.y = 1;

		// if(keyboardInput.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL))
		// 	doubleSpeed = !doubleSpeed;


		// Change block //
		/**
		 * Bug, below not workin because of the way isKeyPressed works
		 * Because it checks then pressed becomes -1, so is aways -1
		 * */

		// System.out.println("KEY: " + keyboardInput.getPressedKey());
		if(keyboardInput.isKeyPressed(GLFW.GLFW_KEY_E)) {
			holdingBlock++;
			if(world.getBlockTypes().size() <= holdingBlock) holdingBlock = 1;
		
			System.out.println("Holding Block: " + BlocksEnum.getBlockById(holdingBlock).getName());
		} else if(keyboardInput.isKeyPressed(GLFW.GLFW_KEY_Q)) {
			holdingBlock--;	
			if(holdingBlock < 0) holdingBlock = world.getBlockTypes().size() - 1;

			System.out.println("Holding Block: " + BlocksEnum.getBlockById(holdingBlock).getName());
		}



		// Exit from moviment //
		if(keyboardInput.isPressingKey(GLFW.GLFW_KEY_TAB) && movimentEnable) {	
			movimentEnable = false;
			GLFW.glfwSetInputMode(window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		}
	}

	@Override
	public void update(double delta, MouseInput mouseInput, Window window) {
		// Move with cameraInc
		camera.movePosition(cameraInc.x, cameraInc.y, cameraInc.z,
			(doubleSpeed) ? 2.0f : 1.0f);


		if(mouseInput.isLMBPressed()) {
			// Disable cursor, enable moviment
			if(!movimentEnable) {
				movimentEnable = true;
				GLFW.glfwSetInputMode(window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
				// GLFW.glfwSetInputMode(window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_CAPTURED);

				GLFW.glfwSetCursorPos(window.getWindowHandle(), (int) window.getWidth() / 2, (int) window.getHeight() / 2);
			}
		}

		// If can use mouse
		if(movimentEnable) {
			Vector2f rotVec = mouseInput.getMovement(); // Get the movement the cursor made
			camera.moveRotation(rotVec.x * camera.getSensitivity(), rotVec.y * camera.getSensitivity()); // Change camera rotation

			// Handle block breaking
			hitRay = new HitRay(world, new Vector3f(camera.getRotation()), new Vector3f(camera.getPosition()));
			while(hitRay.getDistance() < HitRay.HIT_RANGE) {
				if(hitRay.step(Game::HitCallback)) break;
			}
			// GLFW.glfwSetCursorPos(window.getWindowHandle(), (int) window.getWidth() / 2, (int) window.getHeight() / 2);
		}

		// Dev test
		// world.setBlock(camera.getPosition(), 7);
	}

	@Override
	public void render(Window window) {
		// System.out.println("X: " + position.x + " Y: " + position.y + " Z: " + position.z);
		// font.draw(10.0f, 10.0f, "X: " + position.x + " Y: " + position.y + " Z: " + position.z);
		renderer.render(world);
	}

	@Override
	public void cleanup(Window window) {
		// All below is after while loop break

		// Destroy window
		window.destroy();

		// Other
		renderer.delete(); // Shader program

		world.getTexManager().delete();
	}

	public static Integer HitCallback(Vector3f[] blocks) {
		// System.out.println("Ray block: " + world.getBlockNumber(blocks[1]));
		/**
		 * blocks[0] = current block
		 * blocks[1] = next block
		 * */

		if(mouseInput.isLMBPressed()) {
			mouseInput.releaseLMB(); // Force user to click multiple times

			world.setBlock(blocks[1], BlocksEnum.AIR.getId()); // Place air (remove)
		} else if(mouseInput.isMMBPressed()) {	
			mouseInput.releaseMMB();

			holdingBlock = world.getBlockInChunk(blocks[1]);
		} else if(mouseInput.isRMBPressed()) {
			mouseInput.releaseRMB();

			world.setBlock(blocks[0], holdingBlock);
		}

		return 0;	
	}
}
