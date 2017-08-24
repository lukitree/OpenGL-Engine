package com.lukitree.engine.framework.graphics;

import com.lukitree.engine.framework.asset.*;
import com.lukitree.engine.framework.graphics.shader.*;
import com.lukitree.engine.framework.object.*;
import com.lukitree.engine.framework.window.*;
import org.joml.*;

import java.lang.Math;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer
{
	private static final float FOV = 60.f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 20000.0f;

	private StaticShader entityShader = new StaticShader();
	private TerrainShader terrainShader = new TerrainShader();

	private EntityRenderer entityRenderer;
	private TerrainRenderer terrainRenderer;

	private Window window;
	private Vector2i windowSize = new Vector2i();
	private Matrix4f projectionMatrix;

	private Map<Model, List<Entity>> entities = new HashMap<>();
	private List<Terrain> terrains = new ArrayList<>();

	private boolean renderWireframe = false;
	private float[] skyColor = { 0.0f, 0.8f, 1.0f, 1.0f };

	public MasterRenderer(Window window)
	{
		System.out.println("OpenGL:\t" + glGetString(GL_VERSION));

		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		enableCulling();
		glClearColor(skyColor[0], skyColor[1], skyColor[2], skyColor[3]);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		this.window = window;
		this.projectionMatrix = new Matrix4f();
		syncToWindow();

		this.entityRenderer = new EntityRenderer(entityShader);
		this.terrainRenderer = new TerrainRenderer(terrainShader);
	}

	public static void enableCulling()
	{
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
	}

	public static void disableCulling()
	{
		glDisable(GL_CULL_FACE);
	}

	public void render(Light sun, Camera camera)
	{
		window.swapBuffers();
		glClearColor(skyColor[0], skyColor[1], skyColor[2], skyColor[3]);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		syncToWindow();

		entityShader.start();
		entityShader.loadSkyColor(skyColor[0], skyColor[1], skyColor[2]);
		entityShader.loadLight(sun);
		entityRenderer.render(entities, camera, projectionMatrix);
		entityShader.stop();

		terrainShader.start();
		terrainShader.loadSkyColor(skyColor[0], skyColor[1], skyColor[2]);
		terrainShader.loadLight(sun);
		terrainRenderer.render(terrains, camera, projectionMatrix);
		terrainShader.stop();

		entities.clear();
		terrains.clear();
	}

	public void processTerrain(Terrain terrain)
	{
		terrains.add(terrain);
	}

	public void processEntity(Entity entity)
	{
		Model model = entity.getModel();

		List<Entity> batch = entities.get(model);

		if (batch != null) batch.add(entity);
		else
		{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(model, newBatch);
		}
	}

	private void syncToWindow()
	{
		if (windowSize.equals(window.getSize())) return;

		windowSize = window.getSize();
		glViewport(0, 0, windowSize.x, windowSize.y);

		projectionMatrix.setPerspective((float)Math.toRadians(FOV), (float)window.getSize().x / window.getSize().y, NEAR_PLANE, FAR_PLANE);
	}

	public void close()
	{
		entityShader.close();
		terrainShader.close();
	}

	public void toggleWireFrame()
	{
		renderWireframe = !renderWireframe;
		if (renderWireframe) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		else glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}
}
