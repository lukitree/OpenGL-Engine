package com.lukitree.engine.framework.object;

import com.lukitree.engine.framework.asset.*;
import com.lukitree.engine.framework.asset.loader.*;

public class Terrain
{
	private static final float SIZE = 800;

	private float x;
	private float z;
	private Model model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;

	public Terrain(int gridX, int gridZ, ModelLoader loader, TerrainTexturePack texturePack, TerrainTexture blendMap)
	{
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;

		this.model = generateTerrain(loader);
	}

	public Terrain(int gridX, int gridZ, Model model, TerrainTexturePack texturePack, TerrainTexture blendMap)
	{
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.model = model;
	}

	private Model generateTerrain(ModelLoader loader)
	{
		final int VERTEX_COUNT = 128;
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++)
		{
			for (int j = 0; j < VERTEX_COUNT; j++)
			{
				vertices[vertexPointer * 3] = (float)j / ((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = 0;
				vertices[vertexPointer * 3 + 2] = (float)i / ((float)VERTEX_COUNT - 1) * SIZE;
				normals[vertexPointer * 3] = 0;
				normals[vertexPointer * 3 + 1] = 1;
				normals[vertexPointer * 3 + 2] = 0;
				textureCoords[vertexPointer * 2] = (float)j / ((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float)i / ((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++)
		{
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++)
			{
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, indices, textureCoords, normals);
	}

	public float getX()
	{
		return x;
	}

	public float getZ()
	{
		return z;
	}

	public Model getModel()
	{
		return model;
	}

	public TerrainTexturePack getTexturePack()
	{
		return texturePack;
	}

	public TerrainTexture getBlendMap()
	{
		return blendMap;
	}
}