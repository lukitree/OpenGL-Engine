package com.lukitree.engine.framework.object;


import com.lukitree.engine.framework.window.*;
import com.lukitree.engine.framework.window.event.*;
import org.joml.*;
import org.joml.Math;

public class PlayerCamera implements Camera
{
	private static float LOOK_PITCH_SENSITIVITY = 0.5f;
	private static float LOOK_YAW_SENSITIVITY = 0.75f;
	private static float ZOOM_SENSITIVITY = 2;
	private static float FOCUS_HEIGHT = 5;

	private Vector3f position = new Vector3f(0, 5, 0);

	private float pitch = 90;
	private float yaw = 0;
	private float roll = 0;

	private Player player;
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;

	float lastMouseWheelPosition;
	Vector2f lastMousePosition = new Vector2f();
	Vector2f mouseDelta = new Vector2f();

	public PlayerCamera(Player player)
	{
		this.player = player;
		lastMousePosition = Mouse.getPosition();
	}

	private void getMouseDelta()
	{
		Vector2f mousePosition = Mouse.getPosition();
		mouseDelta.x = mousePosition.x - lastMousePosition.x;
		mouseDelta.y = mousePosition.y - lastMousePosition.y;
		lastMousePosition = new Vector2f(mousePosition);
	}

	private void calculateZoom(float dt)
	{
		float mouseWheelPosition = Mouse.getWheelPosition();
		float delta = mouseWheelPosition - lastMouseWheelPosition;
		lastMouseWheelPosition = mouseWheelPosition;

		distanceFromPlayer -= delta * ZOOM_SENSITIVITY;
		distanceFromPlayer = Math.max(Math.min(distanceFromPlayer, 50), 0);
	}

	private void calculatePitch(float dt)
	{
		boolean leftClicked = Mouse.isButtonPressed(MouseButton.LEFT_CLICK);
		boolean rightClicked = Mouse.isButtonPressed(MouseButton.RIGHT_CLICK);
		if (leftClicked || rightClicked)
		{
			float pitchChange = mouseDelta.y * LOOK_PITCH_SENSITIVITY;
			pitch += pitchChange;
			pitch = Math.max(Math.min(pitch, 90), -90);
		}
	}

	private void calculateAngleAroundPlayer(float dt)
	{
		float angleChange = mouseDelta.x * LOOK_YAW_SENSITIVITY;

		if (Mouse.isButtonPressed(MouseButton.LEFT_CLICK) && !Mouse.isButtonPressed(MouseButton.RIGHT_CLICK))
		{
			angleAroundPlayer -= angleChange;
		}
		else if (Mouse.isButtonPressed(MouseButton.RIGHT_CLICK))
		{
			player.rotate(0, angleAroundPlayer, 0);
			angleAroundPlayer = 0;
			player.rotate(0, -angleChange, 0);
		}
	}

	private void calculatePosition()
	{
		Vector3f playerPosition = new Vector3f(player.getPosition());
		playerPosition.y += FOCUS_HEIGHT;
		float playerRotation = player.getRotation().y;

		float horizontalDistance = (float)(distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
		float verticalDistance = (float)(distanceFromPlayer * Math.sin(Math.toRadians(pitch)));

		float theta = playerRotation + angleAroundPlayer;
		float offsetX = (float)(horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float)(horizontalDistance * Math.cos(Math.toRadians(theta)));

		position.x = playerPosition.x - offsetX;
		position.y = playerPosition.y + verticalDistance;
		position.z = playerPosition.z - offsetZ;
		yaw = 180 - theta;

		if (position.y < 1) position.y = 1;
	}

	@Override
	public void update(float dt)
	{
		getMouseDelta();
		calculateZoom(dt);
		calculatePitch(dt);
		calculateAngleAroundPlayer(dt);
		calculatePosition();
	}

	@Override
	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	@Override
	public void setPosition(float x, float y, float z)
	{
		setPosition(new Vector3f(x, y, z));
	}

	@Override
	public Vector3f getPosition()
	{
		return position;
	}

	@Override
	public void move(Vector3f move)
	{
		position.add(move);
	}

	@Override
	public void move(float x, float y, float z)
	{
		move(new Vector3f(x, y, z));
	}

	@Override
	public void setRotation(Vector3f rotation)
	{
		setRotation(rotation.x, rotation.y, rotation.z);
	}

	@Override
	public void setRotation(float angleX, float angleY, float angleZ)
	{
		pitch = angleX;
		yaw = angleY;
		roll = angleZ;
	}

	@Override
	public Vector3f getRotation()
	{
		return new Vector3f(pitch, yaw, roll);
	}

	@Override
	public void rotate(Vector3f rotate)
	{
		rotate(rotate.x, rotate.y, rotate.z);
	}

	@Override
	public void rotate(float angleX, float angleY, float angleZ)
	{
		pitch += angleX;
		yaw += angleY;
		roll += angleZ;
	}

	@Override
	public Matrix4f getViewMatrix()
	{
		Vector3f reversePosition = new Vector3f(-position.x, -position.y, -position.z);

		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.rotate((float)Math.toRadians(pitch), 1, 0, 0)
		          .rotate((float)Math.toRadians(yaw), 0, 1, 0)
		          .rotate((float)Math.toRadians(roll), 0, 0, 1)
		          .translate(reversePosition);

		return viewMatrix;
	}
}
