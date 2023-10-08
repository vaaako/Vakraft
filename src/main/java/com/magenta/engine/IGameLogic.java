package com.magenta.engine;

import com.magenta.engine.input.MouseInput;

public interface IGameLogic {
	void init(Window window) throws Exception;
	void input(Window window, MouseInput mouseInput);
	void update(float delta, MouseInput mouseInput, Window window);
	void render(Window window);
	void cleanup(Window window);
}