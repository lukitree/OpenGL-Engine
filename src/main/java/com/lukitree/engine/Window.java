package com.lukitree.engine;

import org.joml.Vector2i;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
	private long window;

	private int WIDTH, HEIGHT;

	private Set<Integer> keysPressed;

	public Window(int width, int height, String title)
	{
		keysPressed = new HashSet<>();

		WIDTH = width;
		HEIGHT = height;

		GLFWErrorCallback.createPrint(System.err).set();

		if(!glfwInit()) throw new IllegalStateException("Failed to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		window = glfwCreateWindow(WIDTH, HEIGHT, title, NULL, NULL);
		if(window == NULL) throw new RuntimeException("Failed to create window");

		glfwSetKeyCallback(window, (window,key,scancode,action, mods)->{
			if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
			{
				glfwSetWindowShouldClose(window, true);
			}
			else if (action == GLFW_PRESS)
			{
				keysPressed.add(key);
			}
			else if (action == GLFW_RELEASE)
			{
				keysPressed.remove(key);
			}
		});

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - height) / 2);

		glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback()
		{
			@Override
			public void invoke(long l, int i, int i1)
			{
				try(MemoryStack stack = stackPush())
				{
					IntBuffer w = stack.callocInt(1);
					IntBuffer h = stack.callocInt(1);

					glfwGetWindowSize(window, w, h);

					WIDTH = w.get(0);
					HEIGHT = h.get(0);
				}
			}
		});

		glfwSetWindowCloseCallback(window, new GLFWWindowCloseCallback()
		{
			@Override
			public void invoke(long l)
			{
				glfwHideWindow(window);
			}
		});

		glfwMakeContextCurrent(window);
		glfwSwapInterval(0);
		glfwShowWindow(window);

		createCapabilities();

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		System.out.println("LWJGL:]t" + Version.getVersion());
		System.out.println("OpenGL:\t" + glGetString(GL_VERSION));
	}

	public final Set<Integer> getKeysPressed()
	{
		return keysPressed;
	}

	public int width()
	{
		return WIDTH;
	}

	public int height()
	{
		return HEIGHT;
	}

	public void setTitle(String title)
	{
		glfwSetWindowTitle(window, title);
	}

	public Vector2i getSize()
	{
		return new Vector2i(WIDTH, HEIGHT);
	}

	public long id()
	{
		return window;
	}

	public boolean shouldClose()
	{
		return glfwWindowShouldClose(window);
	}

	public void draw(Drawable drawable)
	{
		drawable.draw(window);
	}
}
