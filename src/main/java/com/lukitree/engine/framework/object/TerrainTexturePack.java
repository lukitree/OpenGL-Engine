package com.lukitree.engine.framework.object;

import com.lukitree.engine.framework.asset.*;

public class TerrainTexturePack
{
	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;

	public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture, TerrainTexture bTexture)
	{
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}

	public TerrainTexturePack(Texture backgroundTexture, Texture rTexture, Texture gTexture, Texture bTexture)
	{
		this.backgroundTexture = new TerrainTexture(backgroundTexture);
		this.rTexture = new TerrainTexture(rTexture);
		this.gTexture = new TerrainTexture(gTexture);
		this.bTexture = new TerrainTexture(bTexture);
	}

	public TerrainTexture getBackgroundTexture()
	{
		return backgroundTexture;
	}

	public TerrainTexture getrTexture()
	{
		return rTexture;
	}

	public TerrainTexture getgTexture()
	{
		return gTexture;
	}

	public TerrainTexture getbTexture()
	{
		return bTexture;
	}
}
