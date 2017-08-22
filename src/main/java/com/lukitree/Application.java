package com.lukitree;

import com.lukitree.engine.*;
import com.lukitree.engine.framework.asset.*;
import com.lukitree.engine.framework.asset.manager.*;
import com.lukitree.engine.framework.asset.manager.enums.*;
import com.lukitree.engine.framework.graphics.*;
import com.lukitree.engine.framework.asset.loader.*;
import com.lukitree.engine.framework.object.*;
import com.lukitree.engine.framework.utility.*;
import com.lukitree.engine.framework.window.event.*;
import org.joml.*;

import java.lang.*;
import java.lang.Math;
import java.lang.String;
import java.util.*;

public class Application extends Game
{
	private MasterRenderer renderer;
	private ModelLoader modelLoader = new ModelLoader();
	private TextureLoader textureLoader = new TextureLoader();
	private Camera camera = new Camera();

	private Light sun = new Light(new Vector3f(0, 5000, 5000), Color.WHITE);

	private TextureManager textures = new TextureManager();
	private ModelManager models = new ModelManager(textures);

	private Entity cube = new Entity(models.get(Models.CUBE));
	private Entity earth = new Entity(models.get(Models.EARTH));
	private Entity dragon = new Entity(models.get(Models.DRAGON));
	private Entity sunRep = new Entity(models.get(Models.SUN));

	private Terrain terrain = new Terrain(0,0, modelLoader, textures.get(Textures.GRASS));
	private Terrain terrain2 = new Terrain(-1,0, modelLoader, textures.get(Textures.GRASS));

	private Map<Key, KeyEvent> keysDown = new HashMap<>();

	private List<Entity> bunchaCubes = new ArrayList<>();
	private List<Entity> grass = new ArrayList<>();
	private List<Entity> trees = new ArrayList<>();

	private float timeMultiplier = 1;
	float totalTime = 0;
	boolean menu = false;

	@Override
	protected void init()
	{
		window.hideCursor(true);
		renderer = new MasterRenderer(window);

		cube.scale(100);
		cube.setPosition(400, 200, 1000);

		earth.scale(100);
		earth.setPosition(-400, 200, 1000);

		dragon.scale(10);
		dragon.setPosition(0,0, 600);

		sunRep.scale(100);

		camera.setPosition(0, 10.f, 400);

		for(int i = 0; i < 50; ++i)
		{
			Entity entity = new Entity(models.get(Models.CUBE));

			final int DIST = 100;

			float x = (float)Math.random() * DIST * 16 - DIST * 8;
			float z = (float)Math.random() * DIST * 8 + 5;
			entity.setPosition(x,0,z);

			float y = (float)Math.random() * 360;
			entity.setRotation(0,y,0);
			entity.scale(3);
			entity.setOrigin(0,-1,0);
			bunchaCubes.add(entity);
		}

		generateTerrain();
	}

	private void generateTerrain()
	{
		final int DIST = 800;

		// Generate grass
		final int PATCH_COUNT = 25000;
		final int PATCH_UNIT_COUNT = 5;
		final int PATCH_UNIT_FACES_COUNT = 3;
		final int PATCH_SIZE = 10;

		for(int i = 0; i < PATCH_COUNT; ++i)
		{

			float groupX = (float)Math.random() * (DIST * 2) - DIST;
			float groupZ = (float)Math.random() * DIST;

			final int UNIT_COUNT = (int)(Math.random() * PATCH_UNIT_COUNT + PATCH_UNIT_COUNT / 2);
			for(int j = 0; j < UNIT_COUNT; ++j)
			{
				float posX = groupX + (float)Math.random() * PATCH_SIZE - PATCH_SIZE / 2;
				float posZ = groupZ + (float)Math.random() * PATCH_SIZE - PATCH_SIZE / 2;
				float rotY = (float)Math.random() * 360;

				final int FACES = (int)(Math.random() * PATCH_UNIT_FACES_COUNT + 1);
				for(int k = 0; k < FACES; ++k)
				{
					Entity bush = new Entity(models.get(Models.GRASS));
					bush.setPosition(posX, 0, posZ);
					bush.rotate(0, rotY + (float)Math.random() * 360,0);
					grass.add(bush);
				}
			}
		}

		// Generate trees
		final int TREE_COUNT = 500;

		for(int i = 0; i < TREE_COUNT; ++i)
		{
			float posX = (float)Math.random() * (DIST * 2) - DIST;
			float posZ = (float)Math.random() * DIST;
			float rotY = (float)Math.random() * 360;

			Entity tree = new Entity(models.get(Models.TREE));
			tree.scale((float)Math.random() * 5 + 1.5f);
			tree.setRotation(0, rotY, 0);
			tree.setPosition(posX, 0, posZ);
			trees.add(tree);
		}
	}

	@Override
	protected void cleanup()
	{
		modelLoader.close();
		textureLoader.close();
		renderer.close();
	}

	@Override
	protected void handleInput()
	{
		Event ev = new Event();
		while(window.pollEvents(ev))
		{
			switch(ev.type)
			{
				case Closed:
					window.close();
					break;
				case KeyPressed:
					keysDown.put(ev.key.code, ev.key);
					switch(ev.key.code)
					{
						case ESCAPE:
							//window.close();
							menu = !menu;
							window.hideCursor(!menu);
							break;

						case W:
							camera.move(Camera.Move.FOWARD, true);
							break;
						case A:
							camera.move(Camera.Move.LEFT, true);
							break;
						case S:
							camera.move(Camera.Move.BACKWARD, true);
							break;
						case D:
							camera.move(Camera.Move.RIGHT, true);
							break;
						case Z:
							camera.move(Camera.Move.DOWN, true);
							break;
						case SPACE:
							camera.move(Camera.Move.UP, true);
							break;

						case F1:
							renderer.toggleWireFrame();
							break;
						case F2:
							camera.setPosition(sun.getPosition());
							break;
					}
					break;
				case KeyReleased:
					keysDown.remove(ev.key.code);
					switch(ev.key.code)
					{
						case W:
							camera.move(Camera.Move.FOWARD, false);
							break;
						case A:
							camera.move(Camera.Move.LEFT, false);
							break;
						case S:
							camera.move(Camera.Move.BACKWARD, false);
							break;
						case D:
							camera.move(Camera.Move.RIGHT, false);
							break;
						case Z:
							camera.move(Camera.Move.DOWN, false);
							break;
						case SPACE:
							camera.move(Camera.Move.UP, false);
							break;
					}
					break;
				case MouseMoved:
					if(!menu) camera.rotate(ev.mouseMove.y, ev.mouseMove.x, 0);
					break;
			}
		}
	}

	@Override
	protected void update(float dt)
	{
		float dtMult = dt * timeMultiplier;
		totalTime += dtMult * 10;
		camera.update(dt);

		KeyEvent timeButton = keysDown.get(Key.TAB);
		if(timeButton != null)
		{
			if(timeButton.shift && timeButton.control) timeMultiplier = 1;
			else if(timeButton.control) timeMultiplier = 0;
			else if(timeButton.shift) timeMultiplier -= 5 * dt;
			else timeMultiplier += 5 * dt;
		}

		final float rotX = 30.0f * dtMult;
		final float rotY = 60.0f * dtMult;
		final float rotZ = 90.0f * dtMult;

		cube.rotate(rotX, rotY, rotZ);
		earth.rotate(0, rotY / 2, 0);

		Vector3f sunPath = new Vector3f();
		sunPath.y = (float)Math.cos(Math.toRadians(totalTime)) * 9000;
		sunPath.x = (float)Math.sin(Math.toRadians(totalTime)) * 9000;
		sunPath.z = 400;
		sun.setPosition(sunPath);
		sunRep.setPosition(sunPath);
		sunRep.rotate(0, 20.f * dtMult, 0);
	}

	@Override
	protected void render(float currentTime)
	{
		renderer.processTerrain(terrain);
		renderer.processTerrain(terrain2);

		for(Entity el : grass)
		{
			if(el.getDistance(camera.getPosition()) > 150) continue;
			renderer.processEntity(el);
		}

		for(Entity el : trees)
		{
			renderer.processEntity(el);
		}

		renderer.processEntity(cube);
		renderer.processEntity(earth);
		renderer.processEntity(dragon);
		renderer.processEntity(sunRep);

		for(Entity el : bunchaCubes)
		{
			renderer.processEntity(el);
		}

		renderer.render(sun, camera);
	}

	public static void main(String[] args)
	{
		new Application().run();
	}
}
