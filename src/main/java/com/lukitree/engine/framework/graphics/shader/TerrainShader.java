package com.lukitree.engine.framework.graphics.shader;

import com.lukitree.engine.framework.asset.*;
import com.lukitree.engine.framework.object.*;
import org.joml.*;

public class TerrainShader extends Shader
{
	final static int UNIFORM_MVP = 10;
	final static int UNIFORM_LIGHT_POSITION = 11;
	final static int UNIFORM_LIGHT_COLOR = 12;
	final static int UNIFORM_SKY_COLOR = 13;
	final static int UNIFORM_SHINE_DAMPER = 14;
	final static int UNIFORM_REFLECTIVITY = 15;
	final static int UNIFORM_MODEL_TRANSFORM = 16;
	final static int UNIFORM_VIEW_TRANSFORM = 17;
	final static int UNIFORM_PROJECTION_TRANSFORM = 18;
	final static int UNIFORM_AMBIENT_LIGHT_LEVEL = 19;

	private static final String VERTEX_FILENAME = "terrainVertexShader.glsl";
	private static final String FRAGMENT_FILENAME = "terrainFragmentShader.glsl";

	public TerrainShader()
	{
		super(VERTEX_FILENAME, FRAGMENT_FILENAME);
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

	public void loadTextures(TerrainTexturePack texturePack, TerrainTexture blendMap)
	{
		loadTextures(texturePack, blendMap, 1, 0);
	}

	public void loadTextures(TerrainTexturePack texturePack, TerrainTexture blendMap, int shineDamper, int reflectivity)
	{
		TerrainTexture backgroundTexture = texturePack.getBackgroundTexture();
		TerrainTexture rTexture = texturePack.getrTexture();
		TerrainTexture gTexture = texturePack.getgTexture();
		TerrainTexture bTexture = texturePack.getbTexture();

		super.loadTexture(backgroundTexture.getTextureID(), 0);
		super.loadTexture(rTexture.getTextureID(), 1);
		super.loadTexture(gTexture.getTextureID(), 2);
		super.loadTexture(bTexture.getTextureID(), 3);
		super.loadTexture(blendMap.getTextureID(), 4);

		super.loadFloat(UNIFORM_SHINE_DAMPER, shineDamper);
		super.loadFloat(UNIFORM_REFLECTIVITY, reflectivity);
	}

	public void loadSkyColor(float r, float g, float b)
	{
		super.loadVector(UNIFORM_SKY_COLOR, new Vector3f(r, g, b));
	}

	public void loadLight(Light light)
	{
		super.loadVector(UNIFORM_LIGHT_POSITION, light.getPosition());
		super.loadVector(UNIFORM_LIGHT_COLOR, light.getColor());
	}
}
