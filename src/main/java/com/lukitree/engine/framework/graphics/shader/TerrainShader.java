package com.lukitree.engine.framework.graphics.shader;

import com.lukitree.engine.framework.asset.*;
import com.lukitree.engine.framework.object.*;
import org.joml.*;

public class TerrainShader extends Shader
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

	private static final String VERTEX_FILENAME = "terrainVertexShader.glsl";
	private static final String FRAGMENT_FILENAME = "terrainFragmentShader.glsl";

	private boolean textureLoaded = false;

	public TerrainShader()
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

	public void loadTexture(Texture texture)
	{
		super.loadBoolean(UNIFORM_HAS_TEXTURE, true);
		super.loadTexture(texture.getTextureID());
		super.loadFloat(UNIFORM_SHINE_DAMPER, texture.getShineDamper());
		super.loadFloat(UNIFORM_REFLECTIVITY, texture.getReflectivity());
		textureLoaded = true;
	}

	public void loadLight(Light light)
	{
		super.loadVector(UNIFORM_LIGHT_POSITION, light.getPosition());
		super.loadVector(UNIFORM_LIGHT_COLOR, light.getColor());
	}
}
