package com.lukitree.engine.framework.window.event;



public class Event
{
	public Type type = Type.None;

	public SizeEvent size = new SizeEvent();
	public KeyEvent key = new KeyEvent();
	public MouseMoveEvent mouseMove = new MouseMoveEvent();
	public MouseButtonEvent mouseButton = new MouseButtonEvent();
	public MouseWheelEvent mouseWheel = new MouseWheelEvent();

	public void setEvent(Event event)
	{
		this.type = event.type;
		this.size = event.size;
		this.key = event.key;
		this.mouseMove = event.mouseMove;
		this.mouseButton = event.mouseButton;
		this.mouseWheel = event.mouseWheel;
	}

	public enum Type
	{
		None,
		Closed,
		Resized,
		//LostFocus,
		//GainedFocus,
		//TextEntered,
		KeyPressed,
		KeyReleased,
		MouseWheelMoved,
		MouseButtonPressed,
		MouseButtonReleased,
		MouseMoved,
		//MouseEntered,
		//MouseLeft,
		Count,
	}
}
