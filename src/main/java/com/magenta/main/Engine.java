package com.magenta.main;

import com.magenta.engine.IGameLogic;
import com.magenta.engine.Timer;
import com.magenta.engine.Window;
import com.magenta.engine.input.MouseInput;

public class Engine implements Runnable {
	private final Window window;
	private final IGameLogic gameLogic;
	
	private final Timer timer;
	private final MouseInput mouseInput;

	// private float last;
	private int frames = 0,
				ticks  = 0;
	String title = "Just testing around";

	public Engine(IGameLogic gameLogic) throws Exception {
		this.gameLogic = gameLogic;

		window = new Window(title, 640, 480, true);
		timer = new Timer();
		mouseInput = new MouseInput();

		frames = 0;
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
		gameLogic.init(window);
	}

	private void loop() {
		double lastTime = System.currentTimeMillis(); // To change title

		float deltaF = 0; // Frames
		float deltaT = 0; // Ticks	
		float now;
		timer.setLast(System.nanoTime());

		boolean running = true;
		while(running && !window.windowShouldClose()) {
			// Calculate elapsed time since the last frame
			now = System.nanoTime();
			deltaT += timer.getDeltaTick(now);
			deltaF += timer.getDeltaFrame(now);
			timer.setLast(now); // The last elapsed time is the now (this is logical)

			input();
			while(deltaT >= 1.0f) {
				update(deltaT);
				deltaT--;
				ticks++;
			}

			while(deltaF >= 1.0f) {
				render();
				deltaF--;
				frames++;
			}

			// if(!window.isvSync()) sync();

			// Display FPS each second
			// System.out.println(System.currentTimeMillis() - lastTime);
			// Show each 1 sec
			if(System.currentTimeMillis() - lastTime > 1000) {
				lastTime += 1000;
				window.setTitle(title + " - " + frames + " FPS" + " - " + ticks + " UDP");
				frames = 0;
				ticks  = 0;
			}
		}
	}

	private void sync() {
		float now = System.nanoTime(); 
		float loopSlot = 1.0f / timer.getTargetFPS();
		float end = timer.getDeltaFrame(now) + loopSlot;
		timer.setLast(now);

		while(timer.getLast() < end) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void input() {
		gameLogic.input(window, mouseInput);
	}

	private void update(float delta) {
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