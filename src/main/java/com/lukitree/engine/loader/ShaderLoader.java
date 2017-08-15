package com.lukitree.engine.loader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL40.*;

public class ShaderLoader
{
	public static int createProgram()
	{
		int vertexShader, fragmentShader, tessControlShader, tessEvalShader, geometryShader, program;

		String vertexShaderSource = loadShaderSourceFromFile("vertexShader.glsl");
		String tessControlShaderSource = loadShaderSourceFromFile("tessControlShader.glsl");
		String tessEvalShaderSource = loadShaderSourceFromFile("tessEvalShader.glsl");
		String fragmentShaderSource = loadShaderSourceFromFile("fragmentShader.glsl");
		String geometryShaderSource = loadShaderSourceFromFile("geometryShader.glsl");

		vertexShader = compileShader(vertexShaderSource, GL_VERTEX_SHADER);
		tessControlShader = compileShader(tessControlShaderSource, GL_TESS_CONTROL_SHADER);
		tessEvalShader = compileShader(tessEvalShaderSource, GL_TESS_EVALUATION_SHADER);
		geometryShader = compileShader(geometryShaderSource, GL_GEOMETRY_SHADER);
		fragmentShader = compileShader(fragmentShaderSource, GL_FRAGMENT_SHADER);

		List<Integer> shaderList = new ArrayList<>();

		shaderList.add(vertexShader);
		//shaderList.add(tessControlShader);
		//shaderList.add(tessEvalShader);
		//shaderList.add(geometryShader);
		shaderList.add(fragmentShader);

		program = linkProgram(shaderList);

		glDeleteShader(vertexShader);
		glDeleteShader(tessControlShader);
		glDeleteShader(tessEvalShader);
		glDeleteShader(geometryShader);
		glDeleteShader(fragmentShader);

		return program;
	}

	private static int linkProgram(List<Integer> shaderList)
	{
		int program = glCreateProgram();

		for(int shader : shaderList)
		{
			glAttachShader(program, shader);
		}

		glLinkProgram(program);

		int status = glGetProgrami(program, GL_LINK_STATUS);

		if(status != 1)
		{
			System.err.println("Failed to link program:\n" + glGetProgramInfoLog(program));
			throw new RuntimeException("Failed to link program");
		}

		return program;
	}

	private static String loadShaderSourceFromFile(String filename)
	{
		ClassLoader classLoader = ShaderLoader.class.getClassLoader();
		StringBuilder source = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream ("shaders/" + filename))))
		//try (BufferedReader reader = new BufferedReader(new FileReader(new File(classLoader.getResource("shaders/" + filename) .toURI()))))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				source.append(line).append(System.lineSeparator());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return source.toString();
	}

	private static int compileShader(final String shaderSource, final int shaderType)
	{
		int shader = glCreateShader(shaderType);
		glShaderSource(shader, shaderSource);
		glCompileShader(shader);

		int status = glGetShaderi(shader, GL_COMPILE_STATUS);

		if(status != 1)
		{
			String type = getShaderType(shader);
			System.err.println("Failed to compile " + type + " shader:\n" + glGetShaderInfoLog(shader));
			throw new RuntimeException("Failed to compile shader.");
		}

		return shader;
	}

	private static String getShaderType(int shader)
	{
		int type = glGetShaderi(shader, GL_SHADER_TYPE);
		String shaderType = null;

		switch(type)
		{
			case GL_VERTEX_SHADER:
				shaderType = "Vertex";
				break;
			case GL_TESS_CONTROL_SHADER:
				shaderType = "Tess Control";
				break;
			case GL_TESS_EVALUATION_SHADER:
				shaderType = "Tess Eval";
				break;
			case GL_GEOMETRY_SHADER:
				shaderType = "Geometry";
				break;
			case GL_FRAGMENT_SHADER:
				shaderType = "Fragment";
				break;
			default:
				shaderType = "Unknown Type" ;
		}

		return shaderType;
	}
}
