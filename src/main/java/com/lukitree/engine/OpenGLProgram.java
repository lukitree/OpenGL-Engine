package com.lukitree.engine;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public abstract class OpenGLProgram
{
	protected Window window;

	protected abstract void init();
	protected abstract void cleanup();
	protected abstract void update(float dt);
	protected abstract void render(float currentTime);

	public OpenGLProgram()
	{
		this.window = new Window(1024, 768, "OpenGL Program");
	}

	public void run()
	{
		float currentFrame = 0;
		float deltaTime = 0;
		float lastFrame = 0;

		init();
		while (!window.shouldClose())
		{
			currentFrame = (float) glfwGetTime();
			deltaTime = currentFrame - lastFrame;
			lastFrame = currentFrame;

			glfwPollEvents();
			update(deltaTime);

			glfwSwapBuffers(window.id());
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			render(currentFrame);
		}
		cleanup();
	}
}
