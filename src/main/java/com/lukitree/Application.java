package com.lukitree;

import com.lukitree.engine.Loader;
import com.lukitree.engine.OpenGLProgram;
import com.lukitree.engine.Renderer;
import com.lukitree.engine.data.Model;
import com.lukitree.engine.loader.ShaderLoader;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Application extends OpenGLProgram
{
	Loader loader = new Loader();
	Renderer renderer = new Renderer();

	Model model;

	int shaderProgram;

	@Override
	protected void init()
	{
		glClearColor(0.0f, 0.0f, 1.0f, 0.8f);

		shaderProgram = ShaderLoader.createProgram();
		glUseProgram(shaderProgram);

		final float[] vertices =
				{
						-0.5f, 0.5f, 0.0f,
						-0.5f, -0.5f, 0.0f,
						0.5f, -0.5f, 0.0f,
						0.5f, 0.5f, 0.0f
				};

		final int[] indices =
				{
						0, 1, 3,
						3, 1, 2
				};

		final float[] texCoords =
				{
						0.0f, 1.0f,
						0.0f, 0.0f,
						1.0f, 0.0f,
						1.0f, 1.0f,
				};

		model = loader.loadToVAO(vertices, indices, "face.png", texCoords);
	}

	@Override
	protected void cleanup()
	{
		loader.cleanup();
	}

	@Override
	protected void update(float dt)
	{
		model.rotate(new Vector3f(0.1f * dt, 0.1f * dt, 0.1f * dt));
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
