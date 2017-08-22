package com.lukitree.engine.framework.object;

import com.lukitree.engine.framework.utility.*;
import org.joml.*;

public class Light
{
	private Vector3f position;
	private Vector3f color;

	public Light(Vector3f position, Vector3f color)
	{
		this.position = position;
		this.color = color;
	}

	public Light(Vector3f position, Color color)
	{
		this(position, new Vector3f(color.r, color.g, color.b));
	}

	public Vector3f getPosition()
	{
		return position;
	}

	public void setPosition(Vector3f position)
	{
		this.position = position;
	}


	public Vector3f getColor()
	{
		return color;
	}

	public void setColor(Vector3f color)
	{
		this.color = color;
	}
}
