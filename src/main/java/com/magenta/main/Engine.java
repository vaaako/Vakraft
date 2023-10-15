package com.magenta.main;

import com.magenta.engine.IGameLogic;
import com.magenta.engine.KeyboardInput;
import com.magenta.engine.Timer;
import com.magenta.engine.Window;
import com.magenta.engine.MouseInput;

public class Engine implements Runnable {
	private final Window window;
	private final IGameLogic gameLogic;

	private final Timer timer;
	private final MouseInput mouseInput;
	private final KeyboardInput keyboardInput;

	private int frames = 0,
				ticks  = 0;
	private final double TIME_PER_UPDATE;
	private final int TARGET_FPS = 60;

	String title = "Just testing around";

	public Engine(IGameLogic gameLogic) throws Exception {
		this.gameLogic = gameLogic;

		// window = new Window(title, 640, 480, true);
		window = new Window(title, 800, 600, true);
		mouseInput = new MouseInput();
		keyboardInput = new KeyboardInput(window);

		timer = new Timer();
		TIME_PER_UPDATE = 1.0 / TARGET_FPS;
	}

	@Override
	public void run() { // Throws exception from gameLogic
		try {
			init();
			loop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			cleanup();
		}
	}

	private void init() throws Exception {
		// Init All
		window.initGLFW();
		mouseInput.init(window);
		keyboardInput.init();
		gameLogic.init(window, mouseInput);
	}

	private void loop() {
		double deltaTime;
		double unprocessed = 0;
		double lastFps = timer.getTime(); // To display FPS

		while(!window.windowShouldClose()) {
			// Calculate elapsed time since the last frame
			deltaTime = timer.getElapsedTime();
			unprocessed += deltaTime;


			input(); // Process input
			while(unprocessed >= TIME_PER_UPDATE) {
				update(TIME_PER_UPDATE);
				unprocessed -= TIME_PER_UPDATE;
				ticks++;
			}

			render();
			frames++;

			if(!window.isvSync()) sync();
			// System.out.println(timer.getCurrentTime() - timer.getLast());

			// Updte FPS display each second
			if(timer.getLast() - lastFps >= 1) {
				lastFps = timer.getLast();
				window.setTitle(title + " - " + frames + " FPS" + " - " + ticks + " UDP");
				frames = 0;
				ticks  = 0;
			}
		}
	}

	private void sync() {
		double end = timer.getLast() + TIME_PER_UPDATE; // last + loopSlot

		while(timer.getTime() < end) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void input() {
		gameLogic.input(window, keyboardInput, mouseInput);
	}

	private void update(double delta) {
		mouseInput.input();
		gameLogic.update(delta, mouseInput, window);
	}

	private void render() {
		gameLogic.render(window);
	}

	private void cleanup() {
		System.out.println("-= CLEAN UP =-");
		gameLogic.cleanup(window);
	}
}
