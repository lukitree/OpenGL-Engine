package com.lukitree.engine.loader;

import com.lukitree.engine.data.Model;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

public class Loader
{
	private static List<Integer> vaos = new ArrayList<>();
	private static List<Integer> vbos = new ArrayList<>();
	private static List<Integer> textures = new ArrayList<>();

	public static Model loadToVAO(float[] positions, int[] indices, String texturePath, float[] texCoords)
	{
		int vaoID = createVAO();

		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, positions, 3);
		storeDataInAttributeList(1, texCoords, 2);
		unbindVAO();

		return new Model(vaoID, loadTexture(texturePath), indices.length);
	}

	public static int loadTexture(String filename)
	{
		ClassLoader classLoader = Loader.class.getClassLoader();
		ByteBuffer textureData = null;
		int width = 0;
		int height = 0;

		try
		{
			InputStream stream = classLoader.getResourceAsStream("textures/" + filename);
			byte[] img = IOUtils.toByteArray(stream);

			try (MemoryStack stack = MemoryStack.stackPush())
			{
				ByteBuffer image = BufferUtils.createByteBuffer(img.length);
				IntBuffer w = stack.callocInt(1);
				IntBuffer h = stack.callocInt(1);
				IntBuffer f = stack.callocInt(1);

				image.put(img);
				image.flip();

				stbi_set_flip_vertically_on_load(true);
				textureData = stbi_load_from_memory(image, w, h, f, 4);
				if(textureData == null)
				{
					throw new RuntimeException("Failed to load texture: " + filename + "\n" + stbi_failure_reason());
				}

				width = w.get(0);
				height = h.get(0);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		int texture = glGenTextures();
		textures.add(texture);


		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, textureData);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

		return texture;
	}

	private static int createVAO()
	{
		int vaoID = glGenVertexArrays();
		vaos.add(vaoID);

		glBindVertexArray(vaoID);

		return vaoID;
	}

	private static void storeDataInAttributeList(int attributeNumber, float[] data, int size)
	{
		int vboID = glGenBuffers();
		vbos.add(vboID);

		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		glVertexAttribPointer(attributeNumber, size, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	private static void unbindVAO()
	{
		glBindVertexArray(0);
	}

	public static void close()
	{
		for (int vao : vaos)
		{
			glDeleteVertexArrays(vao);
		}

		for (int vbo : vbos)
		{
			glDeleteBuffers(vbo);
		}

		for (int texture : textures)
		{
			glDeleteTextures(texture);
		}
	}

	private static void bindIndicesBuffer(int[] indices)
	{
		int vboID = glGenBuffers();
		vbos.add(vboID);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
	}
}
