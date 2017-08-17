package com.lukitree;

import com.lukitree.engine.*;
import com.lukitree.engine.data.*;
import com.lukitree.engine.loader.*;
import org.joml.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Application extends OpenGLProgram
{
	private Camera camera;
	private int shaderProgram;
	private float SPDMOD = 0.0f;

	private Texture kimEd1Texture;
	private Texture kimEd2Texture;
	private Texture cubeTexture;

	private Model cubeModel;
	private Model planeModel;

	private Entity cube;
	private Entity planeA;
	private Entity planeB;

	@Override
	protected void init()
	{
		camera = new Camera();
		Renderer.setCamera(camera);

		glClearColor(0.0f, 0.0f, 1.0f, 0.8f);

		shaderProgram = ProgramLoader.createProgram();
		glUseProgram(shaderProgram);

		kimEd1Texture = TextureLoader.loadTexture("K&E1.tga");
		kimEd2Texture = TextureLoader.loadTexture("K&E2.tga");
		cubeTexture = TextureLoader.loadTexture("cube.png");

		planeModel = ModelLoader.loadOBJ("plane.obj", kimEd1Texture);
		planeA = new Entity(planeModel);
		planeModel = ModelLoader.loadOBJ("plane.obj", kimEd2Texture);
		planeB = new Entity(planeModel);

		planeA.scale(0.4f);
		planeB.scale(0.4f);

		planeA.move(0.65f, 0.0f, 0.0f);
		planeB.move(-0.65f, 0.0f, 0.0f);

		cubeModel = ModelLoader.loadOBJ("cube.obj", cubeTexture);
		cube = new Entity(cubeModel);
		cube.scale(0.1f);
	}

	@Override
	protected void cleanup()
	{
		ModelLoader.close();
	}

	@Override
	protected void handleInput() { }

	@Override
	protected void update(float dt)
	{
		final float TRSPD = 1.0f * dt;
		final float ROSPD = 45.0f * dt;

		for (int key : getKeysPressed())
		{
			switch (key)
			{
				case GLFW_KEY_W:
					camera.move(0.0f, 0.0f, TRSPD);
					break;
				case GLFW_KEY_S:
					camera.move(0.0f, 0.0f, -TRSPD);
					break;
				case GLFW_KEY_A:
					camera.move(TRSPD, 0.0f, 0.0f);
					break;
				case GLFW_KEY_D:
					camera.move(-TRSPD, 0.0f, 0.0f);
					break;
				case GLFW_KEY_Q:
					camera.move(0.0f, TRSPD, 0.0f);
					break;
				case GLFW_KEY_E:
					camera.move(0.0f, -TRSPD, 0.0f);
					break;

				case GLFW_KEY_Z:
					camera.rotate(0.0f, -ROSPD, 0.0f);
					break;
				case GLFW_KEY_X:
					camera.rotate(0.0f, ROSPD, 0.0f);
					break;
				case GLFW_KEY_R:
					camera.rotate(-ROSPD, 0.0f, 0.0f);
					break;
				case GLFW_KEY_F:
					camera.rotate(ROSPD, 0.0f, 0.0f);
					break;

				case GLFW_KEY_SPACE:
					camera.reset();
					SPDMOD = 0.0f;
					break;
				case GLFW_KEY_UP:
					SPDMOD += 10.0f * dt;
					break;
				case GLFW_KEY_DOWN:
					SPDMOD -= 10.0f * dt;
					break;
			}
		}

		final float rotX = (30.0f * dt) * SPDMOD;
		final float rotY = (30.0f * dt) * SPDMOD;
		final float rotZ = (30.0f * dt) * SPDMOD;

		planeA.rotate(new Vector3f(rotX, rotY, rotZ));
		planeB.rotate(new Vector3f(rotX, -rotY, -rotZ));
		cube.rotate(new Vector3f(rotX, 0.0f, 0.0f));
	}

	@Override
	protected void render(float currentTime)
	{
		Renderer.render(planeA);
		Renderer.render(planeB);
		Renderer.render(cube);
	}

	public static void main(String[] args)
	{
		new Application().run();
	}
}
