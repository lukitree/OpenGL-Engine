package com.lukitree.engine.framework.graphics.shader;

import com.lukitree.engine.framework.asset.*;
import com.lukitree.engine.framework.object.*;
import org.joml.*;

public class StaticShader extends Shader
{
	final static int UNIFORM_MVP = 10;
	final static int UNIFORM_LIGHT_POSITION = 11;
	final static int UNIFORM_LIGHT_COLOR = 12;
	final static int UNIFORM_HAS_TEXTURE = 13;
	final static int UNIFORM_SHINE_DAMPER = 14;
	final static int UNIFORM_REFLECTIVITY = 15;
	final static int UNIFORM_MODEL_TRANSFORM = 16;
	final static int UNIFORM_VIEW_TRANSFORM = 17;
	final static int UNIFORM_PROJECTION_TRANSFORM = 18;
	final static int UNIFORM_AMBIENT_LIGHT_LEVEL = 19;
	final static int UNIFORM_USE_FAKE_LIGHTING = 20;
	final static int UNIFORM_SKY_COLOR = 21;

	private static final String VERTEX_FILENAME = "vertexShader.glsl";
	private static final String FRAGMENT_FILENAME = "fragmentShader.glsl";

	public StaticShader()
	{
		super(VERTEX_FILENAME, FRAGMENT_FILENAME);
		super.loadBoolean(UNIFORM_HAS_TEXTURE, false); // No texture loaded yet
	}

	public void loadModelTransform(Matrix4f matrix)
	{
		super.loadMatrix(UNIFORM_MODEL_TRANSFORM, matrix);
	}

	public void loadViewTransform(Matrix4f matrix)
	{
		super.loadMatrix(UNIFORM_VIEW_TRANSFORM, matrix);
	}

	public void loadProjectionTransform(Matrix4f matrix)
	{
		super.loadMatrix(UNIFORM_PROJECTION_TRANSFORM, matrix);
	}

	public void loadMVP(Matrix4f matrix)
	{
		super.loadMatrix(UNIFORM_MVP, matrix);
	}

	public void loadFakeLightingFlag(boolean use)
	{
		super.loadBoolean(UNIFORM_USE_FAKE_LIGHTING, use);
	}

	public void loadAmbientLightLevel(float level)
	{
		super.loadFloat(UNIFORM_AMBIENT_LIGHT_LEVEL, level);
	}

	public void loadTexture(Texture texture)
	{
		super.loadBoolean(UNIFORM_HAS_TEXTURE, true);
		super.loadTexture(texture.getTextureID());
		super.loadFloat(UNIFORM_SHINE_DAMPER, texture.getShineDamper());
		super.loadFloat(UNIFORM_REFLECTIVITY, texture.getReflectivity());
	}

	public void loadSkyColor(float r, float g, float b)
	{
		super.loadVector(UNIFORM_SKY_COLOR, new Vector3f(r,g,b));
	}

	public void loadLight(Light light)
	{
		super.loadVector(UNIFORM_LIGHT_POSITION, light.getPosition());
		super.loadVector(UNIFORM_LIGHT_COLOR, light.getColor());
	}
}
