package com.lukitree.engine.framework.object;

import com.lukitree.engine.framework.window.*;
import com.lukitree.engine.framework.window.event.*;
import org.joml.*;
import org.joml.Math;

public class FreeRoamCamera implements Camera
{
	protected Vector3f position = new Vector3f();
	protected Vector3f rotation = new Vector3f();

	private static final float MOVE_SPEED = 80;
	protected static final float SENSITIVITY = 0.5f;

	public FreeRoamCamera()
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

	@Override
	public Vector3f getRotation()
	{
		return null;
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

	public void move(float x, float y, float z)
	{
		move(new Vector3f(x,y,z));
	}

	public void move(Vector3f move)
	{
	}

	public void update(float dt)
	{
		Vector3f move = new Vector3f();

		if(Keyboard.isKeyPressed(Key.W)) move.z += MOVE_SPEED;
		if(Keyboard.isKeyPressed(Key.S)) move.z -= MOVE_SPEED;
		if(Keyboard.isKeyPressed(Key.A)) move.x += MOVE_SPEED;
		if(Keyboard.isKeyPressed(Key.D)) move.x -= MOVE_SPEED;
		if(Keyboard.isKeyPressed(Key.Q)) move.y += MOVE_SPEED;
		if(Keyboard.isKeyPressed(Key.E)) move.y -= MOVE_SPEED;

		if(rotation.x > 360) rotation.x -= 360;
		if(rotation.x < 0) rotation.x += 360;
		if(rotation.x > 90 && rotation.x < 180) rotation.x = 90;
		if(rotation.x < 270 && rotation.x > 180) rotation.x = 270;
		if(rotation.y > 360) rotation.y -= 360;
		if(rotation.y < 0) rotation.y += 360;
		if(rotation.z > 360) rotation.z -= 360;
		if(rotation.z < 0) rotation.z += 360;

		float cs = (float)Math.cos(Math.toRadians(rotation.y));
		float sn = (float)Math.sin(Math.toRadians(rotation.y));

		float pX = move.x * cs - move.z * sn;
		float pZ = move.x * sn + move.z * cs;

		position.x += pX * MOVE_SPEED * dt;
		position.y += move.y * MOVE_SPEED * dt;
		position.z += pZ * MOVE_SPEED * dt;

		if(position.y < 1) position.y = 1;
	}
}
