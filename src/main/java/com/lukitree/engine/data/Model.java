package com.lukitree.engine.data;

import javafx.animation.TranslateTransition;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Model
{
	private int vaoID;
	private int textureID;
	private int vertexCount;

	public Model(int vaoID, Texture texture, int vertexCount)
	{
		this.vaoID = vaoID;
		this.textureID = texture.getId();
		this.vertexCount = vertexCount;
	}

	public int getVaoID()
	{
		return vaoID;
	}

	public int getTextureID()
	{
		return textureID;
	}

	public int getVertexCount()
	{
		return vertexCount;
	}
}
