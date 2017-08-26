package com.lukitree.engine.framework.graphics.renderer;

import com.lukitree.engine.framework.asset.*;
import com.lukitree.engine.framework.graphics.shader.*;
import com.lukitree.engine.framework.object.*;
import org.joml.*;

import java.util.*;

public class Renderer<S extends Shader, M extends Model, E>
{
	private S shader;

	public Renderer(S shader)
	{
		this.shader = shader;
	}

	public void render(Map<M, List<E>> entities, FreeRoamCamera camera, Matrix4f projectionTransform)
	{

	}
}
