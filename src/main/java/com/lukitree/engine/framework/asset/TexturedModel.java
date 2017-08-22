package com.lukitree.engine.framework.asset;

public class TexturedModel extends Model
{
	private Texture texture;

	private boolean hasTransparency;

	public TexturedModel(int vaoID, int vertexCount)
	{
		super(vaoID, vertexCount);
	}

	public boolean hasTransparency()
	{
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency)
	{
		this.hasTransparency = hasTransparency;
	}

	public TexturedModel(Model model, Texture texture)
	{
		super(model.getVaoID(), model.getVertexCount());
		this.texture = texture;
	}

	public Texture getTexture()
	{
		return texture;
	}

	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}
}
