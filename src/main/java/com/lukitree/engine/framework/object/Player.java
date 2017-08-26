package com.lukitree.engine.framework.object;

import com.lukitree.engine.framework.asset.*;
import com.lukitree.engine.framework.window.*;
import com.lukitree.engine.framework.window.event.*;
import org.joml.*;
import org.joml.Math;

public class Player extends Entity
{
	private static final float MOVE_SPEED = 50;
	private static final float TURN_SPEED = 270;
	private static final float GRAVITY = 120;
	private static final float JUMP_POWER = 40;
	private static final float TERRAIN_HEIGHT = 0;

	private float upwardsSpeed = 0;

	public Player(Model model)
	{
		super(model);
	}

	public void update(float dt)
	{
		float currentSpeed = 0;
		float currentTurnSpeed = 0;
		float currentStrafeSpeed = 0;

		if (Keyboard.isKeyPressed(Key.W) || (Mouse.isButtonPressed(MouseButton.LEFT_CLICK) && Mouse.isButtonPressed(MouseButton.RIGHT_CLICK))) currentSpeed += MOVE_SPEED;
		if (Keyboard.isKeyPressed(Key.S)) currentSpeed -= MOVE_SPEED;
		if (Mouse.isButtonPressed(MouseButton.RIGHT_CLICK))
		{
			if (Keyboard.isKeyPressed(Key.A)) currentStrafeSpeed += MOVE_SPEED;
			if (Keyboard.isKeyPressed(Key.D)) currentStrafeSpeed -= MOVE_SPEED;
		}
		else
		{
			if (Keyboard.isKeyPressed(Key.A)) currentTurnSpeed += TURN_SPEED;
			if (Keyboard.isKeyPressed(Key.D)) currentTurnSpeed -= TURN_SPEED;
		}
		if (Keyboard.isKeyPressed(Key.SPACE)) jump();


		float magnitude = (float)Math.abs(Math.sqrt(currentSpeed * currentSpeed + currentStrafeSpeed *
				                                                                          currentStrafeSpeed));
		if (magnitude != 0)
		{
			currentSpeed = (currentSpeed / magnitude) * MOVE_SPEED;
			currentStrafeSpeed = (currentStrafeSpeed / magnitude) * MOVE_SPEED;
		}

		super.rotate(0, currentTurnSpeed * dt, 0);

		super.move(0, upwardsSpeed * dt, 0);
		upwardsSpeed -= GRAVITY * dt;

		Vector3f position = getPosition();
		if (position.y < TERRAIN_HEIGHT)
		{
			upwardsSpeed = 0;
			position.y = TERRAIN_HEIGHT;
			setPosition(position);
		}

		Vector3f rotation = super.getRotation();
		float distance = currentSpeed * dt;
		float strafe = currentStrafeSpeed * dt;

		float dx = distance * (float)Math.sin(Math.toRadians(rotation.y));
		float dz = distance * (float)Math.cos(Math.toRadians(rotation.y));

		dx += strafe * (float)Math.sin(Math.toRadians(rotation.y + 90));
		dz += strafe * (float)Math.cos(Math.toRadians(rotation.y + 90));

		super.move(dx, 0, dz);
	}

	private void jump()
	{
		if (getPosition().y > TERRAIN_HEIGHT) return;
		upwardsSpeed = JUMP_POWER;
	}
}
