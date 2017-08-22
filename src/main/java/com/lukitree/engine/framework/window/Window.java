package com.lukitree.engine.framework.window;

import com.lukitree.engine.framework.window.event.*;
import org.joml.*;

import java.util.*;

public interface Window
{
	void setTitle(String title);
	Vector2i getSize();
	boolean isOpen();
	void close();
	void swapBuffers();
	boolean pollEvents(Event event);
	void hideCursor(boolean hide);
}
