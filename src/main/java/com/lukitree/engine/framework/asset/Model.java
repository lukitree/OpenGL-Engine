package com.lukitree.engine.framework.asset;

public class Model
{
	private int vaoID;
	private int vertexCount;
	private float ambientLightLevel = 0.2f;

	public boolean usesFakeLighting()
	{
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean use)
	{
		this.useFakeLighting = use;
	}

	private boolean useFakeLighting = false;

	public Model(int vaoID, int vertexCount)
	{
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID()
	{
		return vaoID;
	}

	public int getVertexCount()
	{
		return vertexCount;
	}

	public float getAmbientLightLevel()
	{
		return ambientLightLevel;
	}

	public void setAmbientLightLevel(float level)
	{
		this.ambientLightLevel = level;
	}
}
