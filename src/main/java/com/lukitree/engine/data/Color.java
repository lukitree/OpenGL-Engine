package com.lukitree.engine.data;

public class Color
{
	public float r;
	public float g;
	public float b;
	public float a;

	public Color()
	{
		this(0.0f, 0.f, 0.0f, 1.0f);
	}

	public Color(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public float[] toArray()
	{
		return new float[] {r,g,b,a};
	}

	public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f, 1.0f);

	public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);

	public static final Color RED = new Color(1.0f, 0.0f, 0.0f, 1.0f);

	public static final Color GREEN = new Color(0.0f, 1.0f, 0.0f, 1.0f);

	public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f, 1.0f);

	public static final Color YELLOW = new Color(1.0f, 1.0f, 0.0f, 1.0f);

	public static final Color MAGENTA = new Color(1.0f, 0.0f, 1.0f, 1.0f);

	public static final Color CYAN = new Color(0.0f, 1.0f, 1.0f, 1.0f);
}
