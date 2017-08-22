package com.lukitree.engine.framework.object;

import org.joml.*;
import org.joml.Math;

public class Camera
{
	private Vector3f position = new Vector3f();
	private Vector3f rotation = new Vector3f();

	private Vector3f move = new Vector3f();

	private static final float MOVE_SPEED = 80;
	private static final float SENSITIVITY = 0.75f;

	public Camera()
	{
		this.setPosition(0,3,0);
		this.setRotation(0,0,0);
	}

	public Matrix4f getViewMatrix()
	{
		Matrix4f view = new Matrix4f();

		Vector3f reversePosition = new Vector3f();
		position.mul(-1, reversePosition);

		view.rotate((float)Math.toRadians(rotation.x), 1.0f, 0.0f, 0.0f)
		    .rotate((float)Math.toRadians(rotation.y + 180), 0.0f, 1.0f, 0.0f)
			.translate(reversePosition);

		return view;
	}

	public Vector3f getPosition()
	{
		return position;
	}

	public void setPosition(Vector3f newPosition)
	{
		this.position = new Vector3f(-newPosition.x, newPosition.y, newPosition.z);
		if(position.y < 0) this.position.y = 0;
	}

	public void setPosition(float x, float y, float z)
	{
		setPosition(new Vector3f(x, y, z));
	}

	public void setRotation(Vector3f newRotation)
	{
		this.rotation = newRotation;
	}

	public void setRotation(float angleX, float angleY, float angleZ)
	{
		setRotation(new Vector3f(angleX, angleY, angleZ));
	}

	public void rotate(Vector3f rotation)
	{
		Vector3f rot = new Vector3f(rotation);
		rot.mul(SENSITIVITY);
		this.rotation.add(rot);
	}

	public void rotate(float angleX, float angleY, float angleZ)
	{
		rotate(new Vector3f(angleX, angleY, angleZ));
	}

	public void move(Move moveDirection, boolean enable)
	{
		int val = (enable) ? 1 : -1;
		switch (moveDirection)
		{
			case UP:
				move.y += val;
				break;
			case DOWN:
				move.y -= val;
				break;
			case LEFT:
				move.x += val;
				break;
			case RIGHT:
				move.x -= val;
				break;
			case FOWARD:
				move.z += val;
				break;
			case BACKWARD:
				move.z -= val;
				break;
		}
	}

	public void update(float dt)
	{
		if(rotation.x > 360) rotation.x -= 360;
		if(rotation.x < 0) rotation.x += 360;
		if(rotation.x > 90 && rotation.x < 180) rotation.x = 90;
		if(rotation.x < 270 && rotation.x > 180) rotation.x = 270;
		if(rotation.y > 360) rotation.y -= 360;
		if(rotation.y < 0) rotation.y += 360;
		if(rotation.z > 360) rotation.z -= 360;
		if(rotation.z < 0) rotation.z += 360;

		position.x += move.x * MOVE_SPEED * dt;
		position.y += move.y * MOVE_SPEED * dt;
		position.z += move.z * MOVE_SPEED * dt;

		if(position.y < 1) position.y = 1;
	}

	public enum Move
	{
		UP,
		DOWN,
		LEFT,
		RIGHT,
		FOWARD,
		BACKWARD;
	}

	public enum Rotate
	{
		UP,
		DOWN,
		LEFT,
		RIGHT,
		TILT_LEFT,
		TILT_RIGHT;
	}
}
