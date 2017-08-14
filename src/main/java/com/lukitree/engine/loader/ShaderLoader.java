package com.lukitree.engine.loader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL40.*;

public class ShaderLoader
{
	public static String loadShaderSourceFromFile(String filename)
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

	public static int createProgram()
	{
		int vertexShader, fragmentShader, tessControlShader, tessEvalShader, geometryShader, program;

		program = glCreateProgram();

		String vertexShaderSource = loadShaderSourceFromFile("vertexShader.glsl");
		String tessControlShaderSource = loadShaderSourceFromFile("tessControlShader.glsl");
		String tessEvalShaderSource = loadShaderSourceFromFile("tessEvalShader.glsl");
		String fragmentShaderSource = loadShaderSourceFromFile("fragmentShader.glsl");
		String geometryShaderSource = loadShaderSourceFromFile("geometryShader.glsl");

		vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, vertexShaderSource);
		glCompileShader(vertexShader);

		tessControlShader = glCreateShader(GL_TESS_CONTROL_SHADER);
		glShaderSource(tessControlShader, tessControlShaderSource);
		glCompileShader(tessControlShader);

		tessEvalShader = glCreateShader(GL_TESS_EVALUATION_SHADER);
		glShaderSource(tessEvalShader, tessEvalShaderSource);
		glCompileShader(tessEvalShader);

		geometryShader = glCreateShader(GL_GEOMETRY_SHADER);
		glShaderSource(geometryShader, geometryShaderSource);
		glCompileShader(geometryShader);

		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);

		checkShaderStatus(vertexShader);
		checkShaderStatus(tessControlShader);
		checkShaderStatus(tessEvalShader);
		checkShaderStatus(geometryShader);
		checkShaderStatus(fragmentShader);

		glAttachShader(program, vertexShader);
		//glAttachShader(program, tessControlShader);
		//glAttachShader(program, tessEvalShader);
		//glAttachShader(program, geometryShader);
		glAttachShader(program, fragmentShader);

		glLinkProgram(program);

		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		glDeleteShader(tessControlShader);
		glDeleteShader(tessEvalShader);

		return program;
	}

	private static void checkShaderStatus(int shader)
	{
		int status;
		status = glGetShaderi(shader, GL_COMPILE_STATUS);

		if (status == 0)
		{
			//throw new InvalidStateException("Failed to compile GLSL shader:\n\t" + glGetShaderInfoLog(shader));
			throw new RuntimeException("Failed to compile GLSL shader:\n\t" + glGetShaderInfoLog(shader));
		}
	}
}
