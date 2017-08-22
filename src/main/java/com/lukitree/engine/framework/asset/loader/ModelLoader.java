package com.lukitree.engine.framework.asset.loader;

import com.lukitree.engine.framework.asset.Model;
import com.lukitree.engine.framework.graphics.shader.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class ModelLoader
{
	private List<Integer> vaos = new ArrayList<>();
	private List<Integer> vbos = new ArrayList<>();

	public Model loadOBJ(String filename)
	{
		ClassLoader classLoader = ModelLoader.class.getClassLoader();

		float[] positions = null;
		float[] texCoords = null;
		float[] normals = null;
		int[] indices = null;

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream("models/" + filename))))
		{
			List<Vector3f> positionsList = new ArrayList<>();
			List<Vector2f> texCoordsList = new ArrayList<>();
			List<Vector3f> normalsList = new ArrayList<>();

			Map<Integer, String> verticesMap = new HashMap<>();
			Map<String, Integer> verticesMapReverse = new HashMap<>();
			List<Integer> indicesList = new ArrayList<>();

			String line;
			String[] split;

			while ((line = reader.readLine()) != null)
			{
				split = line.split("\\s+");

				switch (split[0])
				{
					case "v":
						float x = Float.parseFloat(split[1]);
						float y = Float.parseFloat(split[2]);
						float z = Float.parseFloat(split[3]);
						positionsList.add(new Vector3f(x, y, z));
						break;
					case "vt":
						float u = Float.parseFloat(split[1]);
						float v = Float.parseFloat(split[2]);
						texCoordsList.add(new Vector2f(u, v));
						break;
					case "vn":
						float a = Float.parseFloat(split[1]);
						float b = Float.parseFloat(split[2]);
						float c = Float.parseFloat(split[3]);
						normalsList.add(new Vector3f(a, b, c));
						break;
				}

				if (split[0].equals("f")) break;
			}

			// Faster load uses more space, but loads faster
			boolean fasterLoad = (positionsList.size() + texCoordsList.size() + normalsList.size() > 10000) ? true : false;
			boolean hasTexCoords = (texCoordsList.size() > 0) ? true : false;
			boolean hasNormals = (normalsList.size() > 0) ? true : false;

			int vertexIndex = 0;

			while (line != null)
			{
				split = line.split("\\s+");
				if (!split[0].equals("f"))
				{
					line = reader.readLine();
					continue;
				}

				for (int i = 1; i <= 3; ++i)
				{
					String val = split[i];
					int index;
					if (fasterLoad)
					{
						index = vertexIndex;
						verticesMap.put(index, val);
						vertexIndex++;
					}
					else if (!verticesMap.containsValue(val))
					{
						index = vertexIndex;
						verticesMap.put(index, val);
						verticesMapReverse.put(val, index);
						vertexIndex++;
					}
					else
					{
						index = verticesMapReverse.get(val);
					}

					indicesList.add(index);
				}

				line = reader.readLine();
			}

			List<Float> positionsFloatList = new ArrayList<>();
			List<Float> texCoordsFloatList = new ArrayList<>();
			List<Float> normalsFloatList = new ArrayList<>();

			for (int i = 0; i < verticesMap.size(); ++i)
			{
				String el[] = verticesMap.get(i).split("/");

				int p = Integer.parseInt(el[0]) - 1;
				Vector3f pos = positionsList.get(p);

				positionsFloatList.add(pos.x);
				positionsFloatList.add(pos.y);
				positionsFloatList.add(pos.z);

				if (hasTexCoords)
				{
					int t = Integer.parseInt(el[1]) - 1;
					Vector2f tex = texCoordsList.get(t);

					texCoordsFloatList.add(tex.x);
					texCoordsFloatList.add(tex.y);
				}
				if (hasNormals)
				{
					int n = (hasTexCoords) ? Integer.parseInt(el[2]) - 1 : Integer.parseInt(el[1]) - 1;

					Vector3f nml = normalsList.get(n);

					normalsFloatList.add(nml.x);
					normalsFloatList.add(nml.y);
					normalsFloatList.add(nml.z);
				}
			}

			positions = new float[positionsFloatList.size()];
			texCoords = new float[texCoordsFloatList.size()];
			normals = new float[normalsFloatList.size()];
			indices = new int[indicesList.size()];

			for (int i = 0; i < positionsFloatList.size(); ++i)
			{
				positions[i] = positionsFloatList.get(i);
			}
			for (int i = 0; i < texCoordsFloatList.size(); ++i)
			{
				texCoords[i] = texCoordsFloatList.get(i);
			}
			for (int i = 0; i < normalsFloatList.size(); ++i)
			{
				normals[i] = normalsFloatList.get(i);
			}
			for (int i = 0; i < indicesList.size(); ++i)
			{
				indices[i] = indicesList.get(i);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		System.gc();
		return loadToVAO(positions, indices, normals, texCoords);
	}

	public Model loadToVAO(float[] positions, int[] indices, float[] normals, float[] texCoords)
	{
		int vaoID = createVAO();

		bindIndicesBuffer(indices);
		storeDataInAttributeList(Attribute.POSITION.location, positions, 3);
		storeDataInAttributeList(Attribute.TEX_COORD.location, texCoords, 2);
		storeDataInAttributeList(Attribute.NORMAL.location, normals, 3);
		unbindVAO();

		return new Model(vaoID, indices.length);
	}

	private int createVAO()
	{
		int vaoID = glGenVertexArrays();
		vaos.add(vaoID);

		glBindVertexArray(vaoID);

		return vaoID;
	}

	private void storeDataInAttributeList(int attributeNumber, float[] data, int size)
	{
		int vboID = glGenBuffers();
		vbos.add(vboID);

		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		glVertexAttribPointer(attributeNumber, size, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	private void bindIndicesBuffer(int[] indices)
	{
		int vboID = glGenBuffers();
		vbos.add(vboID);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
	}

	private void unbindVAO()
	{
		glBindVertexArray(0);
	}

	public void close()
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
}
