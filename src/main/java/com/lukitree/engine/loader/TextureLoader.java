package com.lukitree.engine.loader;

import com.lukitree.engine.data.Texture;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class TextureLoader
{
	private static List<Integer> textures = new ArrayList<>();

	public static Texture loadTexture(String filename)
	{
		ClassLoader classLoader = TextureLoader.class.getClassLoader();
		ByteBuffer textureData = null;
		int width = 0;
		int height = 0;

		try
		{
			InputStream stream = classLoader.getResourceAsStream("textures/" + filename);

			if(stream == null) throw new FileNotFoundException("Failed to load file: textures/" + filename);

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
				if (textureData == null)
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

		return new Texture(texture);
	}

	public void close()
	{
		for (int texture : textures)
		{
			glDeleteTextures(texture);
		}
	}
}
