package com.lukitree.engine;

import com.lukitree.engine.data.Model;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer
{
	public void render(Model model)
	{
		glBindVertexArray(model.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glActiveTexture(model.getTextureID());
		glBindTexture(GL_TEXTURE_2D, model.getTextureID());

		float[] mvpTransform = new float[16];

		Matrix4f modelMatrix = model.getTransform();

		Matrix4f viewMatrix = new Matrix4f().translate(0.0f,
		                                               0.0f,
		                                               -3.0f);

		Matrix4f projMatrix = new Matrix4f().perspective((float) Math.toRadians(30.f),
		                                                 (float) 1024 / 768,
		                                                 0.1f,
		                                                 1000.0f);

		projMatrix.mul(viewMatrix.mul(modelMatrix)).get(mvpTransform);

		glUniformMatrix4fv(10, false, mvpTransform);

		glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}
}
