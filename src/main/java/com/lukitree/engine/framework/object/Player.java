package com.lukitree.engine.framework.object;

import com.lukitree.engine.framework.asset.*;
import org.joml.*;
import org.joml.Math;

public class Player extends Entity
{
	private static final float MOVE_SPEED = 20;
	private static final float RUN_SPEED_MODIFIER = 1.5f;
	private static final float TURN_SPEED = 160;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;

	private boolean isMovingForward = false;
	private boolean isMovingBackwards = false;
	private boolean isTurningLeft = false;
	private boolean isTurningRight = false;
	private boolean isRunning = false;

	public Player(Model model)
	{
		super(model);
	}

	public void update(float dt)
	{
		float currentRunModifier = (isRunning) ? RUN_SPEED_MODIFIER : 1.0f;

		if(isMovingForward) currentSpeed += MOVE_SPEED * currentRunModifier;
		if(isMovingBackwards) currentSpeed -= MOVE_SPEED * currentRunModifier;

		if(isTurningLeft) currentTurnSpeed += TURN_SPEED;
		if(isTurningRight) currentTurnSpeed -= TURN_SPEED;

		super.rotate(0, currentTurnSpeed * dt, 0);

		float distance = currentSpeed * dt;
		Vector3f rotation = super.getRotation();
		float dx = distance * (float)Math.sin(Math.toRadians(rotation.y));
		float dz = distance * (float)Math.cos(Math.toRadians(rotation.y));
		super.move(dx, 0, dz);

		currentSpeed = 0;
		currentTurnSpeed = 0;
	}

	public void setMovingForward(boolean movingForward)
	{
		isMovingForward = movingForward;
	}

	public void setMovingBackwards(boolean movingBackwards)
	{
		isMovingBackwards = movingBackwards;
	}

	public void setTurningLeft(boolean turningLeft)
	{
		isTurningLeft = turningLeft;
	}

	public void setTurningRight(boolean turningRight)
	{
		isTurningRight = turningRight;
	}

	public void setRunning(boolean running)
	{
		isRunning = running;
	}
}
