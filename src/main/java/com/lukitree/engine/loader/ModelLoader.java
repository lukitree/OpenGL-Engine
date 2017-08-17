package com.lukitree.engine.loader;

import com.lukitree.engine.data.Model;
import com.lukitree.engine.data.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class ModelLoader
{
	private static List<Integer> vaos = new ArrayList<>();
	private static List<Integer> vbos = new ArrayList<>();

	public static Model loadOBJ(String modelFilename, Texture texture)
	{
		ClassLoader classLoader = ModelLoader.class.getClassLoader();

		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> texCoords = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();

		float[] verticesArray = null;
		float[] texCoordsArray = null;
		float[] normalsArray = null;
		int[] indicesArray = null;

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream("models/" + modelFilename))))
		{
			String line;
			String[] split;

			while (true)
			{
				line = reader.readLine();
				split = line.split(" ");

				switch (split[0])
				{
					case "v":
					{
						float x = Float.parseFloat(split[1]);
						float y = Float.parseFloat(split[2]);
						float z = Float.parseFloat(split[3]);
						vertices.add(new Vector3f(x, y, z));
						break;
					}
					case "vt":
					{
						float u = Float.parseFloat(split[1]);
						float v = Float.parseFloat(split[2]);
						texCoords.add(new Vector2f(u, v));
						break;
					}
					case "vn":
					{
						float x = Float.parseFloat(split[1]);
						float y = Float.parseFloat(split[2]);
						float z = Float.parseFloat(split[3]);
						normals.add(new Vector3f(x, y, z));
						break;
					}
				}

				if (split[0].equals("f"))
				{
					texCoordsArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}

			while (line != null)
			{
				if (!split[0].equals("f"))
				{
					line = reader.readLine();
					continue;
				}

				split = line.split(" ");
				for (int i = 1; i < split.length; ++i)
				{
					processVertex(split[i].split("/"), vertices, texCoords, normals,
					              indices, texCoordsArray, normalsArray);
				}

				line = reader.readLine();
			}


			verticesArray = new float[vertices.size() * 3];
			indicesArray = new int[indices.size()];

			int vertexPointer = 0;
			for (Vector3f v : vertices)
			{
				verticesArray[vertexPointer++] = v.x;
				verticesArray[vertexPointer++] = v.y;
				verticesArray[vertexPointer++] = v.z;
			}

			for (int i = 0; i < indices.size(); ++i)
			{
				indicesArray[i] = indices.get(i);
			}

		}
		catch (IOException e)
		{
			System.err.println("Could not load file: models/" + modelFilename);
			e.printStackTrace();
		}

		return loadToVAO(verticesArray, indicesArray, normalsArray, texCoordsArray, texture);
	}

	private static void processVertex(String[] vertexData, List<Vector3f> vertices, List<Vector2f> texCoords,
	                                  List<Vector3f> normals, List<Integer> indices, float[] texCoordsArray,
	                                  float[] normalArray)
	{
		int vertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(vertexPointer);

		int texCoordPointer = Integer.parseInt(vertexData[1]) - 1;
		Vector2f textureCoordinate = texCoords.get(texCoordPointer);
		texCoordsArray[vertexPointer * 2 + 0] = textureCoordinate.x;
		texCoordsArray[vertexPointer * 2 + 1] = 1 - textureCoordinate.y;

		int normalPointer = Integer.parseInt(vertexData[2]) - 1;
		Vector3f normal = normals.get(normalPointer);
		normalArray[vertexPointer * 3 + 0] = normal.x;
		normalArray[vertexPointer * 3 + 1] = normal.y;
		normalArray[vertexPointer * 3 + 2] = normal.z;
	}

	public static Model loadToVAO(float[] positions, int[] indices, float[] texCoords, Texture texture)
	{
		return loadToVAO(positions, indices, new float[0], texCoords, texture);
	}

	public static Model loadToVAO(float[] positions, int[] indices, float[] normals, float[] texCoords, Texture texture)
	{
		int vaoID = createVAO();

		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, positions, 3);
		storeDataInAttributeList(1, texCoords, 2);
		storeDataInAttributeList(2, normals, 3);
		unbindVAO();

		return new Model(vaoID, texture, indices.length);
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
	}

	private static void bindIndicesBuffer(int[] indices)
	{
		int vboID = glGenBuffers();
		vbos.add(vboID);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
	}
}
