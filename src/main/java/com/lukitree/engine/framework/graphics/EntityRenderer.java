package com.lukitree.engine.framework.graphics;

import com.lukitree.engine.framework.object.*;
import com.lukitree.engine.framework.asset.*;
import com.lukitree.engine.framework.object.Entity;
import com.lukitree.engine.framework.graphics.shader.*;
import org.joml.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

public class EntityRenderer
{
	private StaticShader shader;

	public EntityRenderer(StaticShader shader)
	{
		this.shader = shader;
	}

	public void render(Map<Model, List<Entity>> entities, Camera camera, Matrix4f projectionTransform)
	{
		for(Model model : entities.keySet())
		{
			prepareModel(model);

			List<Entity> batch = entities.get(model);

			for(Entity entity : batch)
			{
				prepareInstance(entity, camera, projectionTransform);
				glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
			}

			unbindModel();
		}
	}

	private void prepareModel(Model model)
	{
		glBindVertexArray(model.getVaoID());
		if(model instanceof  TexturedModel)
		{
			TexturedModel texturedModel = (TexturedModel)model;
			if(texturedModel.hasTransparency()) MasterRenderer.disableCulling();

			Texture texture = texturedModel.getTexture();
			Attribute.TEX_COORD.enable();
			shader.loadTexture(texture);
			glActiveTexture(GL_TEXTURE_2D);
			glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
		}
		Attribute.POSITION.enable();
		Attribute.NORMAL.enable();

		shader.loadFakeLightingFlag(model.usesFakeLighting());
		shader.loadAmbientLightLevel(model.getAmbientLightLevel());
	}

	private void unbindModel()
	{
		MasterRenderer.enableCulling();
		Attribute.POSITION.disable();
		Attribute.TEX_COORD.disable();
		Attribute.NORMAL.disable();
		glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity, Camera camera, Matrix4f projectionMatrix)
	{
		Matrix4f modelMatrix = new Matrix4f(entity.getTransform());
		Matrix4f viewMatrix = new Matrix4f(camera.getViewMatrix());
		Matrix4f projMatrix = new Matrix4f(projectionMatrix);

		shader.loadModelTransform(modelMatrix);
		shader.loadViewTransform(viewMatrix);
		shader.loadProjectionTransform(projMatrix);
		shader.loadMVP(new Matrix4f(projMatrix.mul(viewMatrix.mul(modelMatrix))));
	}
}
