package com.lukitree.engine;

import com.lukitree.engine.data.Model;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Loader
{
	private List<Integer> vaos = new ArrayList<>();
	private List<Integer> vbos = new ArrayList<>();

	public Model loadToVAO(float[] positions)
	{
		int vaoID = createVAO();
		storeDataInAttributeList(0, positions);
		unbindVAO();

		return new Model(vaoID, positions.length / 3);
	}

	private int createVAO()
	{
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		vaos.add(vaoID);

		return vaoID;
	}

	private void storeDataInAttributeList(int attributeNumber, float[] data)
	{
		int vboID = glGenBuffers();

		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vbos.add(vboID);
	}

	private void unbindVAO()
	{
		glBindVertexArray(0);
	}

	public void cleanup()
	{
		for(int vao : vaos)
		{
			glDeleteVertexArrays(vao);
		}

		for(int vbo : vbos)
		{
			glDeleteBuffers(vbo);
		}
	}
}
