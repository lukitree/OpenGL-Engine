package com.lukitree.engine.framework.graphics;

import com.lukitree.engine.framework.asset.*;
import com.lukitree.engine.framework.graphics.shader.*;
import com.lukitree.engine.framework.object.*;
import org.joml.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

public class TerrainRenderer
{
	private TerrainShader shader;

	public TerrainRenderer(TerrainShader shader)
	{
		this.shader = shader;
	}

	public void render(List<Terrain> terrains, Camera camera, Matrix4f projectionMatrix)
	{
		for(Terrain el : terrains)
		{
			prepareTerrain(el);
			loadMVPMatrix(el, camera, projectionMatrix);
			glDrawElements(GL_TRIANGLES, el.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			unbindTerrain();
		}
	}

	private void prepareTerrain(Terrain terrain)
	{
		Model model = terrain.getModel();

		glBindVertexArray(model.getVaoID());
		if(model instanceof TexturedModel)
		{
			Texture texture = ((TexturedModel)model).getTexture();
			Attribute.TEX_COORD.enable();
			shader.loadTexture(texture);
			glActiveTexture(GL_TEXTURE_2D);
			glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
		}

		Attribute.POSITION.enable();
		Attribute.NORMAL.enable();
	}

	private void unbindTerrain()
	{
		Attribute.POSITION.disable();
		Attribute.TEX_COORD.disable();
		Attribute.NORMAL.disable();
		glBindVertexArray(0);
	}

	private void loadMVPMatrix(Terrain terrain, Camera camera, Matrix4f projectionMatrix)
	{
		Matrix4f modelMatrix = new Matrix4f();
		modelMatrix.translate(terrain.getX(), 0, terrain.getZ());
		Matrix4f viewMatrix = new Matrix4f(camera.getViewMatrix());
		Matrix4f projMatrix = new Matrix4f(projectionMatrix);

		shader.loadModelTransform(modelMatrix);
		shader.loadViewTransform(viewMatrix);
		shader.loadProjectionTransform(projMatrix);
		shader.loadMVP(new Matrix4f(projMatrix.mul(viewMatrix.mul(modelMatrix))));
	}
}
