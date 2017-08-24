package com.lukitree.engine.framework.asset;

public class TerrainTexture
{
	private int textureID;

	public TerrainTexture(int textureID)
	{
		this.textureID = textureID;
	}

	public TerrainTexture(Texture texture)
	{
		this.textureID = texture.getTextureID();
	}

	public int getTextureID()
	{
		return textureID;
	}
}
