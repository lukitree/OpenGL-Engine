package com.lukitree.engine;

import com.lukitree.engine.data.Model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer
{
	public void prepare()
	{
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	public void render(Model model)
	{
		glBindVertexArray(model.getVaoID());
		glEnableVertexAttribArray(0);
		glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
}
