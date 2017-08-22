package com.lukitree.engine.framework.window;

import com.lukitree.engine.framework.window.event.*;
import org.joml.*;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLFWWindow implements Window
{
	private long window;

	private int width, height;
	private boolean isOpen = true;
	private boolean isCursorHidden = false;
	private Vector2f mousePosition = new Vector2f();

	private Queue<Event> eventQueue = new LinkedList<>();

	public GLFWWindow(int width, int height, String title)
	{
		this.width = width;
		this.height = height;

		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit()) throw new IllegalStateException("Failed to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		window = glfwCreateWindow(this.width, this.height, title, NULL, NULL);
		if (window == NULL) throw new RuntimeException("Failed to create window");

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - this.width) / 2, (vidmode.height() - height) / 2);

		setupCallbacks();

		glfwMakeContextCurrent(window);
		glfwSwapInterval(0);
		glfwShowWindow(window);

		createCapabilities();

		System.out.println("LWJGL:\t" + Version.getVersion());
	}

	private void setupCallbacks()
	{
		setupCloseCallback();
		setupResizeCallback();
		setupKeyCallback();
		setupMouseCallback();
	}

	private void setupCloseCallback()
	{
		glfwSetWindowCloseCallback(window, new GLFWWindowCloseCallback()
		{
			@Override
			public void invoke(long l)
			{
				isOpen = false;
				Event event = new Event();
				event.type = Event.Type.Closed;
				eventQueue.add(event);
			}
		});
	}

	private void setupResizeCallback()
	{
		glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback()
		{
			@Override
			public void invoke(long l, int i, int i1)
			{
				int[] w = new int[1];
				int[] h = new int[1];

				glfwGetWindowSize(window, w, h);

				Vector2i newSize = new Vector2i(w[0], h[0]);

				width = newSize.x;
				height = newSize.y;

				Event event = new Event();
				event.type = Event.Type.Resized;
				event.size.width = width;
				event.size.height = height;
				eventQueue.add(event);
			}
		});
	}

	private void setupKeyCallback()
	{
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			Event event = new Event();

			switch (key)
			{
				case GLFW_KEY_UP:
					event.key.code = Key.UP_ARROW;
					break;
				case GLFW_KEY_DOWN:
					event.key.code = Key.DOWN_ARROW;
					break;
				case GLFW_KEY_LEFT:
					event.key.code = Key.LEFT_ARROW;
					break;
				case GLFW_KEY_RIGHT:
					event.key.code = Key.RIGHT_ARROW;
					break;
				case GLFW_KEY_A:
					event.key.code = Key.A;
					break;
				case GLFW_KEY_B:
					event.key.code = Key.B;
					break;
				case GLFW_KEY_C:
					event.key.code = Key.C;
					break;
				case GLFW_KEY_D:
					event.key.code = Key.D;
					break;
				case GLFW_KEY_E:
					event.key.code = Key.E;
					break;
				case GLFW_KEY_F:
					event.key.code = Key.F;
					break;
				case GLFW_KEY_G:
					event.key.code = Key.G;
					break;
				case GLFW_KEY_H:
					event.key.code = Key.H;
					break;
				case GLFW_KEY_I:
					event.key.code = Key.I;
					break;
				case GLFW_KEY_J:
					event.key.code = Key.J;
					break;
				case GLFW_KEY_K:
					event.key.code = Key.K;
					break;
				case GLFW_KEY_L:
					event.key.code = Key.L;
					break;
				case GLFW_KEY_M:
					event.key.code = Key.M;
					break;
				case GLFW_KEY_N:
					event.key.code = Key.N;
					break;
				case GLFW_KEY_O:
					event.key.code = Key.O;
					break;
				case GLFW_KEY_P:
					event.key.code = Key.P;
					break;
				case GLFW_KEY_Q:
					event.key.code = Key.Q;
					break;
				case GLFW_KEY_S:
					event.key.code = Key.S;
					break;
				case GLFW_KEY_T:
					event.key.code = Key.T;
					break;
				case GLFW_KEY_U:
					event.key.code = Key.U;
					break;
				case GLFW_KEY_V:
					event.key.code = Key.V;
					break;
				case GLFW_KEY_W:
					event.key.code = Key.W;
					break;
				case GLFW_KEY_X:
					event.key.code = Key.X;
					break;
				case GLFW_KEY_Y:
					event.key.code = Key.Y;
					break;
				case GLFW_KEY_Z:
					event.key.code = Key.Z;
					break;
				case GLFW_KEY_SPACE:
					event.key.code = Key.SPACE;
					break;
				case GLFW_KEY_ESCAPE:
					event.key.code = Key.ESCAPE;
					break;
				case GLFW_KEY_TAB:
					event.key.code = Key.TAB;
					break;
				case GLFW_KEY_ENTER:
					event.key.code = Key.ENTER;
					break;
				case GLFW_KEY_PAGE_UP:
					event.key.code = Key.PAGE_UP;
					break;
				case GLFW_KEY_HOME:
					event.key.code = Key.HOME;
					break;
				case GLFW_KEY_END:
					event.key.code = Key.END;
					break;
				case GLFW_KEY_INSERT:
					event.key.code = Key.INSERT;
					break;
				case GLFW_KEY_GRAVE_ACCENT:
					event.key.code = Key.GRAVE;
					break;
				case GLFW_KEY_0:
					event.key.code = Key.ZERO;
					break;
				case GLFW_KEY_1:
					event.key.code = Key.ONE;
					break;
				case GLFW_KEY_2:
					event.key.code = Key.TWO;
					break;
				case GLFW_KEY_3:
					event.key.code = Key.THREE;
					break;
				case GLFW_KEY_4:
					event.key.code = Key.FOUR;
					break;
				case GLFW_KEY_5:
					event.key.code = Key.FIVE;
					break;
				case GLFW_KEY_6:
					event.key.code = Key.SIX;
					break;
				case GLFW_KEY_7:
					event.key.code = Key.SEVEN;
					break;
				case GLFW_KEY_8:
					event.key.code = Key.EIGHT;
					break;
				case GLFW_KEY_9:
					event.key.code = Key.NINE;
					break;
				case GLFW_KEY_MINUS:
					event.key.code = Key.MINUS;
					break;
				case GLFW_KEY_EQUAL:
					event.key.code = Key.EQUAL;
					break;
				case GLFW_KEY_F1:
					event.key.code = Key.F1;
					break;
				case GLFW_KEY_F2:
					event.key.code = Key.F2;
					break;
				case GLFW_KEY_F3:
					event.key.code = Key.F3;
					break;
				case GLFW_KEY_F4:
					event.key.code = Key.F4;
					break;
				case GLFW_KEY_F5:
					event.key.code = Key.F5;
					break;
				case GLFW_KEY_F6:
					event.key.code = Key.F6;
					break;
				case GLFW_KEY_F7:
					event.key.code = Key.F7;
					break;
				case GLFW_KEY_F8:
					event.key.code = Key.F8;
					break;
				case GLFW_KEY_F9:
					event.key.code = Key.F9;
					break;
				case GLFW_KEY_F10:
					event.key.code = Key.F10;
					break;
				case GLFW_KEY_F11:
					event.key.code = Key.F11;
					break;
				case GLFW_KEY_F12:
					event.key.code = Key.F12;
					break;
			}

			switch (action)
			{
				case GLFW_PRESS:
					event.type = Event.Type.KeyPressed;
					break;
				case GLFW_RELEASE:
					event.type = Event.Type.KeyReleased;
					break;
			}

			switch (mods)
			{
				case GLFW_MOD_ALT:
					event.key.alt = true;
					break;
				case GLFW_MOD_CONTROL:
					event.key.control = true;
					break;
				case GLFW_MOD_SHIFT:
					event.key.shift = true;
					break;
				case GLFW_MOD_SUPER:
					event.key.system = true;
					break;
			}

			eventQueue.add(event);
		});
	}

	private void setupMouseCallback()
	{
		glfwSetCursorPosCallback(window, new GLFWCursorPosCallback()
		{
			@Override
			public void invoke(long window, double posX, double posY)
			{
				mousePosition.x = (float)posX;
				mousePosition.y = (float)posY;
			}
		});

		glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback()
		{
			@Override
			public void invoke(long window, int button, int action, int mods)
			{
				Event event = new Event();
				event.type = Event.Type.MouseButtonPressed;
				switch (button)
				{
					case GLFW_MOUSE_BUTTON_1:
						event.mouseButton.button = MouseButton.LEFT_CLICK;
						break;
					case GLFW_MOUSE_BUTTON_2:
						event.mouseButton.button = MouseButton.RIGHT_CLICK;
						break;
					case GLFW_MOUSE_BUTTON_3:
						event.mouseButton.button = MouseButton.MIDDLE_CLICK;
						break;
				}
				event.mouseButton.x = mousePosition.x;
				event.mouseButton.y = mousePosition.y;
				eventQueue.add(event);
			}
		});

		glfwSetCursorPosCallback(window, new GLFWCursorPosCallback()
		{
			private Vector2d lastPosition;

			@Override
			public void invoke(long l, double v, double v1)
			{
				if(lastPosition == null) lastPosition = new Vector2d(v, v1);
				Event event = new Event();
				event.type = Event.Type.MouseMoved;
				event.mouseMove.x = (float)(v - lastPosition.x);
				event.mouseMove.y = (float)(v1 - lastPosition.y);
				eventQueue.add(event);

				lastPosition.x = v;
				lastPosition.y = v1;
			}
		});
	}

	@Override
	public void setTitle(String title)
	{
		glfwSetWindowTitle(window, title);
	}

	@Override
	public Vector2i getSize()
	{
		return new Vector2i(width, height);
	}

	@Override
	public boolean isOpen()
	{
		return isOpen;
	}

	@Override
	public void close()
	{
		isOpen = false;
	}

	@Override
	public void swapBuffers()
	{
		glfwSwapBuffers(window);
	}

	public boolean pollEvents(Event event)
	{
		if (eventQueue.size() == 0) return false;
		event.setEvent(eventQueue.remove());
		return true;
	}

	@Override
	public void hideCursor(boolean hide)
	{
		int mode;
		if(hide)
		{
			isCursorHidden = true;
			mode = GLFW_CURSOR_DISABLED;
		}
		else
		{
			isCursorHidden = false;
			mode = GLFW_CURSOR_NORMAL;
		}
		glfwSetInputMode(window, GLFW_CURSOR, mode);
	}
}
