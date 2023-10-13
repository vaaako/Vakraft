package com.magenta.engine;

public interface IGameLogic {
	void init(Window window, MouseInput mouseInput) throws Exception;
	void input(Window window, MouseInput mouseInput);
	void update(double delta, MouseInput mouseInput, Window window);
	void render(Window window);
	void cleanup(Window window);
}