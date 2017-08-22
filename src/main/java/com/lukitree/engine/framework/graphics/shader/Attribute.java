package com.lukitree.engine.framework.graphics.shader;

import static org.lwjgl.opengl.GL20.*;

public enum Attribute
{
	POSITION(0),
	TEX_COORD(1),
	NORMAL(2);

	public final int location;

	private Attribute(int location)
	{
		this.location = location;
	}

	public void enable()
	{
		glEnableVertexAttribArray(location);
	}

	public void disable()
	{
		glDisableVertexAttribArray(location);
	}

	public static void enableAll()
	{
		for(Attribute el : Attribute.values()) el.enable();
	}

	public static void disableAll()
	{
		for(Attribute el : Attribute.values()) el.disable();
	}
}
