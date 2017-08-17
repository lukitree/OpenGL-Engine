package com.lukitree.engine.data;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Vector;

public class Camera
{
	private Vector3f position;
	private Vector3f orientation;

	private Vector3f initialPosition;
	private Vector3f initialOrientation;

	public Camera()
	{
		this.initialPosition = new Vector3f(0.0f,0.0f,-3.0f);
		this.initialOrientation = new Vector3f(0.0f, 0.0f, 0.0f);
		reset();
	}

	public void move(Vector3f movement)
	{
		position = position.add(movement);
	}

	public void move(float x, float y, float z)
	{
		move(new Vector3f(x, y, z));
	}

	public void rotate(Vector3f rotation)
	{
		orientation = orientation.add(rotation);
	}

	public void rotate(float x, float y, float z)
	{
		rotate(new Vector3f(x, y, z));
	}

	public Matrix4f getViewMatrix()
	{
		Matrix4f view = new Matrix4f();

		view.rotate((float) Math.toRadians(orientation.x), 1.0f, 0.0f, 0.0f)
		    .rotate((float) Math.toRadians(orientation.y), 0.0f, 1.0f, 0.0f)
		    .translate(position);

		return view;
	}

	public void reset()
	{
		position = new Vector3f(initialPosition);
		orientation = new Vector3f(initialOrientation);
	}
}
