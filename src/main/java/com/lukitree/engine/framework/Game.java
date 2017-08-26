package com.lukitree.engine.framework;


import com.lukitree.engine.framework.window.*;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Game
{
	protected Window window = new GLFWWindow(1024, 768, "Game");

	protected abstract void init();
	protected abstract void cleanup();
	protected abstract void handleInput();
	protected abstract void update(float dt);
	protected abstract void render(float currentTime);

	public void run()
	{
		float currentFrame = 0;
		float deltaTime = 0;
		float lastFrame = 0;

		init();
		while (window.isOpen())
		{
			currentFrame = (float) glfwGetTime();
			deltaTime = currentFrame - lastFrame;
			lastFrame = currentFrame;

			glfwPollEvents();

			handleInput();
			update(deltaTime);
			render(currentFrame);
		}
		cleanup();
	}
}
