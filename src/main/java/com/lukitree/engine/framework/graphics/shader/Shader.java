package com.lukitree.engine.framework.graphics.shader;

import org.joml.*;

import java.io.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

public abstract class Shader
{
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	public Shader(String vertexFilename, String fragmentFilename)
	{
		vertexShaderID = loadShader(vertexFilename, GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFilename, GL_FRAGMENT_SHADER);

		programID = glCreateProgram();

		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, fragmentShaderID);
		linkProgram(programID);
	}

	protected void loadFloat(int location, float value)
	{
		glUniform1f(location, value);
	}

	protected void loadVector(int location, Vector3f vector)
	{
		glUniform3f(location, vector.x, vector.y, vector.z);
	}

	protected void loadBoolean(int location, boolean value)
	{
		int toLoad = (value) ? 1 : 0;
		glUniform1i(location, toLoad);
	}

	protected void loadMatrix(int location, Matrix4f matrix)
	{
		glUniformMatrix4fv(location, false, matrix.get(new float[16]));
	}

	protected void loadTexture(int texture, int location)
	{
		glActiveTexture(GL_TEXTURE0 + location);
		glBindTexture(GL_TEXTURE_2D, texture);
	}

	protected void loadTexture(int texture)
	{
		loadTexture(texture, 0);
	}

	private static void linkProgram(int programID)
	{
		glLinkProgram(programID);

		if(glGetProgrami(programID, GL_LINK_STATUS) != 1)
		{
			System.err.println(glGetProgramInfoLog(programID));
			throw new RuntimeException("Failed to link shader program");
		}
	}

	public void start()
	{
		glUseProgram(programID);
	}

	public void stop()
	{
		glUseProgram(0);
	}

	public void close()
	{
		stop();

		glDetachShader(programID, vertexShaderID);
		glDetachShader(programID, fragmentShaderID);

		glDeleteShader(vertexShaderID);
		glDeleteShader(fragmentShaderID);

		glDeleteProgram(programID);
	}

	private static int loadShader(String filename, int type)
	{
		ClassLoader classLoader = Shader.class.getClassLoader();
		StringBuilder shaderSource = new StringBuilder();

		try(BufferedReader reader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream ("shaders/" + filename))))
		{
			String line;
			while((line = reader.readLine()) != null)
			{
				shaderSource.append(line).append(System.lineSeparator());
			}
		}
		catch (IOException e)
		{
			System.err.println("Could not read file: models/" + filename);
			e.printStackTrace();
			System.exit(-1);
		}

		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);

		if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println(glGetShaderInfoLog(shaderID));
			throw new RuntimeException("Could not compile shader: shaders/" + filename);
		}

		return shaderID;
	}
}
