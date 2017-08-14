package com.lukitree.engine.data;

import javafx.animation.TranslateTransition;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Model
{
	private int vaoID;
	private int textureID;
	private int vertexCount;

	private Vector3f rotation;
	private Vector3f position;
	private Vector3f scale;

	public Model(int vaoID, int textureID, int vertexCount)
	{
		this.vaoID = vaoID;
		this.textureID = textureID;
		this.vertexCount = vertexCount;

		rotation = new Vector3f();
		position = new Vector3f();
		scale = new Vector3f(1.0f, 1.0f, 1.0f);
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

	public Matrix4f getTransform()
	{
		Matrix4f transform = new Matrix4f();
		transform.translate(position)
		         .rotate(rotation.x, 1.0f, 0.0f, 0.0f)
		         .rotate(rotation.y, 0.0f, 1.0f, 0.0f)
		         .rotate(rotation.z, 0.0f, 0.0f, 1.0f)
		         .scale(scale);

		return transform;
	}

	public void setRotation(float x, float y, float z)
	{
		setRotation(new Vector3f(x, y, z));
	}

	public void setRotation(Vector3f rotation)
	{
		this.rotation = rotation;
	}

	public void rotate(float x, float y, float z)
	{
		rotate(new Vector3f(x, y, z));
	}

	public void rotate(Vector3f rotation)
	{
		setRotation(this.rotation.add(rotation));
	}

	public void setPosition(float x, float y, float z)
	{
		setPosition(new Vector3f(x, y, z));
	}

	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	public void move(float x, float y, float z)
	{
		move(new Vector3f(x, y, z));
	}

	public void move(Vector3f position)
	{
		setPosition(this.position.add(position));
	}

	public void setScale(Vector3f scale)
	{
		this.scale = scale;
	}

	public void setScale(float x, float y, float z)
	{
		setScale(new Vector3f(x, y, z));
	}
}
