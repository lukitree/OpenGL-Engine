package com.lukitree.engine.framework.object;

import com.lukitree.engine.framework.asset.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Entity
{
	private Model model;

	private Vector3f rot;
	private Vector3f pos;
	private Vector3f scale;
	private Vector3f origin;

	public Entity(Model model)
	{
		this.model = model;

		this.rot = new Vector3f(0.0f, 0.0f, 0.0f);
		this.pos = new Vector3f(0.0f, 0.0f, 0.0f);
		this.scale = new Vector3f(1.0f, 1.0f, 1.0f);
		this.origin = new Vector3f(0, 0, 0);
	}

	public Model getModel()
	{
		return model;
	}

	public void setRotation(float x, float y, float z)
	{
		setRotation(new Vector3f(x, y, z));
	}

	public void setRotation(Vector3f rotation)
	{
		this.rot = rotation;
	}

	public void rotate(float x, float y, float z)
	{
		rotate(new Vector3f(x, y, z));
	}

	public void rotate(Vector3f rotation)
	{
		setRotation(this.rot.add(rotation));
	}

	public void setPosition(float x, float y, float z)
	{
		setPosition(new Vector3f(x, y, z));
	}

	public void setPosition(Vector3f position)
	{
		this.pos = position;
	}

	public void move(float x, float y, float z)
	{
		move(new Vector3f(x, y, z));
	}

	public void move(Vector3f position)
	{
		setPosition(this.pos.add(position));
	}

	public void setScale(Vector3f scale)
	{
		this.scale = scale;
	}

	public void setScale(float x, float y, float z)
	{
		setScale(new Vector3f(x, y, z));
	}

	public void scale(float scalar)
	{
		setScale(scalar, scalar, scalar);
	}

	public void setOrigin(Vector3f origin)
	{
		this.origin = origin.mul(-1);
	}

	public void setOrigin(float x, float y, float z)
	{
		setOrigin(new Vector3f(x, y, z));
	}

	public float getDistance(Entity other)
	{
		return getDistance(other.pos);
	}

	public float getDistance(Vector3f position)
	{
		return this.pos.distance(position);
	}

	public Vector3f getRotation()
	{
		return rot;
	}

	public Vector3f getPosition()
	{
		return pos;
	}

	public Matrix4f getTransform()
	{
		Matrix4f transform = new Matrix4f();
		transform.translate(pos)
		         .rotate((float)Math.toRadians(rot.x), 1.0f, 0.0f, 0.0f)
		         .rotate((float)Math.toRadians(rot.y), 0.0f, 1.0f, 0.0f)
		         .rotate((float)Math.toRadians(rot.z), 0.0f, 0.0f, 1.0f)
		         .scale(scale)
		         .translate(origin);

		return transform;
	}
}
