package com.lukitree.engine.framework.object;

import org.joml.*;

public interface Camera
{
	void update(float dt);

	void setPosition(Vector3f position);
	void setPosition(float x, float y, float z);
	Vector3f getPosition();
	void move(Vector3f move);
	void move(float x, float y, float z);

	void setRotation(Vector3f rotation);
	void setRotation(float angleX, float angleY, float angleZ);
	Vector3f getRotation();
	void rotate(Vector3f rotate);
	void rotate(float angleX, float angleY, float angleZ);

	Matrix4f getViewMatrix();
}
