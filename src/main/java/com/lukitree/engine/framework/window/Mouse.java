package com.lukitree.engine.framework.window;

import com.lukitree.engine.framework.window.event.*;
import org.joml.*;

import java.util.*;

public class Mouse
{
	static Set<MouseButton> buttonsPressed = new HashSet<>();
	static Vector2f position = new Vector2f();
	static float wheelPosition = 0;

	public static boolean isButtonPressed(MouseButton button)
	{
		return buttonsPressed.contains(button);
	}

	public static Vector2f getPosition()
	{
		return position;
	}

	public static float getWheelPosition()
	{
		return wheelPosition;
	}
}
