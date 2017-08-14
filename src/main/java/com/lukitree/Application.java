package com.lukitree;

import com.lukitree.engine.Loader;
import com.lukitree.engine.OpenGLProgram;
import com.lukitree.engine.Renderer;
import com.lukitree.engine.data.Model;

public class Application extends OpenGLProgram
{
	Loader loader = new Loader();
	Renderer renderer = new Renderer();

	Model model;

	@Override
	protected void init()
	{
		final float[] vertices =
				{
						-0.5f, 0.5f, 0.0f,
						-0.5f, -0.5f, 0.0f,
						0.5f, -0.5f, 0.0f,

						0.5f, -0.5f, 0.0f,
						0.5f, 0.5f, 0.0f,
						-0.5f, 0.5f, 0.0f,
				};

		model = loader.loadToVAO(vertices);
	}

	@Override
	protected void cleanup()
	{
		loader.cleanup();
	}

	@Override
	protected void update(float dt)
	{

	}

	@Override
	protected void render(float currentTime)
	{
		renderer.render(model);
	}

	public static void main(String[] args)
	{
		new Application().run();
	}
}
