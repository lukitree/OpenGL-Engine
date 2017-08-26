package com.lukitree.engine.framework.window;

import com.lukitree.engine.framework.window.event.*;

import java.util.*;

public class Keyboard
{
	static Set<Key> keysPressed = new HashSet<>();

	public static boolean isKeyPressed(Key key)
	{
		return keysPressed.contains(key);
	}
}
